package com.andrei.kit.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrei.kit.databinding.CommentItemListBinding
import com.andrei.kit.models.Comment
import com.andrei.kit.models.Post

class AdapterComments : RecyclerView.Adapter<AdapterComments.ViewHolder>() {

    private val data = ArrayList<Comment>()

    fun setData(newData: MutableList<Comment>){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
    inner class ViewHolder(var binding: CommentItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindDataToView(comment: Comment) {
            binding.comment = comment;
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindDataToView(data[position]);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context);
        val commentItemListBinding: CommentItemListBinding = CommentItemListBinding.inflate(layoutInflater, parent, false);
        return ViewHolder(commentItemListBinding);
    }

    override fun getItemCount(): Int {
        return data.size;
    }
}