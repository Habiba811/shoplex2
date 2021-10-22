package com.trueandtrust.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.trueandtrust.shoplex.databinding.FragmentProductBinding
import com.trueandtrust.shoplex.model.adapter.PropertyAdapter
import com.trueandtrust.shoplex.model.pojo.Product
import com.trueandtrust.shoplex.view.activities.DetailsActivity

class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)

        product = (requireActivity() as DetailsActivity).product
        val imageList = ArrayList<SlideModel>()
        for (imgURL in product.images)
            imageList.add(SlideModel(imgURL))

        binding.imgSliderDetails.setImageList(imageList, ScaleTypes.CENTER_INSIDE)

        binding.product = product

        if(!product.properties.isNullOrEmpty())
            binding.textView4.visibility = View.VISIBLE

        // Properties Adapter
        binding.rcProperties.layoutManager = LinearLayoutManager(context)
        binding.rcProperties.adapter = PropertyAdapter(product.properties, true)

        return binding.root
    }
}