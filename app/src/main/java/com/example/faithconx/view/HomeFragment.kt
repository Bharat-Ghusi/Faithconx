package com.example.faithconx.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.faithconx.R
import com.example.faithconx.adapter.RandomUsersAdapter
import com.example.faithconx.databinding.FragmentHomeBinding
import com.example.faithconx.model.RandomUsers
import com.example.faithconx.model.Result
import com.example.faithconx.viewmodel.RandomUsersViewModel


class HomeFragment : Fragment() {

private lateinit var binding:FragmentHomeBinding
private lateinit var  randomUsersViewModel: RandomUsersViewModel
private val  randomUsersViewModel1= RandomUsersViewModel()
    private val listOfRandomUsers = ArrayList<Result>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        setUi()
        return binding.root
    }


    private fun setUi() {
      randomUsersViewModel =  ViewModelProvider(this).get(RandomUsersViewModel::class.java)
        randomUsersViewModel. fetchRandomUsersFromApi()
        val usersLiveData = randomUsersViewModel.getRandomUsers()
        usersLiveData.observe(viewLifecycleOwner, Observer {
            setToAdapter(it)
        })
    }

    private fun setToList(results: RandomUsers?) {
        results?.let {
            for (users in it.results){
                listOfRandomUsers.add(users)
            }
        }
    }

    private fun setToAdapter(users:RandomUsers) {
//        setToList(users)
        binding.recyclerHomeFragment.layoutManager= LinearLayoutManager(context)
        binding.recyclerHomeFragment.adapter = RandomUsersAdapter(users)

    }



}