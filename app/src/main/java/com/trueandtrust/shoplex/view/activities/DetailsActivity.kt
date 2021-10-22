package com.trueandtrust.shoplex.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ActivityDetailsBinding
import com.trueandtrust.shoplex.model.adapter.PagerAdapter
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.pojo.Product

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        if(StoreInfo.lang != this.resources.configuration.locale.language)
            StoreInfo.setLocale(StoreInfo.lang, this)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        product = this.intent.getParcelableExtra(getString(R.string.PRODUCT_KEY))!!

        val pagerAdapter = PagerAdapter(supportFragmentManager,this)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
                title = tab.text
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        title = binding.viewPager.adapter!!.getPageTitle(0)
    }
}