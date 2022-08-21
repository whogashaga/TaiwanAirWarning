package com.kerry.ubiquitiassignment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kerry.ubiquitiassignment.databinding.ItemBelowPm30Binding
import com.kerry.ubiquitiassignment.model.Record

class BelowPm30Adapter : ListAdapter<Record?, BelowPm30ViewHolder>(
    object : DiffUtil.ItemCallback<Record?>() {

        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean =
            oldItem.siteId == newItem.siteId

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean =
            oldItem == newItem

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BelowPm30ViewHolder =
        BelowPm30ViewHolder(
            ItemBelowPm30Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: BelowPm30ViewHolder, position: Int) {
        holder.bindView(position, getItem(position))
    }
}

class BelowPm30ViewHolder(
    private val binding: ItemBelowPm30Binding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindView(position: Int, record: Record?) {

    }

}