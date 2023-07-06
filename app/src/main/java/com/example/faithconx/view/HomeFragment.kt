package com.example.faithconx.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.faithconx.R
import com.example.faithconx.adapter.RandomUsersAdapter
import com.example.faithconx.databinding.FragmentHomeBinding
import com.example.faithconx.model.RandomUsers
import com.example.faithconx.model.Result
import com.example.faithconx.viewmodel.RandomUsersViewModel


class HomeFragment : Fragment() {
    private var list : MutableList<Result>? = null
private lateinit var binding:FragmentHomeBinding
private lateinit var  randomUsersViewModel: RandomUsersViewModel
private lateinit var randomUsersAdapter: RandomUsersAdapter
private var randomUsers: RandomUsers? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        randomUsersViewModel =  ViewModelProvider(this).get(RandomUsersViewModel::class.java) //Initialize RandomUsersViewModel
        Log.i("TAG", "HASHCODE: ${randomUsersViewModel.hashCode()}")
        setDataToAdapter(randomUsers)

        //Observer
        setProgressbar()
        setUi()

        return binding.root
    }


    //Set progress bar
    private fun setProgressbar(){
        val progressVisibility = randomUsersViewModel.getProgressVisibility()
        progressVisibility.observe(viewLifecycleOwner, Observer {
            //Set visibility of progressbar and recycler view true:Progressbar false: recyclerview
            if(it){
                binding.recyclerHomeFragment.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
            else{
                binding.recyclerHomeFragment.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun setUi() {
        val usersLiveData = randomUsersViewModel.getRandomUsers()
        usersLiveData.observe(viewLifecycleOwner, Observer {
            randomUsersAdapter.updateList(it)
        })
    }



    private fun setDataToAdapter(users:RandomUsers?) {
        binding.recyclerHomeFragment.layoutManager= LinearLayoutManager(context)
        randomUsersAdapter = RandomUsersAdapter(list) //empty list
        binding.recyclerHomeFragment.adapter = randomUsersAdapter

    }




}