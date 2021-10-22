package com.trueandtrust.shoplex.view.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.snackbar.Snackbar
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ActivityAddProductBinding
import com.trueandtrust.shoplex.model.adapter.MyImagesAdapter
import com.trueandtrust.shoplex.model.adapter.PropertyAdapter
import com.trueandtrust.shoplex.model.enumurations.*
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.interfaces.AddProductListener
import com.trueandtrust.shoplex.model.pojo.Product
import com.trueandtrust.shoplex.model.pojo.Property
import com.trueandtrust.shoplex.view.dialogs.PropertyDialog
import com.trueandtrust.shoplex.viewmodel.AddProductFactory
import com.trueandtrust.shoplex.viewmodel.AddProductVM
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import kotlinx.android.synthetic.main.product_gv.*
import java.util.*

class AddProductActivity : AppCompatActivity(), AddProductListener {
    private val OPEN_GALLERY_CODE = 200
    private val MAX_IMAGES_SIZE = 6
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var viewModel: AddProductVM
    private lateinit var product: Product
    private var isUpdate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if(StoreInfo.lang != this.resources.configuration.locale.language)
            StoreInfo.setLocale(StoreInfo.lang, this)
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define View Model
        viewModel = ViewModelProvider(this, AddProductFactory(this)).get(AddProductVM::class.java)

        viewModel.product.observe(this, {
            updateSliderUI()
            binding.imgSliderAddProduct.setImageList(
                viewModel.product.value!!.imageSlideList, ScaleTypes.CENTER_INSIDE
            )
        })

        viewModel.arrCategory.observe(this, {
            // Category Dropdown
            val arrayCategoryAdapter = ArrayAdapter(applicationContext, R.layout.dropdown_item, it)
            binding.actTVCategory.setAdapter(arrayCategoryAdapter)
        })

        viewModel.arrSubCategory.observe(this, {
            // SubCategory Dropdown
            val arraySubcategoryAdapter = ArrayAdapter(applicationContext, R.layout.dropdown_item, it)
            binding.actTVSubCategory.setAdapter(arraySubcategoryAdapter)
        })

        if(intent.hasExtra(getString(R.string.PRODUCT_KEY))){
            // User need to update data
            this.viewModel.product.value = intent.getParcelableExtra(getString(R.string.PRODUCT_KEY))
            this.product = this.viewModel.product.value!!
            onUpdate(product)
        }else{
            // Define product from view model
            product = viewModel.product.value!!
        }

        binding.product = product

//        product.name = binding.edProductName.text.toString()

        // Images Adapter
        val myAdapter = MyImagesAdapter(viewModel.product.value!!.imagesListURI, this)
        binding.rvUploadImages.adapter = myAdapter

        // Property Adapter
        //binding.rcProperty.adapter = PropertyAdapter(product.properties)
        binding.rcProperty.adapter = ScaleInAnimationAdapter(
            SlideInBottomAnimationAdapter(PropertyAdapter(product.properties))
        ).apply {
            setDuration(700)
            setInterpolator(OvershootInterpolator(1f))
        }

        // Category Dropdown
        viewModel.getCategory()

        settingUpButtons()

