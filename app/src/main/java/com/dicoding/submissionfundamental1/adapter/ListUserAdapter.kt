package com.dicoding.submissionfundamental1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submissionfundamental1.R
import com.dicoding.submissionfundamental1.User
import com.dicoding.submissionfundamental1.databinding.ItemListUserBinding


class ListUserAdapter: RecyclerView.Adapter<ListUserAdapter.ListUserViewHolder>() {

    private val listUser = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(data: ArrayList<User>){
        listUser.clear()
        listUser.addAll(data)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    class ListUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemListUserBinding.bind(itemView)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val view: View =LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false)
        return ListUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        val user = listUser[position]

        holder.binding.apply {
            Glide.with(holder.itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(60, 60))
                    .into(imgAvatar)
            txtUsername.text = user.username?.let { checkNull(it) }
            txtLink.text = user.githubLink?.let { checkNull(it) }
        }

        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(listUser[holder.absoluteAdapterPosition])}
    }

    private fun checkNull(it: String): String {
        if (it == "null") return "-"
        return it
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}