package com.trueandtrust.shoplex.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trueandtrust.shoplex.R
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.trueandtrust.shoplex.databinding.PropertyItemRowBinding
import com.trueandtrust.shoplex.model.pojo.Property

class PropertyAdapter(val properties: ArrayList<Property>, val isViewOnly: Boolean = false) :
    RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        this.context = parent.context

        return PropertyViewHolder(
            PropertyItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) =
        holder.bind(properties[position])

    override fun getItemCount() = properties.size

    inner class PropertyViewHolder(val binding: PropertyItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            binding.tvPropName.text = property.name

            for (value in property.values) {
                val chipItem: Chip

                if(isViewOnly) {
                    chipItem = LayoutInflater.from(binding.root.context).inflate(R.layout.chip_choice_item, null, false) as Chip
                    binding.btnDelteProperty.visibility = View.GONE
                }
                else {
                    chipItem = LayoutInflater.from(binding.root.context).inflate(R.layout.chip_entry_item, null, false) as Chip
                    binding.btnDelteProperty.setOnClickListener {
                        properties.remove(property)
                        notifyItemRemoved(bindingAdapterPosition)
                    }
                }

                chipItem.text = value
                chipItem.setOnCloseIconClickListener {
                    binding.cgPropValue.removeView(it)
                    properties[bindingAdapterPosition].values.remove(value)
                    if (property.values.count() == 0) {
                        properties.remove(property)
                        notifyItemRemoved(bindingAdapterPosition)
                    }
                }

                binding.cgPropValue.addView(chipItem)
            }
        }
    }
}