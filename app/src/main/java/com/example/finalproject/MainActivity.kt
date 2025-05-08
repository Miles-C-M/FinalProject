package com.example.finalproject

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // #### Authentication using FirebaseAuth #####

        // Get instance of the FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // If currentUser is null, open the RegisterActivity
        if (currentUser == null) {
            startRegisterActivity()
        } else {
            // Begin on profile fragment
            val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
            bottomNavigationView.selectedItemId = R.id.profile

            val firstFragment = FirstFragment()
            val secondFragment = SecondFragment()
            val thirdFragment = ThirdFragment()

            setCurrentFragment(secondFragment)
            Toast.makeText(this, "Welcome ${currentUser.displayName}. Shake to leave.", Toast.LENGTH_LONG).show()

            bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.events -> setCurrentFragment(firstFragment)
                    R.id.profile -> setCurrentFragment(secondFragment)
                    R.id.music -> setCurrentFragment(thirdFragment)

                }
                true
            }
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    // An helper function to start our RegisterActivity
    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        // Make sure to call finish() to remove this activity from the backstack, otherwise the user
        // would be able to go back to the MainActivity
        finish()
    }
}