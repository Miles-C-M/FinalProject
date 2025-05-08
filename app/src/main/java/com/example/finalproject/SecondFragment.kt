package com.example.finalproject

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            Log.d(TAG, "onCreateView: currentUser is null")
        } else {
            // Display user information
            view.findViewById<TextView>(R.id.usernameText).text = currentUser.displayName

            Glide.with(this)
                .load(currentUser.photoUrl)
                .placeholder(R.drawable.app_logo)
                .into(view.findViewById<ImageView>(R.id.userImage))
        }
        view.findViewById<Toolbar>(R.id.toolbar).setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    // After logout, you can start the RegisterActivity again
                    startRegisterActivity()
                    }
                }
        }

        return view
    }


    private fun startRegisterActivity() {
        val intent = Intent(requireContext(), RegisterActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
