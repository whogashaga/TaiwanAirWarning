package com.kerry.ubiquitiassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kerry.ubiquitiassignment.R
import com.kerry.ubiquitiassignment.databinding.ItemAbovePm30Binding
import com.kerry.ubiquitiassignment.model.Record
import com.kerry.ubiquitiassignment.utils.gone
import com.kerry.ubiquitiassignment.utils.visible

class AboveAvgPmAdapter : ListAdapter<Record?, AboveAvgPmViewHolder>(
    object : DiffUtil.ItemCallback<Record?>() {

        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean =
            oldItem.siteId == newItem.siteId

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean =
            oldItem == newItem

    }
) {

    var onArrowClick: (Record) -> Unit  = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboveAvgPmViewHolder =
        AboveAvgPmViewHolder(
            ItemAbovePm30Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onArrowClick
        )

    override fun onBindViewHolder(holder: AboveAvgPmViewHolder, position: Int) {
        holder.bindView(position, getItem(position))
    }
}

class AboveAvgPmViewHolder(
    private val binding: ItemAbovePm30Binding,
    private val onArrowClick: (Record) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindView(position: Int, record: Record?) {
        binding.tvSiteId.text = record?.siteId.orEmpty()
        binding.tvSiteName.text = record?.siteName.orEmpty()
        binding.tvPmValue.text = record?.pmTwoPointFive.orEmpty()
        binding.tvCounty.text = record?.county.orEmpty()
        binding.tvStatus.text = record?.customStatus

        if (record?.isStatusGood == true) {
            binding.ivArrow.gone()
            binding.root.setBackgroundResource(R.drawable.bg_card_white)
            binding.root.setOnClickListener {  }
        } else {
            binding.ivArrow.visible()
            binding.root.setBackgroundResource(R.drawable.bg_card_white_clickable)
            binding.root.setOnClickListener {
                if (record != null) {
                    onArrowClick.invoke(record)
                }
            }
        }
    }

}