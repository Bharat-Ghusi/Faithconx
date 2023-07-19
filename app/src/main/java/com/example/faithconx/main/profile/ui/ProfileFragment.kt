package com.example.faithconx.main.profile.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MenuItem.OnMenuItemClickListener
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.faithconx.R
import com.example.faithconx.databinding.FragmentProfileBinding
import com.example.faithconx.helper.user.UserSession
import com.example.faithconx.login.ui.ActivityLogin
import com.example.faithconx.model.User
import com.example.faithconx.main.profile.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(), OnMenuItemClickListener{
    companion object{
        private  val TAG = ProfileFragment::class.java.simpleName
    }
    private val userSession = UserSession()
    private val profileViewModel = ProfileViewModel()
    private lateinit var binding: FragmentProfileBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        //Progressbar
        binding.grpAll.visibility =View.GONE
        binding.progressbar.visibility =View.VISIBLE


        setOnClickListener()
        setUserDataToTv()
        return binding.root
    }

    private fun setUserDataToTv() {
        profileViewModel.fetchCurrentUserData()
        activity?.let {activity->
            profileViewModel.getTaskState().observe(activity, Observer {taskState ->
                if(taskState){
                    Log.i(TAG,"Fetched Profile data successfully.")
                }
            })
            profileViewModel.getUser().observe(activity, Observer { user ->
                setToTv(user)

            })

        }
    }

    private fun setToTv(user: User?) {
        user?.let {user
            binding.grpAll.visibility =View.VISIBLE
            binding.progressbar.visibility =View.GONE
            binding.tvEmptyProfile.visibility = View.GONE
            setProfileImage(user.profileUrl)
        binding.tvUserFullName.text = user.firstName + user.lastName
        binding.tvUsername.text = user.email
        binding.tvUsername.text = user.email

        binding.tvFollowersList.text = "${user.followersCount.toString()} followers"
        binding.tvFollowingList.text = "${user.followingCount.toString()} followers"

        binding.tvDesignationOne.text = "${user.designationOne} at"
        binding.tvCompanyOne.text = user.companyName

        binding.tvDesignationTwo.text = user.designationTwo
        binding.tvSecCompanyName.text = user.companyName
        binding.tvRevenue.text = "$${user.revenue.toString()}"

        binding.tvHobby.text = user.hobby

        binding.tvCity.text = user.city
        binding.tvState.text = user.state

        binding.tvTwitterUsername.text = user.twitterUsername
        binding.tvInstagramUsername.text = user.instagramUsername

        binding.tvJoinedData.text = user.joinedDate
        } ?: kotlin.run {
            Log.i(TAG,"NO CONTENT")
            binding.grpAll.visibility = View.GONE
            binding.tvEmptyProfile.visibility = View.VISIBLE
            binding.progressbar.visibility = View.GONE
        }


    }

    private fun setProfileImage(profileUrl: String?) {
        profileUrl?.let { profileUrl ->
            if (profileUrl.isNotBlank()) {
                Glide.with(requireActivity() ).load(profileUrl)
//                    .apply(RequestOptions().override(400, 400))

                    .into(binding.ivProfile17x17)
            }
        }
    }


    private fun setOnClickListener() {
//        binding.btnLogout.setOnClickListener(this@ProfileFragment)
        binding.toolbarMain.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { onMenuItemClick(it) })
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuLogout -> showDialogBox(item)
        }
        return true
    }

    private fun showDialogBox(view: MenuItem){
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.do_you_want_to_logout))
                .setTitle("Alert !")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes)
                ) { dialog, which ->
                    logoutUser(view)
                     }
                .setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            builder.create().show()
        }
    }


    private fun logoutUser(view: MenuItem) {
        userSession.createSession(activity)
        userSession.removeUser()
        firebaseAuth.signOut()
        activity?.supportFragmentManager?.popBackStack()
        startActivity(Intent(activity, ActivityLogin::class.java))
        activity?.finish()
    }




}