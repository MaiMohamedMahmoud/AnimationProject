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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ObjectAnimator.ofArgb
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator.ofArgb
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView


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
                view.isEnabled = false
            }

            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = true
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

    private fun shower() {
    }

}
