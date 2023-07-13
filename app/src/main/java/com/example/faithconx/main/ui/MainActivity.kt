package com.example.faithconx.main.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityMainBinding
import com.example.faithconx.home.ui.HomeFragment
import com.example.faithconx.profile.ui.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.btmNavView.setOnItemSelectedListener(this)

        setContentView(binding.root)
        setHomeFragment()
    }

    private fun setHomeFragment():Boolean {
       return supportFragmentManager.beginTransaction().add(R.id.fcvMain, HomeFragment()).commit() > 0
    }

    private fun setProfileFragment():Boolean {
        return supportFragmentManager.beginTransaction().replace(R.id.fcvMain,
            ProfileFragment()
        ).commit() > 0
    }



    // Handling the click events of the menu items
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       return when(item.itemId){
            R.id.profileMenu -> setProfileFragment()
            R.id.homeMenu -> setHomeFragment()
            else -> false
        }
    }


}
