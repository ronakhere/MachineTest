package com.ronaktest.myapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.ronaktest.myapp.databinding.ActivityMainBinding
import com.ronaktest.myapp.pixabayapp.activities.ImageDisplayActivity
import com.ronaktest.myapp.ui.updateProfile.PhoneNumberVerification
import com.ronaktest.myapp.util.AppPreference
import com.ronaktest.myapp.util.Constants
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var nav_view: com.google.android.material.navigation.NavigationView? = null

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(R.id.fragment1, R.id.fragment2),
            my_drawer
        )
    }

    private val navController: NavController by lazy{findNavController(R.id.nav_host)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        )
        NavigationUI.setupActionBarWithNavController(this, navController, my_drawer)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)

        var name= AppPreference.getInstance(this).getString("NAME")
        val header = (findViewById<View>(R.id.nav_view) as com.google.android.material.navigation.NavigationView)
            .getHeaderView(0)

        var tv = header.findViewById<TextView>(R.id.tv_user_name)
        tv.setText(name)

        nav_view = findViewById(R.id.nav_view)
        nav_view!!.setNavigationItemSelectedListener { menuItem ->
            my_drawer!!.closeDrawers()
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.nav_profile -> navController.navigate(R.id.fragment2)
                R.id.nav_contact_list -> navController.navigate(R.id.fragment1)
                R.id.nav_image_task -> callImageFragment()
                R.id.nav_logout -> callLogOut()
            }
            true
        }
    }

    private fun callImageFragment() {
        var intent = Intent(this, ImageDisplayActivity::class.java)
        startActivity(intent)
    }

    private fun callLogOut() {
        AppPreference.getInstance(applicationContext).putString(Constants.IsLogin, "false")
        var intent1 = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent1)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, my_drawer)
    }
}