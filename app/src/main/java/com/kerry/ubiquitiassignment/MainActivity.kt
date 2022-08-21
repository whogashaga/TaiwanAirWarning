package com.kerry.ubiquitiassignment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.kerry.ubiquitiassignment.databinding.ActivityMainBinding
import com.kerry.ubiquitiassignment.ui.BelowPm30Adapter
import com.kerry.ubiquitiassignment.utils.dp
import com.kerry.ubiquitiassignment.utils.gone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()
    private val belowPm30Adapter = BelowPm30Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel.fetchRecordList()

        setupRecyclerView()

        viewModel.recordsBelowPm30.observe(this) {
            belowPm30Adapter.submitList(it)
        }

    }

    private fun setupRecyclerView() {
        with(binding.rvBelowPm30) {
            adapter = belowPm30Adapter
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

    }
}