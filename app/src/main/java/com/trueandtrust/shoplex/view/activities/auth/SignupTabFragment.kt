package com.trueandtrust.shoplex.view.activities.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.SignupTabFragmentBinding
import com.trueandtrust.shoplex.model.enumurations.LocationAction
import com.trueandtrust.shoplex.model.pojo.Location
import com.trueandtrust.shoplex.view.activities.MapsActivity
import com.trueandtrust.shoplex.viewmodel.AuthVM
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupTabFragment: Fragment() {
    private lateinit var binding: SignupTabFragmentBinding
    private lateinit var authVM: AuthVM

    private lateinit var startImageChooser: ActivityResultLauncher<Intent>
    private lateinit var startMaps: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startImageChooser =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = it.data
                    if (data != null || data?.data != null) {
                        val uri = data.data
                        authVM.store.value!!.image = uri.toString()
                        binding.imgSignup.setImageURI(uri)
                    }
                }
            }

        startMaps = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                if (data != null || data?.data != null) {
                    val location: Location? = data.getParcelableExtra(MapsActivity.LOCATION)
                    val address: String? = data.getStringExtra(MapsActivity.ADDRESS)
                    if (location != null && address != null) {
                        binding.tvLocation.text = address
                        authVM.store.value!!.addresses.clear()
                        authVM.store.value!!.locations.clear()
                        authVM.store.value!!.addresses.add(address)
                        authVM.store.value!!.locations.add(location)
                        authVM.primaryAddress.value = address
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignupTabFragmentBinding.inflate(inflater, container, false)
        authVM = (activity as AuthActivity).authVM
        binding.storeData = authVM


        authVM.isSignupBtnPressed.observe(requireActivity(), {
            if(it){
                authVM.isSignupValid.value = checkEditText()
                authVM.isLoginBtnPressed.value = false
            }
        })

        binding.btnLocation.setOnClickListener {
            startMaps.launch(Intent(requireContext(), MapsActivity::class.java).apply {
                this.putExtra(MapsActivity.LOCATION_ACTION, LocationAction.Add.name)
            })
        }

        binding.imgSignup.setOnClickListener{
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.type = "image/*"

            startImageChooser.launch(intent)
        }
        onEditTextChanged()

        return binding.root
    }

    private fun onEditTextChanged(){
        binding.edName.addTextChangedListener {
            binding.tiName.error = null
        }
        binding.edEmail.addTextChangedListener {
            binding.tiEmail.error = null
        }
        binding.edPassword.addTextChangedListener {
            binding.tiPassword.error = null
        }
        binding.edConfirmPassword.addTextChangedListener {
            binding.tiConfirmPassword.error = null
        }
        binding.edPhone.addTextChangedListener {
            binding.tiPhone.error = null
        }
        binding.tvLocation.addTextChangedListener{
            binding.tvLocation.error = null
        }
    }

    //check EditText
    private fun checkEditText(): Boolean {
        when {
            binding.edName.length() == 0 -> binding.edName.error = getString(R.string.Required)
            binding.edName.length() < 5 -> binding.edName.error =
                getString(R.string.min_store_name_err)
            binding.edEmail.length() == 0 -> binding.edEmail.error = getString(R.string.Required)
            !(isEmailValid(binding.edEmail.text.toString())) -> binding.edEmail.error =
                getString(
                    R.string.require_email
                )
            binding.edPassword.length() == 0 -> binding.edPassword.error =
                getString(R.string.Required)
            binding.edPassword.length() < 8 -> binding.edPassword.error =
                getString(R.string.min_password_err)
            binding.edConfirmPassword.length() == 0 -> binding.edConfirmPassword.error =
                getString(R.string.Required)
            binding.edConfirmPassword.text.toString() != binding.edPassword.text.toString() -> binding.edConfirmPassword.error =
                getString(
                    R.string.not_match
                )

            binding.edPhone.text.toString().isEmpty() -> binding.tiPhone.error =
                getString(R.string.Required)

            !isValidMobile(binding.edPhone.text.toString()) -> binding.tiPhone.error =
               getString(R.string.valid_phone)


//            binding.edPhone.length() == 0 -> binding.edPhone.error = getString(R.string.Required)
            authVM.store.value?.addresses?.size == 0 || authVM.store.value?.locations?.size == 0 -> Toast.makeText(requireContext(),"Choose Your Location",Toast.LENGTH_LONG).show()

            authVM.store.value?.image.isNullOrEmpty() -> {
                val snackbar = Snackbar.make(
                    binding.root,
                    binding.root.context.getString(R.string.choose_image),
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

            else -> return true
        }
        return false
    }

    //Email Validation
    private fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isValidMobile(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length in 12..13
        } else false
    }
}