package com.kerry.ubiquitiassignment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.kerry.ubiquitiassignment.databinding.ActivityMainBinding
import com.kerry.ubiquitiassignment.model.Record
import com.kerry.ubiquitiassignment.ui.AboveAvgPmAdapter
import com.kerry.ubiquitiassignment.ui.BelowAvgPmAdapter
import com.kerry.ubiquitiassignment.utils.*
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
//        binding.ivSearch.setOnClickListener {
//            binding.ivBack.visible()
//            binding.editKeyword.visible()
//            binding.editKeyword.isEnabled = true
//            binding.editKeyword.showKeyboard()
//            binding.tvTitle.gone()
//            binding.ivSearch.gone()
//            binding.root.transitionToEnd()
//            viewModel.onTextChanged("")
//        }
//
//        binding.ivBack.setOnClickListener {
//            binding.tvTitle.visible()
//            binding.ivSearch.visible()
//            binding.editKeyword.setText("")
//            binding.editKeyword.isEnabled = false
//            binding.editKeyword.gone()
//            binding.ivBack.gone()
//            binding.root.transitionToStart()
//        }
//
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
            )
            .show()
    }

}