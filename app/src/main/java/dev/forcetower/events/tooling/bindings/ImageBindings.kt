package dev.forcetower.events.tooling.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter(
    value = [
        "imageUrl",
        "fallbackResource",
        "crossFade",
    ],
    requireAll = false
)
fun ImageView.imageUrl(
    imageUrl: String?,
    fallbackResource: Int?,
    crossFade: Boolean?,
) {
    val load = imageUrl ?: fallbackResource
    var request = Glide.with(this).load(load)
    if (fallbackResource != null) request = request.error(fallbackResource)
    if (crossFade == true) request = request.transition(DrawableTransitionOptions.withCrossFade())
    request.into(this)
}
