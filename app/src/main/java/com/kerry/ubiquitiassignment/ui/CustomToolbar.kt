package com.kerry.ubiquitiassignment.ui

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StyleableRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.addTextChangedListener
import com.kerry.ubiquitiassignment.R
import com.kerry.ubiquitiassignment.databinding.CustomToolbarBinding
import com.kerry.ubiquitiassignment.utils.showKeyboard

class CustomToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : MotionLayout(context, attrs, defStyleAttr) {

    private val binding = CustomToolbarBinding.bind(
        inflate(context, R.layout.custom_toolbar, this@CustomToolbar)
    )

    var onSearchClick: () -> Unit = { }
    var onBackClick: () -> Unit = { }
    var onEditTextChanged: (String) -> Unit = { }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, defStyleAttr, 0)
        binding.ivBack.setAttributeDrawable(ta, R.styleable.CustomToolbar_backIconRes)
        binding.ivSearch.setAttributeDrawable(ta, R.styleable.CustomToolbar_searchIconRes)
        ta.getDrawable(R.styleable.CustomToolbar_editTextCursorBackground)?.let { drawable ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.editKeyword.textCursorDrawable = drawable
            }
        }
        ta.getString(R.styleable.CustomToolbar_toolbarTitle)?.let { title ->
            binding.tvToolbarTitle.text = title
        }
        ta.recycle()
        setupClickEvent()
        binding.editKeyword.addTextChangedListener {
            onEditTextChanged.invoke(it.toString())
        }

        this.setTransitionListener(object : TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float,
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when (currentId) {
                    R.id.toolbar_normal_mode -> {
                        binding.editKeyword.isEnabled = false
                        onBackClick.invoke()
                        binding.editKeyword.setText("")
                    }
                    R.id.toolbar_search_mode -> {
                        binding.editKeyword.isEnabled = true
                        binding.editKeyword.showKeyboard()
                        onSearchClick.invoke()
                    }
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float,
            ) {
            }
        })
    }

    private fun setupClickEvent() {
        binding.ivBack.setOnClickListener {
            this.transitionToStart()
        }
        binding.ivSearch.setOnClickListener {
            this.transitionToEnd()
        }
    }

    private fun View.setAttributeDrawable(ta: TypedArray, @StyleableRes attrs: Int) {
        ta.getDrawable(attrs)?.let { drawable -> this.background = drawable }
    }

    fun navigateSearchMode() {
        this.transitionToEnd()
    }

    fun navigateNormalMode() {
        this.transitionToStart()
    }

}