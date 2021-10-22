package com.trueandtrust.shoplex.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.LocationItemRowBinding
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.pojo.PendingLocation
import com.trueandtrust.shoplex.viewmodel.StoreVM

class LocationAdapter(val locationsList: ArrayList<PendingLocation>) :
    RecyclerView.Adapter<LocationAdapter.LocationsViewHolder>() {

    private lateinit var storeVM: StoreVM
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        context = parent.context
        storeVM = ViewModelProvider(context as AppCompatActivity).get(StoreVM::class.java)
        return LocationsViewHolder(
            LocationItemRowBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) =
        holder.bind(locationsList[position])

    override fun getItemCount() = locationsList.size

    inner class LocationsViewHolder(val binding: LocationItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: PendingLocation) {
            binding.tvLocation.text = location.address

            binding.btnColse.setOnClickListener {
                if (locationsList.count() > 1) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(context.getString(R.string.delete))
                    builder.setMessage(context.getString(R.string.deleteMessage))

                    builder.setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                        val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.deleteSuccess), Snackbar.LENGTH_LONG)
                        val sbView: View = snackbar.view
                        sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                        snackbar.show()

                        storeVM.removeLocationAddress(location)
                        storeVM.isLocationRemoved.observe(
                            it.context as AppCompatActivity,
                            { isRemoved ->
                                if (isRemoved == true) {
                                    StoreInfo.saveStoreInfo(context)
                                    locationsList.remove(location)
                                    notifyItemRemoved(bindingAdapterPosition)
                                }
                            })

                    }

                    builder.setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
                        dialog.cancel()
                    }

                    builder.show()
                } else {
                    val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.onelocation), Snackbar.LENGTH_LONG)
                    val sbView: View = snackbar.view
                    sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                    snackbar.show()

                }
            }
        }
    }
}