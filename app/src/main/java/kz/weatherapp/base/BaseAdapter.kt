package kz.weatherapp.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, Binding : ViewBinding>() :
    RecyclerView.Adapter<BaseAdapter.ViewHolder<T, Binding>>() {

    protected val items: MutableList<T> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder<T, Binding>, position: Int) {
        holder.show(items[position])
    }

    open fun setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    open fun addItems(items: List<T>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    open fun getItem(position: Int): T? {
        return this.items[position]
    }

    open fun getRVItems() = items

    override fun getItemCount(): Int = items.size

    open class ViewHolder<T, Binding : ViewBinding>(
        protected val binding: Binding
    ) : RecyclerView.ViewHolder(binding.root) {

        open fun show(data: T) {}
    }
}
