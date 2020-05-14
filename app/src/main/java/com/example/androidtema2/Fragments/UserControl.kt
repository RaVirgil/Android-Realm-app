package com.example.androidtema2.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.androidtema2.Model.User

import com.example.androidtema2.R
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_user_control.*
import org.json.JSONArray
import org.json.JSONObject

class UserControl : Fragment() {

    private val realm by lazy { Realm.getDefaultInstance() }
    private var userList = mutableListOf<User>()

    companion object {
        fun newInstance() = UserControl()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Realm.init(this.context!!)
        val config = RealmConfiguration.Builder()
            .name("users.realm").build()
        Realm.setDefaultConfiguration(config)

        btn_add_user.setOnClickListener {
            insertUser(first_name.text.toString(), last_name.text.toString())
            refreshUserList()
            recycler_view.adapter?.notifyDataSetChanged()
            Toast.makeText(
                this.context,
                "User ${first_name.text} ${last_name.text} added",
                Toast.LENGTH_SHORT
            ).show()
        }

        btn_remove_user.setOnClickListener {
            removeUser(first_name.text.toString(), last_name.text.toString())
            refreshUserList()
            recycler_view.adapter?.notifyDataSetChanged()
            Toast.makeText(
                this.context,
                "User ${first_name.text} ${last_name.text} deleted",
                Toast.LENGTH_SHORT
            ).show()
        }

        btn_sync.setOnClickListener {
            getUsersFromHttps()
            refreshUserList()
            recycler_view.adapter?.notifyDataSetChanged()
        }

        recycler_view.apply {
            refreshUserList()
            layoutManager = LinearLayoutManager(activity)
            adapter = com.example.androidtema2.Adapters.ListAdapter(userList)
        }
    }

    private fun removeUser(firstName: String, lastName: String) {
        realm.executeTransaction {
            val user = realm.where(User::class.java)
                .equalTo("firstName", firstName)
                .equalTo("lastName", lastName)
                .findAll()

            for (user in user) {
                user.deleteFromRealm()
            }
        }
    }

    private fun insertUser(firstName: String, lastName: String) {
        realm.executeTransaction {
            val maxId = realm.where(User::class.java).max("id")
            if (maxId == null) {
                val user = realm.createObject(User::class.java, 0)
                user.firstName = firstName
                user.lastName = lastName
            } else {
                val user = realm.createObject(User::class.java, (maxId.toLong() + 1))
                user.firstName = firstName
                user.lastName = lastName
            }
        }
    }

    private fun getUsersFromHttps() {
        val queue = Volley.newRequestQueue(this.context)
        val url = "https://jsonplaceholder.typicode.com/users"

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                handleResponse(response)
            },

            Response.ErrorListener {
                Toast.makeText(
                    this.context,
                    "That didnt work! :(",
                    Toast.LENGTH_SHORT
                ).show()
            })

        queue.add(stringRequest)
    }

    private fun handleResponse(response: String) {
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val jsonObject: JSONObject = jsonArray[i] as JSONObject
            insertUser(jsonObject.getString("name"), jsonObject.getString("username"))
        }
    }

    private fun getAllUsers(): MutableList<User> {
        val userList = realm.where(User::class.java)
            .findAll()

        val list = mutableListOf<User>()

        for (user in userList) {
            list.add(user)
        }
        return list
    }

    private fun refreshUserList() {
        userList.clear()
        userList.addAll(getAllUsers())
    }
}
