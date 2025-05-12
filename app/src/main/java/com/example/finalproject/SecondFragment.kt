// Fragment for user profile
package com.example.finalproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

        // Adapter with long-click delete functionality and click to open URL
        trackAdapter = TrackAdapter(tracks) { track ->
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val favoritesRef = db.collection("users")
                    .document(currentUser.uid)
                    .collection("favorites")

                // Query for the track using its unique fields
                favoritesRef
                    .whereEqualTo("name", track.name)
                    .whereEqualTo("artist", track.artist)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (doc in documents) {
                            favoritesRef.document(doc.id).delete()
                                .addOnSuccessListener {
                                    Log.d(TAG, "Track deleted: ${track.name}")
                                    tracks.remove(track)
                                    trackAdapter.notifyDataSetChanged()

                                    // Show Toast message when the track is removed
                                    Toast.makeText(
                                        context,
                                        "${track.name} by ${track.artist} removed from favorites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Failed to delete track", e)
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Failed to query for deletion", e)
                    }
            }
        }

        recyclerView.adapter = trackAdapter

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            Log.d(TAG, "onCreateView: currentUser is null")
        } else {
            view.findViewById<TextView>(R.id.usernameText).text = currentUser.displayName

            val photoUrl = currentUser.photoUrl
            Glide.with(this)
                .load(photoUrl ?: R.drawable.app_logo)
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(view.findViewById<ImageView>(R.id.userImage))

            // Fetch user's favorited tracks
            db.collection("users")
                .document(currentUser.uid)
                .collection("favorites")
                .get()
                .addOnSuccessListener { result ->
                    tracks.clear()
                    for (document in result) {
                        val track = document.toObject(TrackData::class.java)
                        tracks.add(track)
                    }
                    trackAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }

        return view
    }
}