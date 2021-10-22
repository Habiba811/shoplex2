package com.trueandtrust.shoplex.view.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ActivitySplashBinding
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.interfaces.AddProductListener
import com.trueandtrust.shoplex.view.activities.auth.AuthActivity
import java.util.*

class SplashActivity : AppCompatActivity(), AddProductListener {
    private val splashDuration = 4000L
    private lateinit var binding: ActivitySplashBinding
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StoreInfo.readStoreInfo(applicationContext)

        if(StoreInfo.lang != this.resources.configuration.locale.language)
            setLocale(StoreInfo.lang)

        currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            currentUser!!.reload()
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        binding.imgSplash.animation = topAnimation
        binding.tvShoplexSplash.animation = bottomAnimation

        Handler().postDelayed({
            currentUser = Firebase.auth.currentUser

            if (currentUser != null) {
                if (StoreInfo.storeID != null) {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    StoreInfo.saveToRecentVisits()
                } else {
                    //AuthVM.logout(this)
                    startActivity(Intent(applicationContext, AuthActivity::class.java))
                }
            } else {
                //AuthVM.logout(this)
                startActivity(Intent(applicationContext, AuthActivity::class.java))
            }
            finish()

        }, splashDuration)
    }

    fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.createConfigurationContext(config)
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }
}