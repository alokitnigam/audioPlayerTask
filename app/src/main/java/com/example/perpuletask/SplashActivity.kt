package com.example.perpuletask

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity(), AnimationListener {
    var animZoomIn: Animation? = null
    var logoimage: ImageView? = null
    var rl: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        logoimage = findViewById(R.id.logoimage)
        rl = findViewById(R.id.rl)
        animZoomIn = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.zoom_in
        )
        applyZoomEffect(rl)
    }

    fun applyZoomEffect(view: View?) {
        val DEFAULT_ANIMATION_DURATION: Long = 1000
        val valueAnimator = ValueAnimator.ofFloat(1f, 1.5f)
        valueAnimator.addUpdateListener { animation ->
            //3
            val value = animation.animatedValue as Float
            //4
            view!!.scaleX = value
            view.scaleY = value
        }
        valueAnimator.duration = DEFAULT_ANIMATION_DURATION
        val valueAnimator2 = ValueAnimator.ofFloat(1.5f, 0f)
        valueAnimator2.addUpdateListener { animation ->
            //3
            val value = animation.animatedValue as Float
            //4
            view!!.scaleX = value
            view.scaleY = value
        }
        valueAnimator2.duration = 500
        val set = AnimatorSet()
        set.play(valueAnimator).before(valueAnimator2)
        set.interpolator = LinearInterpolator()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) { /*
                 * check if the activity was destroyed
                 */
/*
                     *containing the intent to start discover tab
                     * might have started from oneSignal notification
                     */
                val i = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        set.start()
    }

    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {
        if (animation === animZoomIn) {
        }
    }

    override fun onAnimationRepeat(animation: Animation) {}
}