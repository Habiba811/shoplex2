package com.trueandtrust.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.FragmentHomeBinding
import com.trueandtrust.shoplex.model.adapter.OrdersAdapter
import com.trueandtrust.shoplex.viewmodel.OrdersFactory
import com.trueandtrust.shoplex.viewmodel.OrdersVM
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var ordersVm: OrdersVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        ordersVm = ViewModelProvider(requireActivity(), OrdersFactory(requireContext())).get(OrdersVM::class.java)

        if (ordersVm.order.value == null)
            ordersVm.getCurrentOrders()

        requireActivity().title = getString(R.string.home)

        ordersVm.order.observe(viewLifecycleOwner, { orders ->
            if (orders.count()>0) {
                binding.noItem.visibility=View.INVISIBLE
            }
            else{
                binding.noItem.visibility=View.VISIBLE
            }
            //binding.rvHome.adapter = OrdersAdapter(orders)
            binding.rvHome.adapter = ScaleInAnimationAdapter(SlideInBottomAnimationAdapter( OrdersAdapter(orders))).apply {
                setDuration(700)
                setInterpolator(OvershootInterpolator(2f))
            }
        })

        return binding.root
    }
}