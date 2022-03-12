package dev.forcetower.events.widget.error

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import dev.forcetower.events.R
import dev.forcetower.events.databinding.WidgetErrorViewBinding

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = WidgetErrorViewBinding.inflate(LayoutInflater.from(context), this, true)

    var errorTitle: String = ""
        set(value) {
            binding.title.text = value
            field = value
        }

    var errorSubtitle: String = ""
        set(value) {
            binding.subtitle.text = value
            field = value
        }

    var errorButtonText: String = ""
        set(value) {
            binding.btnTryAgain.text = value
            field = value
        }

    var errorOnTryAgain: OnClickListener? = null
        set(value) {
            binding.btnTryAgain.setOnClickListener(value)
            field = value
        }

    init {
        val attributes = context.obtainStyledAttributes(
            attrs,
            R.styleable.ErrorView,
            defStyleAttr,
            0
        )

        errorTitle = attributes.getString(R.styleable.ErrorView_errorTitle) ?: "Oops!"
        errorSubtitle = attributes.getString(R.styleable.ErrorView_errorSubtitle) ?: "Something went wrong"
        errorButtonText = attributes.getString(R.styleable.ErrorView_errorButtonText) ?: "Try again"

        attributes.recycle()
    }
}
