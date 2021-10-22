package com.trueandtrust.shoplex.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ReveiwItemBinding
import com.trueandtrust.shoplex.model.pojo.Review

class ReviewsAdapter(val reviews: ArrayList<Review>) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            ReveiwItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) =
        holder.bind(reviews[position])

    override fun getItemCount() = reviews.size

    inner class ReviewViewHolder(val binding: ReveiwItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            Glide.with(binding.root.context).load(review.image).error(R.drawable.init_img).into(binding.imgHead)
            binding.review = review
        }
    }
}
