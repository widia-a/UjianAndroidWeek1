package com.bcaf.ujianandroidweek1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(LoginFragment.newInstance("", "")) //show login page at first
    }

    fun loadFragment(fragment: Fragment){
        val fragManager = supportFragmentManager.beginTransaction()
        fragManager.replace(R.id.vFragment,fragment)
        fragManager.addToBackStack("")
        fragManager.commit()
    }
}