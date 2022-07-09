@file:Suppress("NOTHING_TO_INLINE")

package com.geektechkb.core.ui.customViews

import android.animation.Animator
import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.VibratorManager
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import java.util.*

fun Context.dip(value: Float): Float = (value * resources.displayMetrics.density)

fun Context.dipInt(value: Float): Int = dip(value).toInt()

inline fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(this, text, duration).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}

fun Context.vibrate(pattern: LongArray) {
    if (Build.VERSION.SDK_INT >= 26) {
        (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).vibrate(
            CombinedVibration.createParallel(VibrationEffect.createWaveform(pattern, -1))
        )
    } else {
        (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).vibrate(
            CombinedVibration.createParallel(VibrationEffect.createWaveform(pattern, -1))
        )

    }
}

fun Long.formatMillis(): String {
    val formatBuilder = StringBuilder()
    val formatter = Formatter(formatBuilder, Locale.getDefault())
    getStringForTime(formatBuilder, formatter, this)
    return formatBuilder.toString()
}


fun getStringForTime(builder: StringBuilder, formatter: Formatter, timeMs: Long): String {
    val totalSeconds = (timeMs + 500) / 1000
    val seconds = totalSeconds % 60
    val minutes = totalSeconds / 60 % 60
    val hours = totalSeconds / 3600
    builder.setLength(0)
    return if (hours > 0)
        formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
    else
        formatter.format("%02d:%02d", minutes, seconds).toString()
}


fun Animator.doOnEnd(action: (animator: Animator) -> Unit) = addListener(onEnd = action)


fun Animator.addListener(
    onEnd: ((animator: Animator) -> Unit)? = null,
    onStart: ((animator: Animator) -> Unit)? = null,
    onCancel: ((animator: Animator) -> Unit)? = null,
    onRepeat: ((animator: Animator) -> Unit)? = null
): Animator.AnimatorListener {
    val listener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animator: Animator) {
            onRepeat?.invoke(animator)
        }

        override fun onAnimationEnd(animator: Animator) {
            onEnd?.invoke(animator)
        }

        override fun onAnimationCancel(animator: Animator) {
            onCancel?.invoke(animator)
        }

        override fun onAnimationStart(animator: Animator) {
            onStart?.invoke(animator)
        }
    }
    addListener(listener)
    return listener
}

fun View.fadeIn(duration: Long) {
    this.visibility = View.VISIBLE
    this.alpha = 0f
    ViewCompat.animate(this).alpha(1f).setDuration(duration)
        .setListener(object : ViewPropertyAnimatorListener {
            override fun onAnimationStart(view: View) {
            }

            override fun onAnimationEnd(view: View) {
            }

            override fun onAnimationCancel(view: View) {}
        }).start()
}

fun View.fadeOut(duration: Long, delay: Long = 0) {
    this.alpha = 1f
    ViewCompat.animate(this).alpha(0f).setStartDelay(delay).setDuration(duration)
        .setListener(object : ViewPropertyAnimatorListener {
            override fun onAnimationStart(view: View) {
                view.isDrawingCacheEnabled = true
            }

            override fun onAnimationEnd(view: View) {
                view.visibility = View.INVISIBLE
                view.alpha = 0f
                @Suppress("DEPRECATION")
                view.isDrawingCacheEnabled = false
            }

            override fun onAnimationCancel(view: View) {}
        })
}
