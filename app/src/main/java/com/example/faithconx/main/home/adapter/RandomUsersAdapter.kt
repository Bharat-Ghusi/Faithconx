package com.example.faithconx.main.home.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
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
            holder.binding.tvTitle.text = it[position].location.city
            holder.binding.tvSubTitle.text = "${it[position].name.first} ${it[position].name.last}"
            holder.binding.tvFirstName.text = it[position].email
            holder.binding.tvSecondName.text = it[position].name.first
            holder.binding.tvThirdName.text = it[position].gender
            holder.binding.tvFourName.text = it[position].phone
           if(it[position].picture.thumbnail.endsWith("jpg",true) && it[position].picture.thumbnail.isNotBlank()){
            Glide.with(holder.binding.ivProfileTwo.context).load(it[position].picture.thumbnail)
                .apply(RequestOptions().override(100,100))

                .into(holder.binding.ivProfileTwo)
           }

        }
    }

    //Update list
    fun updateList(list: MutableList<Result>?){

        resultList?.clear()
        resultList = list
        notifyDataSetChanged()
    }
}