package com.trueandtrust.shoplex.model.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.model.interfaces.AddProductListener

class MyImagesAdapter(private var myImages: ArrayList<Uri>, private var notifyBy: AddProductListener) :
    RecyclerView.Adapter<MyImagesAdapter.ImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_upload_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) =
        holder.bind(myImages[position])

    override fun getItemCount() = myImages.size

    inner class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(imageURI: Uri) {
            val btnCancel = itemView.findViewById<ImageButton>(R.id.imgButtonCancel)

            btnCancel.setOnClickListener {
                notifyBy.onImageRemoved(bindingAdapterPosition)
            }

            Glide.with(itemView.context).load(imageURI).into(itemView.findViewById(R.id.uploadedImage))
        }
    }
}