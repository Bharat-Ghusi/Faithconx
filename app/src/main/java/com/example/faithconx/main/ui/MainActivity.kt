package com.example.faithconx.main.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityMainBinding
import com.example.faithconx.login.ui.ActivityLogin
import com.example.faithconx.main.chat.ChatFragment
import com.example.faithconx.main.home.ui.HomeFragment
import com.example.faithconx.main.newpost.NewPostFragment
import com.example.faithconx.main.profile.ui.ProfileFragment
import com.example.faithconx.main.search.SearchFragment
import com.example.faithconx.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private var previousSelectedMenuItem: MenuItem? = null
    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //------------//
        binding.btmNavView.setOnItemSelectedListener(this)
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

        previousSelectedMenuItem?.let {
            it.isChecked = false
        }
        item.isChecked = true // Select the current item
        previousSelectedMenuItem = item



        return supportFragmentManager.beginTransaction().replace(
            R.id.fcvMain,
            ChatFragment()
        ).commit() > 0
    }

    private fun setNewPostFragment(item: MenuItem): Boolean {
        previousSelectedMenuItem?.let {
            it.isChecked = false
        }
        item.isChecked = true // Select the current item
        previousSelectedMenuItem = item



        return supportFragmentManager.beginTransaction().replace(
            R.id.fcvMain,
            NewPostFragment()
        ).commit() > 0
    }

    private fun setSearchFragment(item: MenuItem): Boolean {

        previousSelectedMenuItem?.let {
            it.isChecked = false
        }
        item.isChecked = true // Select the current item
        previousSelectedMenuItem = item



        return supportFragmentManager.beginTransaction().replace(
            R.id.fcvMain,
            SearchFragment()
        ).commit() > 0
    }


}
