package com.example.faithconx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.faithconx.databinding.LayoutPostListBinding
import com.example.faithconx.model.RandomUsers
import com.example.faithconx.model.Result

class RandomUsersAdapter(var resultList: MutableList<Result>?): Adapter<RandomUsersAdapter.RandomUsersViewHolder>() {

    class RandomUsersViewHolder(val binding: LayoutPostListBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomUsersViewHolder {
        val binding = LayoutPostListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RandomUsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return resultList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RandomUsersViewHolder, position: Int) {
        resultList?.let {
            holder.binding.textTitle.text = it[position].location.city
            holder.binding.textSubTitle.text = "${it[position].name.first} ${it[position].name.last}"
            holder.binding.textNameOne.text = it[position].email
            holder.binding.textNameTwo.text = it[position].name.first
            holder.binding.textNameThree.text = it[position].gender
            holder.binding.textNameFour.text = it[position].phone
        }
    }

    //Update list
    fun updateList(list: MutableList<Result>?){

        resultList?.clear()
        resultList = list
        notifyDataSetChanged()
    }
}