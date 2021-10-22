package com.trueandtrust.shoplex.view.activities

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ActivityHomeBinding
import com.trueandtrust.shoplex.databinding.DialogAddReportBinding
import com.trueandtrust.shoplex.databinding.NavHeaderBinding
import com.trueandtrust.shoplex.model.extra.FirebaseReferences
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.pojo.Report
import com.trueandtrust.shoplex.view.activities.auth.AuthActivity
import com.trueandtrust.shoplex.viewmodel.AuthVM
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        if(StoreInfo.lang != this.resources.configuration.locale.language)
            StoreInfo.setLocale(StoreInfo.lang, this)
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        bottomNavigationView = binding.navigationView
        bottomNavigationView.setupWithNavController(findNavController(R.id.nav_host_fragment))

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        navView = binding.navView
        drawerLayout = binding.drawerLayout

        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()
        val switch = navView.menu.getItem(3).actionView.findViewById<SwitchCompat>(R.id.switch_id)
        switch.isChecked = StoreInfo.readNotification(this)

        switch.setOnClickListener {
            StoreInfo.saveNotification(this, switch.isChecked)
        }

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.lastOrderFragment -> startActivity(Intent(this, LastOrderActivity::class.java))

                R.id.locationFragment -> startActivity(
                    Intent(
                        this,
                        StoreLocationActivity::class.java
                    )
                )

                R.id.Language -> {
                    if(it.title.toString().contains("En", true)){
                        getSharedPreferences("LANG", MODE_PRIVATE).edit().putString("Language", "en").apply()
                        StoreInfo.lang = "en"
                        StoreInfo.setLocale("en", this)
                    }else{
                        getSharedPreferences("LANG", MODE_PRIVATE).edit().putString("Language", "ar").apply()
                        StoreInfo.lang = "ar"
                        StoreInfo.setLocale("ar", this)
                    }
                }

                R.id.report -> showAddReportDialog()

                R.id.Logout -> showDialog()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val header: NavHeaderBinding = NavHeaderBinding.inflate(layoutInflater, this.navView, true)
        Glide.with(this).load(StoreInfo.image).error(R.drawable.init_img).into(header.navHeaderImage)
        header.tvStoreName.text = StoreInfo.name
        header.tvStoreEmail.text = StoreInfo.email
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)

    }

    override fun onBackPressed() {
        val selectedItemId = bottomNavigationView.selectedItemId
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (selectedItemId == R.id.homeFragment) finishAffinity()
            else findNavController(R.id.nav_host_fragment).popBackStack()
        }
    }

    private fun showAddReportDialog() {
        val dialogBinding = DialogAddReportBinding.inflate(layoutInflater)
        val reportBtnSheetDialog =
            BottomSheetDialog(dialogBinding.root.context, R.style.BottomSheetDialogTheme)
        dialogBinding.btnSendReport.setOnClickListener {
            val reportMsg = dialogBinding.edReport.text.toString()
            FirebaseReferences.ReportRef.add(Report(reportMsg))
            reportBtnSheetDialog.dismiss()
            val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.reportsuccess), Snackbar.LENGTH_LONG)
            val sbView: View = snackbar.view
            sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
            snackbar.show()
        }
        reportBtnSheetDialog.setContentView(dialogBinding.root)
        reportBtnSheetDialog.show()
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.logOut))
        builder.setMessage(getString(R.string.logoutMessage))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            startActivity(Intent(this, AuthActivity::class.java))
            AuthVM.logout(this)
            val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.logoutSuccess), Snackbar.LENGTH_LONG)
            val sbView: View = snackbar.view
            sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
            snackbar.show()

        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}