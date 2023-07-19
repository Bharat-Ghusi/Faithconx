package com.example.faithconx.main.home.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.faithconx.databinding.FragmentHomeBinding
import com.example.faithconx.main.home.adapter.RandomUsersAdapter
import com.example.faithconx.main.home.viewmodel.RandomUsersViewModel
import com.example.faithconx.model.RandomUsers
import com.example.faithconx.model.Result
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess


class HomeFragment : Fragment(), OnRefreshListener {
    private var list: MutableList<Result>? = null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var randomUsersViewModel: RandomUsersViewModel
    private lateinit var randomUsersAdapter: RandomUsersAdapter
    private var randomUsers: RandomUsers? = null
    private val firebaseAuth = FirebaseAuth.getInstance()
    companion object{
        private val TAG = HomeFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        randomUsersViewModel =
            ViewModelProvider(this).get(RandomUsersViewModel::class.java) //Initialize RandomUsersViewModel
        Log.i("TAG", "HASHCODE: ${randomUsersViewModel.hashCode()}")

        setDataToAdapter(randomUsers)
        setOnClickListener()
        //Observer
        setContentToRecyclerView()
        setUi()

        return binding.root
    }

    private fun setOnClickListener() {
        binding.swiperRefreshLayout.setOnRefreshListener(this)
    }

    /**
     * On refresh of home fragment
     */
    override fun onRefresh() {

        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(activity, "Post is up to date", Toast.LENGTH_SHORT).show()
            // to indicate that the refresh is finished
            binding.swiperRefreshLayout.isRefreshing = false
        }, 1000)
    }


    /**
     * Set content to recycler view
     */
    private fun setContentToRecyclerView() {
        val progressVisibility = randomUsersViewModel.getProgressVisibility()
        progressVisibility.observe(viewLifecycleOwner, Observer {
            //Set visibility of progressbar and recycler view true:Progressbar false: recyclerview
            if (it) {
                binding.rvNewPost.visibility = View.GONE
                binding.pbHome.visibility = View.VISIBLE
            } else {
                binding.rvNewPost.visibility = View.VISIBLE
                binding.pbHome.visibility = View.GONE
            }
        })
    }

    private fun setUi() {
        val usersLiveData = randomUsersViewModel.getRandomUsers()
        usersLiveData.observe(viewLifecycleOwner, Observer {
            randomUsersAdapter.updateList(it)
        })
    }


    private fun setDataToAdapter(users: RandomUsers?) {
        binding.rvNewPost.layoutManager = LinearLayoutManager(context)
        randomUsersAdapter = RandomUsersAdapter(list) //empty list
        binding.rvNewPost.adapter = randomUsersAdapter

    }


}