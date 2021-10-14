package uz.smd.floodfillalgorithmssmartstaffuz.utils

import android.content.Context
import android.view.animation.Animation.AnimationListener

import android.graphics.Bitmap
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView


/**
 * Created by Siddikov Mukhriddin on 10/14/21
 */
fun ImageView.setBitmapAnim( new_image: Bitmap?,anim_duration:Long) {
    val anim_out: Animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    val anim_in: Animation = AnimationUtils.loadAnimation(context,android.R.anim.fade_in)
    anim_out.duration=anim_duration
    anim_in.duration=anim_duration
    anim_out.setAnimationListener(object : AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {
            setImageBitmap(new_image)
            anim_in.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {}
            })
            startAnimation(anim_in)
        }
    })
    startAnimation(anim_out)
}