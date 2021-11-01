package kz.weatherapp.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import kz.domain.entities.WeatherData
import kz.weatherapp.base.BaseFragment
import kz.weatherapp.databinding.FragmentMainBinding
import kz.weatherapp.presentation.main.adapter.WeatherAdapter
import kz.weatherapp.utils.DebouncingTextWatcher
import kz.weatherapp.utils.PlacesAutoComplete
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()

        private const val MILLIS_IN_HOUR = 3600000
        private const val MINIMUM_SEARCH_LENGTH = 2
    }


    private lateinit var placesAutoComplete: PlacesAutoComplete
    private lateinit var placesClient: PlacesClient
    private val viewModel by viewModel<MainViewModel>()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var isFromLocalDB = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.dispatch(Action.GetSavedData)
        }

        setupView()
        setupPlacesAutoComplete()
        setupListeners()
        setupVM()
    }

    override fun onDestroy() {
        super.onDestroy()
        placesAutoComplete.cleanUp()
    }

    private fun setupView() {
        binding.recyclerWeather.adapter = WeatherAdapter()
        binding.recyclerWeather.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun setupPlacesAutoComplete() {
        placesAutoComplete = PlacesAutoComplete()
        placesClient = Places.createClient(requireContext())

        placesAutoComplete.places = { places ->
            viewModel.clearWeatherData()

            places.toSet().apply {
                forEach {
                    viewModel.dispatch(Action.Search(it, this.size))
                }
            }
        }
    }

    private fun setupListeners() {
        binding.inputFindCityWeather.addTextChangedListener(DebouncingTextWatcher(this.lifecycle) { searchText ->
            searchText?.let {
                if (it.length <= MINIMUM_SEARCH_LENGTH || isFromLocalDB) {
                    isFromLocalDB = false
                    return@let
                } else {
                    placesAutoComplete.createRequest(searchText, placesClient)
                }
            }
        })
    }

    private fun getAdapter() = (binding.recyclerWeather.adapter as WeatherAdapter)

    private fun setupVM() {
        viewModel.state.subscribe { state ->
            when (state) {
                is State.Success -> {
                   setProgressState(View.GONE)
                    setAdapterItems(state.weatherDataList)

                    viewModel.dispatch(
                        Action.SaveData(
                            state.weatherDataList,
                            binding.inputFindCityWeather.text.toString()
                        )
                    )
                }
                is State.Error -> {
                    setProgressState(View.GONE)

                    handleError(errorDetails = state.error)
                }
                is State.Loading -> {
                    setProgressState(View.VISIBLE)
                }
                is State.LocalSavedData -> {

                    if (kotlin.math.abs(state.time - System.currentTimeMillis()) > MILLIS_IN_HOUR) {
                        placesAutoComplete.createRequest(query = state.query, placesClient)
                        return@subscribe
                    }
                    isFromLocalDB = true
                    setAdapterItems(state.weatherDataList)
                    binding.inputFindCityWeather.setText(state.query)
                }
            }
        }
    }

    private fun setAdapterItems(weatherData: List<WeatherData>) = getAdapter().setItems(weatherData)

    private fun setProgressState(visibility: Int) {
        binding.progressBar.visibility = visibility
    }


}
