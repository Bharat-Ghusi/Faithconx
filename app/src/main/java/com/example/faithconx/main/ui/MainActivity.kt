package com.example.faithconx.main.ui

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityMainBinding
import com.example.faithconx.home.ui.HomeFragment
import com.example.faithconx.profile.ui.ProfileFragment
import com.example.faithconx.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private var previousSelectedMenuItem: MenuItem? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //------------//
        binding.btmNavView.setOnItemSelectedListener(this)
//        setHomeFragment()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setHomeFragment(item: MenuItem): Boolean {
        previousSelectedMenuItem?.let {
            it.isChecked = false
        }
        item.isChecked = true // Select the current item
        previousSelectedMenuItem = item

        return supportFragmentManager.beginTransaction().add(R.id.fcvMain, HomeFragment())
            .commit() > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setProfileFragment(item: MenuItem): Boolean {
        previousSelectedMenuItem?.let {
            it.isChecked = false
        }
        item.isChecked = true // Select the current item
        previousSelectedMenuItem = item



        return supportFragmentManager.beginTransaction().replace(
            R.id.fcvMain,
            ProfileFragment()
        ).commit() > 0
    }


    // Handling the click events of the menu items
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profileMenu -> setProfileFragment(item)
            R.id.homeMenu -> setHomeFragment(item)
            R.id.searchMenu -> setSearchFragment(item)
            R.id.newPostMenu -> setNewPostFragment(item)
            R.id.chatMenu -> setChatFragment(item)
            else -> false
        }
    }

    private fun setChatFragment(item: MenuItem): Boolean {

        Toast.makeText(this, Constants.COMING_SOON_MSG,Toast.LENGTH_SHORT).show()
        return true
    }

    private fun setNewPostFragment(item: MenuItem): Boolean {

        Toast.makeText(this, Constants.COMING_SOON_MSG,Toast.LENGTH_SHORT).show()
        return true
    }

    private fun setSearchFragment(item: MenuItem): Boolean {

        Toast.makeText(this, Constants.COMING_SOON_MSG,Toast.LENGTH_SHORT).show()
        return true
    }


}
