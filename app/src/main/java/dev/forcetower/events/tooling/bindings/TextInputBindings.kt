package dev.forcetower.events.tooling.bindings

import android.content.res.ColorStateList
import androidx.annotation.StringRes
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.forcetower.events.tooling.extensions.resolveColorAttr

@BindingAdapter("textInputError")
fun TextInputEditText.textInputError(@StringRes errorStr: Int?) {
    if (errorStr == null) {
        error = null
    } else {
        error = context.getString(errorStr)
        requestFocus()
    }
}

@BindingAdapter("textHelperError")
fun TextInputLayout.textHelperError(@StringRes error: Int?) {
    if (error == null) {
        helperText = null
    } else {
        helperText = context.getString(error)
        get(0).requestFocus()
    }
    setHelperTextColor(ColorStateList.valueOf(context.resolveColorAttr(com.google.android.material.R.attr.colorError)))
}