        settingUpEditTexts()
    }

    private fun settingUpButtons() {
        // AddImage Button
        binding.btnAddProductImages.setOnClickListener {
            if (product.imagesListURI.count() < MAX_IMAGES_SIZE) {
                openGalleryForImages()
            } else {
                val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.Max), Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                snackbar.show()

            }
        }

        //Open Dialog Button
        binding.btnAddProperty.setOnClickListener {
            val propertyDialog = PropertyDialog()
            propertyDialog.listener = this
            propertyDialog.show(supportFragmentManager, getString(R.string.propertyDialog))
        }

        // AddProduct Button
        binding.btnAddProduct.setOnClickListener {
            if (!validateInput())
                return@setOnClickListener

            val catIndex = viewModel.arrCategory.value!!.indexOf(binding.actTVCategory.text.toString())
            product.category = Category.values()[catIndex].name.replace("_", " ")
            val subIndex = viewModel.arrSubCategory.value!!.indexOf(binding.actTVSubCategory.text.toString())
            product.subCategory = viewModel.arrSubCats[subIndex] //binding.actTVSubCategory.text.toString()

            startActivity(Intent(this, ConfirmProductActivity::class.java).apply {
                this.putExtra(getString(R.string.PRODUCT_KEY), product)
                if (isUpdate)
                    this.putExtra(getString(R.string.update_product), isUpdate)
            })
        }
    }

    private fun settingUpEditTexts() {
        // Product Name
        binding.edProductName.addTextChangedListener {
            binding.tiProductName.error = null
        }

        // Description
        binding.edDescription.addTextChangedListener {
            binding.tiDescription.error = null
        }

        // Discount
        binding.edDiscountNum.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.edNewPrice.text = product.calculateNewPrice()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.edDiscountNum.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (binding.edDiscountNum.text.isNullOrEmpty())
                    binding.edDiscountNum.setText(getString(R.string.zero))
                if (binding.edDiscountNum.text.toString().toFloat() > 90) {
                    binding.edDiscountNum.setText(getString(R.string.maxDiscount))
                }
            }
        }

        // Old Price
        binding.edOldPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.edNewPrice.text = product.calculateNewPrice()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.edOldPrice.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (binding.edOldPrice.text.isNullOrEmpty() || binding.edOldPrice.text.toString()
                        .toFloat() < 10
                ) {
                    binding.edOldPrice.setText(getString(R.string.minPrice))
                }
            }
        }

        // Category dropdown
        binding.actTVCategory.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                binding.tiCategory.error = null
                binding.actTVSubCategory.text = null
                val selectedCategory =
                    Category.values()[position]// parent.getItemAtPosition(position).toString()

                viewModel.getSubCategory(selectedCategory)

                binding.actTVSubCategory.onItemClickListener =
                    OnItemClickListener { _, _, _, _ ->
                        binding.tiSubCategory.error = null
                    }
            }
    }

    private fun onUpdate(product: Product) {
        this.isUpdate = true
        product.imageSlideList.clear()
        product.imagesListURI.clear()
        for(imgURL in product.images){
            product.imageSlideList.add(SlideModel(imgURL))
            product.imagesListURI.add(Uri.parse(imgURL))
        }

        val catIndex = Category.values().map { it.name.replace("_", " ") }.indexOf(product.category)
        val category = resources.getStringArray(R.array.categories)[catIndex]// viewModel.arrCategory.value!![catIndex]
        binding.actTVCategory.setText(category)
        viewModel.getSubCategory(Category.values()[catIndex])

        viewModel.arrSubCategory.observe(this, {
            val subIndex = viewModel.arrSubCats.indexOf(product.subCategory)
            val subCat = viewModel.arrSubCategory.value!![subIndex]
            binding.actTVSubCategory.setText(subCat)
            viewModel.arrSubCategory.removeObservers(this)
            val arraySubcategoryAdapter =
                ArrayAdapter(applicationContext, R.layout.dropdown_item, it)
            binding.actTVSubCategory.setAdapter(arraySubcategoryAdapter)
        })

        binding.btnAddProduct.text = getString(R.string.update_product)
    }

    private fun openGalleryForImages() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_GALLERY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == OPEN_GALLERY_CODE) {
            // if multiple images are selected

            if (data?.clipData != null) {
                val count = data.clipData?.itemCount

                for (i in 0 until count!!) {
                    val imageUri: Uri = data.clipData?.getItemAt(i)!!.uri

                    if (!product.imagesListURI.contains(imageUri) && product.imagesListURI.count() < MAX_IMAGES_SIZE) {
                        product.imagesListURI.add(imageUri)
                        product.imageSlideList.add(SlideModel(imageUri.toString()))
                    } else if (product.imagesListURI.count() >= MAX_IMAGES_SIZE) {
                        Snackbar.make(binding.root, getString(R.string.max), Snackbar.LENGTH_LONG).show()
                    }
                }
            } else if (data?.data != null) {
                // if single image is selected

                val imageUri: Uri = data.data!!
                if (!product.imagesListURI.contains(imageUri)) {
                    product.imagesListURI.add(imageUri)
                    product.imageSlideList.add(SlideModel(imageUri.toString()))
                }
            }
            binding.imgSliderAddProduct.setImageList(
                product.imageSlideList,
                ScaleTypes.CENTER_INSIDE
            )
            binding.rvUploadImages.adapter?.notifyDataSetChanged()
            updateSliderUI()
        }
    }

    override fun onImageRemoved(position: Int) {
        product.imageSlideList.removeAt(position)
        product.imagesListURI.removeAt(position)
        if (product.images.count() > 0)
            product.removedImages.add(product.images.removeAt(position)!!)

        binding.imgSliderAddProduct.setImageList(
            product.imageSlideList,
            ScaleTypes.CENTER_INSIDE
        )

        binding.rvUploadImages.adapter?.notifyItemRemoved(position)
        updateSliderUI()
    }

    private fun updateSliderUI() {
        val param = binding.rvUploadImages.layoutParams as ViewGroup.MarginLayoutParams

        if (product.imageSlideList.count() == 0) {
            binding.imgSliderAddProduct.setBackgroundResource(R.drawable.choose_product)
            binding.rvUploadImages.background = null
            param.setMargins(0, 0, 0, 0)
        } else {
            binding.imgSliderAddProduct.background = null
            binding.rvUploadImages.setBackgroundResource(R.drawable.ed_style)
            val margin16 = resources.getDimension(R.dimen.margin_16).toInt()
            param.setMargins(margin16, margin16, margin16, 0)
        }

        binding.rvUploadImages.layoutParams = param
    }

    override fun onNewPropertyAdded(property: Property) {
        product.properties.add(property)
        binding.rcProperty.adapter!!.notifyItemInserted(product.properties.size - 1)
    }

    private fun validateInput(): Boolean {
        when {
            binding.edProductName.length() == 0 -> {
                binding.tiProductName.error = getString(R.string.Required)
                return false
            }
            binding.edProductName.length() < 5 -> {
                binding.tiProductName.error = getString(R.string.min_product_name_err)
                return false
            }
            binding.edDescription.length() == 0 -> {
                binding.tiDescription.error = getString(R.string.Required)
                return false
            }
            binding.edDescription.length() < 30 -> {
                binding.tiDescription.error =
                    getString(R.string.min_description_err)
                return false
            }
            binding.edOldPrice.length() == 0 -> {
                binding.tiOldPrice.error = getString(R.string.Required)
                return false
            }
            binding.actTVCategory.text.isNullOrEmpty() -> {
                binding.tiCategory.error = getString(R.string.Required)
                return false
            }
            binding.actTVSubCategory.text.isNullOrEmpty() -> {
                binding.tiSubCategory.error = getString(R.string.Required)
                return false
            }
        }

        if (product.imagesListURI.isNullOrEmpty()) {
            val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.imageSelected), Snackbar.LENGTH_LONG)
            val sbView: View = snackbar.view
            sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
            snackbar.show()

            return false
        }

        return true
    }
}