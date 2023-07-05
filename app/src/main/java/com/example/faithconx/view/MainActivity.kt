package com.example.faithconx.view

import android.content.Intent
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setHomeFragment()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun setHomeFragment() {
        val homeFragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction().add(R.id.fragmentContainer,HomeFragment()).commit()
    }

}
