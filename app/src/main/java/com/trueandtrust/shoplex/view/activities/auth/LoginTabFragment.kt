package com.trueandtrust.shoplex.view.activities.auth

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.LoginTabFragmentBinding
import com.trueandtrust.shoplex.viewmodel.AuthVM
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginTabFragment :Fragment() {

    private lateinit var binding: LoginTabFragmentBinding
    private lateinit var authVM: AuthVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        authVM = (activity as AuthActivity).authVM
        binding = LoginTabFragmentBinding.inflate(inflater, container, false)
        binding.storeData = authVM
        binding.tvForgetPass.setOnClickListener {
            openDialog()
        }

        authVM.isLoginBtnPressed.observe(requireActivity(), {
            if(it){
                validate()
            }
        })

        onEditTextChanged()

        return binding.root
    }

    private fun validate(){
        when {
            binding.edEmail.length() == 0 -> binding.tiEmail.error = getString(R.string.Required)
            !(isEmailValid(binding.edEmail.text.toString())) -> binding.tiEmail.error = getString(R.string.require_email)
            binding.edPassword.length() == 0 -> binding.tiPassword.error =
                getString(R.string.Required)
            binding.edPassword.length() < 8 -> binding.tiPassword.error =
                getString(R.string.min_password_err)

            else -> authVM.isLoginValid.value = true
        }
        authVM.isLoginBtnPressed.value = false
    }

    //Email Validation
    private fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun onEditTextChanged(){
        binding.edEmail.addTextChangedListener {
            binding.tiEmail.error = null
        }
        binding.edPassword.addTextChangedListener {
            binding.tiPassword.error = null
        }
    }

    //openDialog
    private fun openDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.forget_password))
        val view: View = layoutInflater.inflate(R.layout.dialog_forget_password, null)
        builder.setView(view)
        val email: EditText = view.findViewById(R.id.edEmailDialog)
        builder.setPositiveButton(getString(R.string.reset)) { _, _ ->
            when {
                email.text.toString().isEmpty() -> email.error = getString(R.string.Required)
                else -> forgetPassword(email.text.toString())
            }
        }

        builder.setNegativeButton(getString(R.string.close_dialog)) { _, _ ->

        }
        builder.show()
    }


    //Forget Password
    private fun forgetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.email_send), Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                snackbar.show()


            } else {
                val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.require_email), Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                snackbar.show()


            }
        }
    }
}