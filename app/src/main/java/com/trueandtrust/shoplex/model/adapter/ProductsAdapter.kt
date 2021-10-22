package com.trueandtrust.shoplex.model.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ProductGvBinding
import com.trueandtrust.shoplex.model.firebase.ProductsDBModel
import com.trueandtrust.shoplex.model.interfaces.ProductsListener
import com.trueandtrust.shoplex.model.pojo.Product
import com.trueandtrust.shoplex.view.activities.AddProductActivity
import com.trueandtrust.shoplex.view.activities.DetailsActivity

class ProductsAdapter(var products: ArrayList<Product>) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        context = parent.context
        return ProductViewHolder(
            ProductGvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(products[position])

    override fun getItemCount() = products.size

    inner class ProductViewHolder(val binding: ProductGvBinding) :
        RecyclerView.ViewHolder(binding.root), ProductsListener {
        fun bind(product: Product) {

            Glide.with(itemView.context).load(product.images.firstOrNull())
                .error(R.drawable.init_img).into(binding.imgProduct)
            binding.product = product

            binding.fabEditProduct.setOnClickListener {
                context.startActivity(Intent(context, AddProductActivity::class.java).apply {
                    this.putExtra(context.getString(R.string.PRODUCT_KEY), product)
                })
            }

            binding.fabDeleteProduct.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(context.getString(R.string.delete))
                builder.setMessage(context.getString(R.string.deleteMessage))

                builder.setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                    ProductsDBModel(this).deleteProduct(product.productID)
                    if (products.count() == 1) {
                        products.removeAt(bindingAdapterPosition)
                        notifyItemRemoved(bindingAdapterPosition)
                    }
                    val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.deleteSuccess), Snackbar.LENGTH_LONG)
                    val sbView: View = snackbar.view
                    sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                    snackbar.show()
                }

                builder.setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }

                builder.show()
            }

            itemView.setOnClickListener {
                context.startActivity(Intent(context, DetailsActivity::class.java).apply {
                    this.putExtra(context.getString(R.string.PRODUCT_KEY), product)
                })
            }
        }

        override fun onProductRemoved() {
//            products.removeAt(bindingAdapterPosition)
  //          notifyItemRemoved(bindingAdapterPosition)
        }
    }
}