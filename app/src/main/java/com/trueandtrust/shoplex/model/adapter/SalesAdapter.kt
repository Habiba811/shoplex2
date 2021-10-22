package com.trueandtrust.shoplex.model.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.trueandtrust.shoplex.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.trueandtrust.shoplex.model.enumurations.DiscountType
import com.trueandtrust.shoplex.databinding.SaleProductItemRowBinding
import com.trueandtrust.shoplex.model.extra.FirebaseReferences
import com.trueandtrust.shoplex.model.pojo.Product
import com.trueandtrust.shoplex.model.pojo.SpecialDiscount
import com.trueandtrust.shoplex.view.activities.MessageActivity

class SalesAdapter(private val salesProduct: ArrayList<Product>, val userID: String) :
    RecyclerView.Adapter<SalesAdapter.SalesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        return SalesViewHolder(
            SaleProductItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) =
        holder.bind(salesProduct[position])

    override fun getItemCount() = salesProduct.size

    inner class SalesViewHolder(val binding: SaleProductItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            Glide.with(itemView.context).load(product.images.firstOrNull())
                .error(R.drawable.init_img).into(binding.imgSaleProduct)
            binding.tvSalesProductName.text = product.name
            binding.tvProductCategory.text = product.category
            binding.tvSalesPrice.text = product.price.toString()
            binding.tvProductPrice.text = product.price.toString()
            val arrayDiscountTypeAdapter = ArrayAdapter(
                binding.root.context, R.layout.dropdown_discount_type_item,
                DiscountType.values()
            )
            binding.actDiscountType.setText(DiscountType.Percentage.name)
            binding.actDiscountType.setAdapter(arrayDiscountTypeAdapter)
            binding.actDiscountType.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    if (position == 0) {
                        binding.tiSaleDiscount.helperText = "0-90%"
                    } else {
                        binding.tiSaleDiscount.helperText = "0-100"

                    }
                }

            binding.btnConfirmSales.setOnClickListener {
                if(binding.edSaleDiscount.text.toString().isNotEmpty()) {
                    addDiscount(product)
                } else {

                }
            }

            binding.edSaleDiscount.doOnTextChanged { text, _, _, _ ->
                if(!text.isNullOrEmpty()){
                    val price = if(binding.actDiscountType.text.toString() == DiscountType.Percentage.name){
                        product.price - (product.price * (text.toString().toFloat()/100F))
                    } else {
                        product.price - text.toString().toInt()
                    }
                    binding.tvSalesPrice.text = price.toString()
                }
            }
        }

        private fun addDiscount(product: Product) {
            val discount = binding.edSaleDiscount.text.toString().toFloat()
            val discountType = DiscountType.valueOf(binding.actDiscountType.text.toString())
            val specialDiscount = SpecialDiscount(discount, discountType)
            FirebaseReferences.productsRef.document(product.productID)
                .collection("Special Discounts")
                .document(userID)
                .set(specialDiscount).addOnSuccessListener {
                    val snackbar = Snackbar.make(
                        binding.root,
                        binding.root.context.getString(R.string.discountAdded),
                        Snackbar.LENGTH_LONG
                    )
                    val sbView: View = snackbar.view
                    sbView.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.blueshop
                        )
                    )
                    snackbar.show()
                    (binding.root.context as MessageActivity).salesBtnSheetDialog.dismiss()
                }.addOnFailureListener {
                    val snackbar = Snackbar.make(
                        binding.root,
                        binding.root.context.getString(R.string.discountFailed),
                        Snackbar.LENGTH_LONG
                    )
                    val sbView: View = snackbar.view
                    sbView.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.blueshop
                        )
                    )
                    snackbar.show()
                }
        }
    }
}