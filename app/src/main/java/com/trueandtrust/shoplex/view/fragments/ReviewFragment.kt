package com.trueandtrust.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.trueandtrust.shoplex.databinding.FragmentReviewBinding
import com.trueandtrust.shoplex.model.adapter.ReviewsAdapter
import com.trueandtrust.shoplex.view.activities.DetailsActivity
import com.trueandtrust.shoplex.viewmodel.ProductsVM

class ReviewFragment : Fragment() {
    private lateinit var binding: FragmentReviewBinding
    private lateinit var productsVm: ProductsVM
    private lateinit var productID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentReviewBinding.inflate(inflater, container, false)
        productsVm = ViewModelProvider(requireActivity()).get(ProductsVM::class.java)

        productID = (requireActivity() as DetailsActivity).product.productID

        if (productsVm.reviews.value == null)
            productsVm.getReviewsByProductId(productID)
        productsVm.reviews.observe(viewLifecycleOwner, { reviews ->
            binding.rvReview.adapter = ReviewsAdapter(reviews)

        })

        productsVm.reviewStatistics.observe(viewLifecycleOwner, {
            binding.reviewStat = it
            if (it.total != 0) {
                binding.fiveStars.progress = ((it.fiveStars.toFloat() / it.total) * 100).toInt()
                binding.fourStars.progress = ((it.fourStars.toFloat() / it.total) * 100).toInt()
                binding.threeStars.progress = ((it.threeStars.toFloat() / it.total) * 100).toInt()
                binding.twoStars.progress = ((it.twoStars.toFloat() / it.total) * 100).toInt()
                binding.oneStar.progress = ((it.oneStar.toFloat() / it.total) * 100).toInt()
            }
        })
        return binding.root
    }
}