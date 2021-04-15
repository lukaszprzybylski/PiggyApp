package com.example.piggy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.piggy.database.City
import com.example.piggy.databinding.RecyclerviewItemBinding

private var summary:Int = 0

class MainAdapter(val clickListener: ContentsListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var contentsList = mutableListOf<City>()

    fun setContentsList(list: List<City>) {
        contentsList.clear()
        contentsList.addAll(list)
        summary = list.sumOf { it.counter }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContentsViewHolder.from(parent)
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
            is ContentsViewHolder -> {
                val header = getItem(position)
                holder.bind(header, clickListener)
                holder.setIsRecyclable(false)
            }
        }
    }
}

class ContentsViewHolder private constructor(val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: City, clickListener: ContentsListener) {
        binding.content = item
        binding.summary = summary
        binding.clickListener = clickListener
    }

    companion object {
        fun from(parent: ViewGroup): ContentsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RecyclerviewItemBinding.inflate(layoutInflater, parent, false)
            return ContentsViewHolder(binding)
        }
    }
}
class ContentsListener(val clickListener: (content:String) -> Unit, val deleteListener: (content: City) -> Unit) {
    fun onClick(content: City) = clickListener(content.title)

    fun onDelete(content: City) = deleteListener(content)
}

