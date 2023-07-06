package com.example.faithconx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.faithconx.databinding.LayoutPostListBinding
import com.example.faithconx.model.RandomUsers
import com.example.faithconx.model.Result

class RandomUsersAdapter(var randomUsers: RandomUsers?): Adapter<RandomUsersAdapter.RandomUsersViewHolder>() {

    class RandomUsersViewHolder(val binding: LayoutPostListBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomUsersViewHolder {
        val binding = LayoutPostListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RandomUsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return randomUsers?.results?.size ?: 0
    }

    override fun onBindViewHolder(holder: RandomUsersViewHolder, position: Int) {
        randomUsers?.let {
            holder.binding.textTitle.text = it.results[position].location.city
            holder.binding.textSubTitle.text = "${it.results[position].name.first} ${it.results[position].name.last}"
            holder.binding.textNameOne.text = it.results[position].email
            holder.binding.textNameTwo.text = it.results[position].name.first
            holder.binding.textNameThree.text = it.results[position].gender
            holder.binding.textNameFour.text = it.results[position].phone
        }
    }

    //Update list
    fun updateList(users: RandomUsers?){
        randomUsers = users
        notifyDataSetChanged()
    }
}