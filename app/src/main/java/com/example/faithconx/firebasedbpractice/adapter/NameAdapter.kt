package com.example.faithconx.firebasedbpractice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.faithconx.databinding.TestDataLayoutBinding
import com.example.faithconx.firebasedbpractice.model.Users

class NameAdapter(val list:ArrayList<Users?>?): Adapter<NameAdapter.NameViewHolder>() {
    class NameViewHolder( val binding:TestDataLayoutBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val binding = TestDataLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return NameViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.binding.textName.text =
            list?.get(position)?.toString() ?: "DEFAULT"
    }
}