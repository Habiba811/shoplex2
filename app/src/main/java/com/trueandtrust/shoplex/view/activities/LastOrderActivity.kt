package com.trueandtrust.shoplex.view.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ActivityLastOrderBinding
import com.trueandtrust.shoplex.model.adapter.OrdersAdapter
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.room.viewModel.LastOrderViewModel
import com.trueandtrust.shoplex.viewmodel.OrdersFactory
import com.trueandtrust.shoplex.viewmodel.OrdersVM
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter

class LastOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLastOrderBinding
    private lateinit var ordersVm: OrdersVM
  //  private lateinit var lastOrderVM : LastOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        if(StoreInfo.lang != this.resources.configuration.locale.language)
            StoreInfo.setLocale(StoreInfo.lang, this)
        super.onCreate(savedInstanceState)
        binding = ActivityLastOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.ordersVm = ViewModelProvider(this, OrdersFactory(this)).get(OrdersVM::class.java)
       // lastOrderVM = ViewModelProvider(this).get(LastOrderViewModel::class.java)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.LastOrder)

        if (ordersVm.lastOrders.value.isNullOrEmpty())
            ordersVm.getLastOrders()

        ordersVm.lastOrders.observe(this, { orders ->
            if (orders.count()>0) {
                binding.noItem.visibility= View.INVISIBLE
            }
            else{
                binding.noItem.visibility= View.VISIBLE
            }
            binding.rvLastOrders.adapter = ScaleInAnimationAdapter(SlideInBottomAnimationAdapter(OrdersAdapter(orders))).apply {
                setDuration(700)
                setInterpolator(OvershootInterpolator(2f))
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()

        return super.onOptionsItemSelected(item)
    }
}