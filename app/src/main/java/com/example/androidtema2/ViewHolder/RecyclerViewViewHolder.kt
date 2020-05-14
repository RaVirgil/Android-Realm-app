package com.example.androidtema2.ViewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtema2.Model.User
import com.example.androidtema2.R

class UserViewHolder(inflater: LayoutInflater,parent:ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_view_layout,parent,false)) {
    private var firstName: TextView? = null
    private var lastName: TextView? = null

    init{
        firstName=itemView.findViewById(R.id.recycler_first_name)
        lastName=itemView.findViewById(R.id.recycler_last_name)
    }

    fun bind(user: User) {
        firstName?.text=user.firstName
        lastName?.text=user.lastName
    }
}