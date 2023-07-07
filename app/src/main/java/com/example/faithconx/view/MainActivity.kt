package com.example.faithconx.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.faithconx.R
import com.example.faithconx.databinding.ActivityMainBinding
import com.example.faithconx.ui.theme.FaithconxTheme
import com.example.faithconx.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.bottomNavViewMainActivity.setOnItemSelectedListener(this)

        setContentView(binding.root)
        setHomeFragment()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun setHomeFragment():Boolean {
       return supportFragmentManager.beginTransaction().add(R.id.fragmentContainer,HomeFragment()).commit() > 0
    }

    private fun setProfileFragment():Boolean {
        return supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,ProfileFragment()).commit() > 0
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
