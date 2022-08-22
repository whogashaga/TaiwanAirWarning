package com.kerry.ubiquitiassignment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.kerry.ubiquitiassignment.databinding.ActivityMainBinding
import com.kerry.ubiquitiassignment.model.Record
import com.kerry.ubiquitiassignment.ui.AboveAvgPmAdapter
import com.kerry.ubiquitiassignment.ui.BelowAvgPmAdapter
import com.kerry.ubiquitiassignment.utils.dp
import com.kerry.ubiquitiassignment.utils.gone
import com.kerry.ubiquitiassignment.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()
    private val belowAvgPmAdapter = BelowAvgPmAdapter()
    private val aboveAvgPmAdapter = AboveAvgPmAdapter()
    private val searchRecordsAdapter = AboveAvgPmAdapter()
    private val verticalDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            rect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            when (parent.getChildAdapterPosition(view)) {
                RecyclerView.NO_POSITION -> super.getItemOffsets(rect, view, parent, state)
                0 -> rect.set(16.dp, 16.dp, 16.dp, 16.dp)
                else -> rect.set(16.dp, 0, 16.dp, 16.dp)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Toast.makeText(this, getString(R.string.alert_api_speed_slow), Toast.LENGTH_LONG).show()

        viewModel.fetchRecordList()

        setupToolbar()
        setupRecyclerView()
        observeLiveData()

    }

    private fun setupToolbar() {
        with(binding.toolbar) {
            onSearchClick = {
                binding.root.transitionToEnd()
            }
            onBackClick = {
                binding.root.transitionToStart()
            }
            onEditTextChanged = {
                viewModel.onTextChanged(it)
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding.rvBelowAvgPm) {
            adapter = belowAvgPmAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    rect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    when (parent.getChildAdapterPosition(view)) {
                        RecyclerView.NO_POSITION -> super.getItemOffsets(rect, view, parent, state)
                        0 -> rect.set(16.dp, 16.dp, 16.dp, 16.dp)
                        else -> rect.set(0, 16.dp, 16.dp, 16.dp)
                    }
                }
            })
        }

        with(binding.rvAboveAvgPm) {
            adapter = aboveAvgPmAdapter.apply {
                onArrowClick = {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle(R.string.air_alert_title)
                        .setMessage(
                            getString(
                                R.string.air_alert_content,
                                it.county.orEmpty(),
                                it.pmTwoPointFive.orEmpty()
                            )
                        )
                        .show()
                }
            }
            addItemDecoration(verticalDecoration)
        }

        with(binding.rvSearchRecords) {
            adapter = searchRecordsAdapter.apply {
                onArrowClick = {
                    showPm25AlertDialog(it)
                }
            }
            addItemDecoration(verticalDecoration)
        }
    }

    private fun observeLiveData() {
        viewModel.recordsBelowAvg.observe(this) {
            belowAvgPmAdapter.submitList(it) {
                binding.shimmerLoading.root.gone()
            }
        }

        viewModel.recordsAboveAvg.observe(this) {
            aboveAvgPmAdapter.submitList(it)
        }

        viewModel.searchedRecords.observe(this) {
            searchRecordsAdapter.submitList(it) {
                binding.tvMessage.run { if (it.isEmpty()) visible() else gone() }
            }
        }

        viewModel.enableErrorAlert.observe(this) {
            if (it) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.api_error_alert_title)
                    .setMessage(R.string.api_error_alert_content)
                    .setCancelable(false)
                    .setPositiveButton(R.string.txt_confirm) { _, _ ->
                        binding.shimmerLoading.root.showShimmer(true)
                        viewModel.fetchRecordList()
                    }
                    .setNegativeButton(R.string.txt_cancel) { _, _ -> }
                    .show()
                binding.shimmerLoading.root.hideShimmer()
            }

        }
    }

    private fun showPm25AlertDialog(it: Record) {
        AlertDialog.Builder(this@MainActivity)
            .setTitle(R.string.air_alert_title)
            .setMessage(
                getString(
                    R.string.air_alert_content,
                    it.county.orEmpty(),
                    it.pmTwoPointFive.orEmpty()
                )
            ).show()
    }

}