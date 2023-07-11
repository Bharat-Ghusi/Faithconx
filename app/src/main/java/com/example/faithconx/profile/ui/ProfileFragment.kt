package com.example.faithconx.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.faithconx.R
import com.example.faithconx.databinding.FragmentProfileBinding
import com.example.faithconx.login.ui.ActivityLogin
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() ,View.OnClickListener{
private lateinit var binding: FragmentProfileBinding
private val firebaseAuth =FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        setOnClickListener()

        return binding.root
    }

    private fun setOnClickListener() {
    binding.btnLogout.setOnClickListener(this@ProfileFragment)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnLogout -> logoutUser()
        }
    }

    private fun logoutUser() {
        firebaseAuth.signOut()
        activity?.supportFragmentManager?.popBackStack()
        startActivity(Intent(activity,ActivityLogin::class.java))
    }


}