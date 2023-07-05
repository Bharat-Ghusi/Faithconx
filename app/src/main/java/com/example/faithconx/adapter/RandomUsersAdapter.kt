package com.example.faithconx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.faithconx.databinding.LayoutPostListBinding
import com.example.faithconx.model.RandomUsers
import com.example.faithconx.model.Result

class RandomUsersAdapter(val list:ArrayList<Result>): Adapter<RandomUsersAdapter.RandomUsersViewHolder>() {

    class RandomUsersViewHolder(val binding: LayoutPostListBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomUsersViewHolder {
        val binding = LayoutPostListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RandomUsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RandomUsersViewHolder, position: Int) {
        holder.binding.textTitle.text = list[position].name.
        holder.binding.textSubTitle.text= list[position].email

    }
}