package com.geektechkb.geekmessenger.presentation.ui.activity

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.geektechkb.core.utils.AppTheme
import com.geektechkb.core.utils.Coordinate
import com.geektechkb.core.utils.SimpleImageView
import com.geektechkb.core.utils.ThemeAnimationListener
import com.geektechkb.feature_main.databinding.NavHeaderMainBinding
import com.geektechkb.feature_main.presentation.ui.fragments.MainFlowFragment
import com.geektechkb.feature_main.themes.LightTheme
import com.geektechkb.feature_main.themes.MyAppTheme
import com.geektechkb.feature_main.themes.NightTheme
import com.geektechkb.geekmessenger.R
import kotlin.math.sqrt

abstract class BaseActivity : AppCompatActivity(){

    private lateinit var root: View
    private lateinit var frontFakeThemeImageView: SimpleImageView
    private lateinit var behindFakeThemeImageView: SimpleImageView
    private lateinit var decorView: FrameLayout
    private lateinit var binding: NavHeaderMainBinding
    private lateinit var Button : MainFlowFragment

    private var dayToggleButton =
        findViewById<ImageView>(com.geektechkb.feature_main.R.id.dayToggleButton)

    private var anim: Animator? = null
    private var themeAnimationListener: ThemeAnimationListener? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        MainActivity.instance.init(this, getStartTheme())
        initViews()
        super.setContentView(root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.instance.init(this, getStartTheme())
        initViews()
        super.setContentView(root)

        binding = NavHeaderMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

            updateButtonText()
            binding.dayToggleButton.setOnClickListener {
                if (MainActivity.instance.getCurrentTheme()
                        ?.id() == NightTheme.ThemeId
                ) {
                    MainActivity.instance.reverseChangeTheme(LightTheme(), it)
                } else if (MainActivity.instance.getCurrentTheme()
                        ?.id() != NightTheme.ThemeId
                ) {
                    MainActivity.instance.changeTheme(NightTheme(), it)
                }
                updateButtonText()
            }
    }

    // to sync ui with selected theme
    fun syncTheme(appTheme: AppTheme) {
        // change ui colors with new appThem here
        val myAppTheme = appTheme as MyAppTheme
        // set background color
        binding.dayToggleButton.setBackgroundColor(myAppTheme.activityBackgroundColor(this))
    }

    fun updateButtonText() {
        when (MainActivity.instance.getCurrentTheme()?.id()) {
            NightTheme.ThemeId -> binding.dayToggleButton.setImageResource(R.drawable.night)
            else -> binding.dayToggleButton.setImageResource(R.drawable.day)
        }
    }

 fun getStartTheme(): AppTheme {
        return LightTheme()
    }

    override fun onResume() {
        super.onResume()
        MainActivity.instance.setActivity(this)
        getThemeManager().getCurrentTheme()?.let { syncTheme(it) }
    }

   fun getThemeManager(): MainActivity {
        return MainActivity.instance
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, decorView, false))
    }

    override fun setContentView(view: View?) {
        decorView.removeAllViews()
        decorView.addView(view)
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        decorView.removeAllViews()
        decorView.addView(view, params)
    }

    private fun initViews() {
        // create roo view
        root = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )

            // create and add behindFakeThemeImageView
            addView(SimpleImageView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                visibility = View.GONE
                behindFakeThemeImageView = this
            })

            // create and add decorView, ROOT_ID is generated ID
            addView(FrameLayout(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                decorView = this
                id = ROOT_ID
            })

            // create and add frontFakeThemeImageView
            addView(SimpleImageView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                visibility = View.GONE
                frontFakeThemeImageView = this
            })
        }
    }

    fun changeTheme(
        newTheme: AppTheme,
        sourceCoordinate: Coordinate,
        animDuration: Long,
        isReverse: Boolean
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // update theme and return: no animation
            syncTheme(newTheme)
            return
        }

        if (frontFakeThemeImageView.visibility == View.VISIBLE ||
            behindFakeThemeImageView.visibility == View.VISIBLE ||
            isRunningChangeThemeAnimation()
        ) {
            return
        }

        // take screenshot
        val w = decorView.measuredWidth
        val h = decorView.measuredHeight
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        decorView.draw(canvas)

        // update theme
        syncTheme(newTheme)

        //create anim
        val finalRadius = sqrt((w * w + h * h).toDouble()).toFloat()
        if (isReverse) {
            frontFakeThemeImageView.bitmap = bitmap
            frontFakeThemeImageView.visibility = View.VISIBLE
            anim = ViewAnimationUtils.createCircularReveal(
                frontFakeThemeImageView,
                sourceCoordinate.x,
                sourceCoordinate.y,
                finalRadius,
                0f
            )
        } else {
            behindFakeThemeImageView.bitmap = bitmap
            behindFakeThemeImageView.visibility = View.VISIBLE
            anim = ViewAnimationUtils.createCircularReveal(
                decorView,
                sourceCoordinate.x,
                sourceCoordinate.y,
                0f,
                finalRadius
            )
        }

        // set duration
        anim?.duration = animDuration

        // set listener
        anim?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                themeAnimationListener?.onAnimationStart(animation)
            }

            override fun onAnimationEnd(animation: Animator) {
                behindFakeThemeImageView.bitmap = null
                frontFakeThemeImageView.bitmap = null
                frontFakeThemeImageView.visibility = View.GONE
                behindFakeThemeImageView.visibility = View.GONE
                themeAnimationListener?.onAnimationEnd(animation)
            }

            override fun onAnimationCancel(animation: Animator) {
                themeAnimationListener?.onAnimationCancel(animation)
            }

            override fun onAnimationRepeat(animation: Animator) {
                themeAnimationListener?.onAnimationRepeat(animation)
            }
        })

        anim?.start()
    }

    fun isRunningChangeThemeAnimation(): Boolean {
        return anim?.isRunning == true
    }

        companion object {
        //generated Id for decorView
        internal val ROOT_ID = ViewCompat.generateViewId()
    }
}