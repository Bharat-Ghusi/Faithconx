package com.example.faithconx.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MenuItem.OnMenuItemClickListener
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.faithconx.R
import com.example.faithconx.databinding.FragmentProfileBinding
import com.example.faithconx.helper.user.UserSession
import com.example.faithconx.login.ui.ActivityLogin
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(), View.OnClickListener, OnMenuItemClickListener{
    private val userSession = UserSession()
    private lateinit var binding: FragmentProfileBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        setOnClickListener()

        return binding.root
    }

    private fun setOnClickListener() {
//        binding.btnLogout.setOnClickListener(this@ProfileFragment)
        binding.toolbarMain.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { onMenuItemClick(it) })
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuLogout -> logoutUser()
        }
        return true
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLogout -> logoutUser()
        }
    }

    private fun logoutUser() {
        userSession.createSession(activity)
        userSession.removeUser()
        firebaseAuth.signOut()
        activity?.supportFragmentManager?.popBackStack()
        activity?.finish()
        startActivity(Intent(activity, ActivityLogin::class.java))
    }




}