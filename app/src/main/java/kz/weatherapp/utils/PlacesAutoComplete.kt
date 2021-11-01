package kz.weatherapp.utils

import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.*

class PlacesAutoComplete() {

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    var places: ((List<String>) -> Unit)? = null


    private var token: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()


    fun createRequest(query: String, placesClient: PlacesClient) {

        scope.launch {
            val request =
                FindAutocompletePredictionsRequest.builder()
                    // Call either setLocationBias() OR setLocationRestriction().
                    //.setLocationRestriction(bounds)
                    .setTypeFilter(TypeFilter.CITIES)
                    .setSessionToken(token)
                    .setQuery(query)
                    .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    val result = mutableListOf<String>()
                    for (prediction in response.autocompletePredictions) {
                        result.add(prediction.getPrimaryText(null).toString())
                    }
                    places?.invoke(result)
                }.addOnFailureListener {

                }
        }
    }

    fun cleanUp() {
        // Cancel the scope to cancel ongoing coroutines work
        scope.cancel()
    }


}