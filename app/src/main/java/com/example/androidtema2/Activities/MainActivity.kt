package com.example.androidtema2.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidtema2.Fragments.UserControl
import com.example.androidtema2.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openFragment()
    }

    private fun openFragment(){
        val fragment = UserControl.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.main_activity,fragment,"fragment")
            .commit()
    }


}
