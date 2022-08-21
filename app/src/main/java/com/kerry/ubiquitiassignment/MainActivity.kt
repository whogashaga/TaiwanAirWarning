package com.kerry.ubiquitiassignment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.kerry.ubiquitiassignment.databinding.ActivityMainBinding
import com.kerry.ubiquitiassignment.ui.AboveAvgPmAdapter
import com.kerry.ubiquitiassignment.ui.BelowAvgPmAdapter
import com.kerry.ubiquitiassignment.utils.dp
import com.kerry.ubiquitiassignment.utils.gone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()
    private val belowAvgPmAdapter = BelowAvgPmAdapter()
    private val aboveAvgPmAdapter = AboveAvgPmAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel.fetchRecordList()

        setupRecyclerView()

        viewModel.recordsBelowAvg.observe(this) {
            belowAvgPmAdapter.submitList(it) {
                binding.shimmerLoading.root.gone()
            }
        }

        viewModel.recordsAboveAvg.observe(this) {
            aboveAvgPmAdapter.submitList(it)
        }

    }

    private fun setupRecyclerView() {
        with(binding.rvBelowAvgPm) {
            adapter = belowAvgPmAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    when (parent.getChildAdapterPosition(view)) {
                        RecyclerView.NO_POSITION -> super.getItemOffsets(outRect, view, parent, state)
                        0 -> outRect.set(16.dp, 16.dp, 16.dp, 16.dp)
                        else -> outRect.set(0, 16.dp, 16.dp, 16.dp)
                    }
                }
            })
        }

        with(binding.rvAboveAvgPm) {
            adapter = aboveAvgPmAdapter.apply {
                onArrowClick = {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("注意空汙")
                        .setMessage("目前${it.county.orEmpty()} PM2.5 為 ${it.pmTwoPointFive}\n出門前請三思或戴好口罩.")
                        .show()
                }
            }
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    when (parent.getChildAdapterPosition(view)) {
                        RecyclerView.NO_POSITION -> super.getItemOffsets(outRect, view, parent, state)
                        0 -> outRect.set(16.dp, 16.dp, 16.dp, 16.dp)
                        else -> outRect.set(16.dp, 0, 16.dp, 16.dp)
                    }
                }
            })
        }
    }
}