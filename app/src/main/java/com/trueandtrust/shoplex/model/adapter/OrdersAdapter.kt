package com.trueandtrust.shoplex.model.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.trueandtrust.shoplex.model.enumurations.OrderStatus
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.HomeRvBinding
import com.trueandtrust.shoplex.model.firebase.OrdersDBModel
import com.trueandtrust.shoplex.model.maps.LocationManager
import com.trueandtrust.shoplex.model.pojo.Order
import com.trueandtrust.shoplex.view.activities.DetailsActivity

class OrdersAdapter(val orders: ArrayList<Order>) :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        context = parent.context

        return OrdersViewHolder(
            HomeRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) = holder.bind(orders[position])

    override fun getItemCount() = orders.size

    inner class OrdersViewHolder(val binding: HomeRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {

            Glide.with(itemView.context).load(order.product!!.images.firstOrNull()).error(R.drawable.init_img).into(binding.imgProduct)
            binding.order = order
            val properites = arrayListOf<String>()
            if (order.orderProperties != null){
                for (prop in order.orderProperties){
                   properites.add(prop)
                }
                binding.tvProp.text = properites.joinToString()
            }
            if (order.orderStatus == OrderStatus.Current) {
                binding.btnDeliveryOrder.setOnClickListener {
                    OrdersDBModel.deliverOrder(order.orderID.toString())
                    orders.removeAt(bindingAdapterPosition)
                    notifyItemRemoved(bindingAdapterPosition)
                }
                binding.btnShowRoute.visibility = View.VISIBLE
            } else {
                binding.btnDeliveryOrder.visibility = View.INVISIBLE
            }

            binding.btnShowRoute.setOnClickListener {
                if(order.deliveryLoc != null) {
                    LocationManager.getInstance(context).launchGoogleMaps(order.deliveryLoc!!)
                }else{
                    val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.customerlocation), Snackbar.LENGTH_LONG)
                    val sbView: View = snackbar.view
                    sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                    snackbar.show()
                }
            }

            itemView.setOnClickListener {
                context.startActivity(Intent(context, DetailsActivity::class.java).apply {
                    this.putExtra(context.getString(R.string.PRODUCT_KEY), order.product)
                })
            }
        }
    }
}
