/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.animation.ObjectAnimator.ofArgb
import android.animation.ValueAnimator.ofArgb
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotate()
        }

        translateButton.setOnClickListener {
            translate()
        }

        scaleButton.setOnClickListener {
            scale()
        }

        fadeButton.setOnClickListener {
            fade()
        }

        colorizeButton.setOnClickListener {
            colorize()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    private fun disableViewWhileAnimation(view: View, animator: ObjectAnimator) {
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }

            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }
        })
    }

    private fun rotate() {
        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)
        animator.duration = 1000
        disableViewWhileAnimation(rotateButton, animator)
        animator.start()
    }

    private fun translate() {
        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        disableViewWhileAnimation(translateButton, animator)
        animator.start()
    }

    private fun scale() {

        /**
         * PropertyValuesHolder...
         * only hold the property and value information for the animation, not the target.
         * So even if you wanted to run these as an animation, you could not;
         * you haven't told the system which target object(s) to animate.
         */
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)
        disableViewWhileAnimation(scaleButton, animator)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    private fun fade() {
        val fade = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(star, fade)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        disableViewWhileAnimation(fadeButton, animator)
        animator.start()
    }

    private fun colorize() {
        val animator = ObjectAnimator.ofArgb(
            star.parent,
            "backgroundColor", Color.BLACK, Color.RED
        )
        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        disableViewWhileAnimation(colorizeButton, animator)
        animator.start()
    }

    private fun createNewStar(): AppCompatImageView {
        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        return newStar
    }

    private fun createStarSizeAndPosition(
        newStar: AppCompatImageView,
        container: ViewGroup
    ): Pair<Float, Float> {
        var starW = star.width.toFloat()
        var starH = star.height.toFloat()
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY
        //star position
        newStar.translationX = Math.random().toFloat() *
                container.width - starW / 2

        return Pair(starW, starH)
    }

    private fun createAnimationFall(
        newStar: AppCompatImageView,
        starH: Float,
        container: ViewGroup
    ): Animator {
        //create animation falling for the star
        val mover = ObjectAnimator.ofFloat(
            newStar, View.TRANSLATION_Y,
            -starH, container.height + starH
        )
        mover.interpolator = AccelerateInterpolator(1f)
        return mover
    }

    private fun createAnimationRotate(newStar: AppCompatImageView): Animator {
        //create animation rotate for the star
        val rotator = ObjectAnimator.ofFloat(
            newStar, View.ROTATION,
            (Math.random() * 1080).toFloat()
        )
        rotator.interpolator = LinearInterpolator()
        return rotator
    }

    private fun shower() {
        val container = star.parent as ViewGroup
        //create new star.
        val newStar = createNewStar()
        //add the created star to the container
        container.addView(newStar)
        //create star size and position.
        val (starW, starH) = createStarSizeAndPosition(newStar, container)
        //create falling animation for the star
        val mover = createAnimationFall(newStar, starH, container)
        //create rotator animation for the star
        val rotator = createAnimationRotate(newStar)

        //create multiple animation in parallel like rotate and move ..
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })
        set.start()
    }

}
