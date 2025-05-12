package com.example.finalproject

import TrackAdapter
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SecondFragment : Fragment() {
    private val TAG = "SecondFragment"
    private val db = FirebaseFirestore.getInstance()
    private lateinit var trackAdapter: TrackAdapter
    private val tracks = mutableListOf<TrackData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_second, container, false)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.userRecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        trackAdapter = TrackAdapter(tracks)
        recyclerView.adapter = trackAdapter

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            Log.d(TAG, "onCreateView: currentUser is null")
        } else {
            // Fetch user data
            view.findViewById<TextView>(R.id.usernameText).text = currentUser.displayName

            val photoUrl = currentUser.photoUrl
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)
                    .into(view.findViewById<ImageView>(R.id.userImage))
            } else {
                Glide.with(this)
                    .load(R.drawable.app_logo)
                    .into(view.findViewById<ImageView>(R.id.userImage))
            }

            // Fetch user's favorited tracks from Firestore
            db.collection("users")
                .document(currentUser.uid)
                .collection("favorites")
                .get()
                .addOnSuccessListener { result ->
                    tracks.clear()  // Clear the existing data
                    for (document in result) {
                        val track = document.toObject(TrackData::class.java)
                        tracks.add(track)  // Add the new track to the list
                    }
                    trackAdapter.notifyDataSetChanged()  // Notify the adapter to update the RecyclerView
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }

        return view
    }
}
