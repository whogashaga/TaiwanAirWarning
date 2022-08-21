package com.kerry.ubiquitiassignment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kerry.ubiquitiassignment.databinding.ItemBelowPm30Binding
import com.kerry.ubiquitiassignment.model.Record

class BelowAvgPmAdapter : ListAdapter<Record?, BelowAvgPmViewHolder>(
    object : DiffUtil.ItemCallback<Record?>() {

        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean =
            oldItem.siteId == newItem.siteId

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean =
            oldItem == newItem

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BelowAvgPmViewHolder =
        BelowAvgPmViewHolder(
            ItemBelowPm30Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: BelowAvgPmViewHolder, position: Int) {
        holder.bindView(position, getItem(position))
    }
}

class BelowAvgPmViewHolder(
    private val binding: ItemBelowPm30Binding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindView(position: Int, record: Record?) {
        binding.tvSiteId.text = record?.siteId.orEmpty()
        binding.tvSiteName.text = record?.siteName.orEmpty()
        binding.tvPmValue.text = record?.pmTwoPointFive.orEmpty()
        binding.tvCounty.text = record?.county.orEmpty()
        binding.tvStatus.text = record?.status.orEmpty()
    }

}