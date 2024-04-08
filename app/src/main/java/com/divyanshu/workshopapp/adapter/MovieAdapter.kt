package com.divyanshu.workshopapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.divyanshu.workshopapp.databinding.MovieItemBinding
import com.divyanshu.workshopapp.model.Content
import com.divyanshu.workshopapp.utils.Utils.Companion.getMappedPosters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MovieAdapter(private val coroutineScope: CoroutineScope) :
    PagingDataAdapter<Content, MovieAdapter.MyViewHolder>(diffCallback = diffCallback), Filterable {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Content>() {
            override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class MyViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            txtMovie.text = item?.name
            val imageLink = item?.posterImage?.getMappedPosters()
            imgMovie.load(imageLink) {
                crossfade(true)
                crossfade(500)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                var filteredList = emptyList<Content>()

                this@MovieAdapter.snapshot().items.let {
                    filteredList = if (constraint.isNullOrEmpty()) {
                        it
                    } else {
                        it.filter { item -> item.name.contains(constraint, ignoreCase = true) }
                    }
                }
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                coroutineScope.launch {
                    val filteredResults: List<Content> = results?.values as List<Content>
                    val filteredData = PagingData.from(filteredResults)
                    this@MovieAdapter.submitData(filteredData)
                }

            }
        }
    }
}