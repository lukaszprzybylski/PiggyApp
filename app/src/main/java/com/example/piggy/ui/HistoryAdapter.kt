package com.example.piggy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.piggy.database.City
import com.example.piggy.databinding.NewCityItemBinding

class HistoryAdapter(val clickListener: ContentsNewListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var contentsList = mutableListOf<City>()

    fun setContentsList(list: List<City>) {
        contentsList.clear()
        contentsList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContentsNewViewHolder.from(parent)
    }

    override fun getItemId(position: Int): Long {
        return contentsList[position].hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return contentsList.size
    }

    private fun getItem(position: Int): City {
        return contentsList[position]
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContentsNewViewHolder -> {
                val header = getItem(position)
                holder.bind(header, clickListener)
                holder.setIsRecyclable(false)
            }
        }
    }
}

class ContentsNewViewHolder private constructor(private val binding: NewCityItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: City, clickListener: ContentsNewListener) {
        binding.content = item
        binding.clickListener = clickListener
    }

    companion object {
        fun from(parent: ViewGroup): ContentsNewViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = NewCityItemBinding.inflate(layoutInflater, parent, false)
            return ContentsNewViewHolder(binding)
        }
    }
}
class ContentsNewListener(val clickListener: (content: String) -> Unit, val deleteListener: (content: City) -> Unit) {
    fun onClick(content: City) = clickListener(content.title)

    fun onDelete(content: City) = deleteListener(content)
}

