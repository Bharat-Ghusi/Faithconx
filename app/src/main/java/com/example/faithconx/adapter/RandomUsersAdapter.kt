package com.example.faithconx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.faithconx.databinding.LayoutPostListBinding
import com.example.faithconx.model.RandomUsers
import com.example.faithconx.model.Result

class RandomUsersAdapter(val randomUsers: RandomUsers): Adapter<RandomUsersAdapter.RandomUsersViewHolder>() {

    class RandomUsersViewHolder(val binding: LayoutPostListBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomUsersViewHolder {
        val binding = LayoutPostListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RandomUsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return randomUsers.results.size
    }

    override fun onBindViewHolder(holder: RandomUsersViewHolder, position: Int) {
        holder.binding.textTitle.text = randomUsers.results[position].location.city
        holder.binding.textSubTitle.text= "${randomUsers.results[position].name.first} ${randomUsers.results[position].name.last}"
        holder.binding.textNameOne.text = randomUsers.results[position].email
        holder.binding.textNameTwo.text = randomUsers.results[position].name.first
        holder.binding.textNameThree.text = randomUsers.results[position].gender
        holder.binding.textNameFour.text = randomUsers.results[position].phone


    }
}