package com.dicoding.consumerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.consumerapp.databinding.ItemListUserBinding

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ListUserViewHolder>() {

    private val listUser = ArrayList<UserFavorite>()

    fun setData(data: ArrayList<UserFavorite>) {
        listUser.clear()
        listUser.addAll(data)
        notifyDataSetChanged()
    }

    class ListUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemListUserBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false)
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
    }

    private fun checkNull(it: String): String {
        if (it == "null") return "-"
        return it
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}