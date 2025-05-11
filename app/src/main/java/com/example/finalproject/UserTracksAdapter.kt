package com.example.finalproject

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class UserTracksAdapter(
    private val tracks: ArrayList<Track>,
    private val userId: String
) : RecyclerView.Adapter<UserTracksAdapter.MyViewHolder>() {

    private val baseURL = "https://ws.audioscrobbler.com/"
    private val apiKEY = "4d71bfa02b7255770d74c8147ad16883"
    private val TAG = "UserTracksAdapter"

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songName: TextView = itemView.findViewById(R.id.song_name)
        val artistName: TextView = itemView.findViewById(R.id.artist_name)
        val albumArt: ImageView = itemView.findViewById(R.id.album_art)
        val playcount: TextView = itemView.findViewById(R.id.playcount)

        init {
            itemView.setOnClickListener {
                val selectedItem = adapterPosition
                if (selectedItem == RecyclerView.NO_POSITION) return@setOnClickListener

                val db = FirebaseFirestore.getInstance()
                val track = tracks[selectedItem]
                val trackId = "${track.artist.name}-${track.name}"
                val favRef = db.collection("users").document(userId)
                    .collection("favorites").document(trackId)

                val isFavorited = itemView.tag as? Boolean ?: false

                if (isFavorited) {
                    favRef.delete().addOnSuccessListener {
                        itemView.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, android.R.color.transparent)
                        )
                        itemView.tag = false
                        Toast.makeText(itemView.context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Log.e(TAG, "Failed to remove favorite", it)
                    }
                } else {
                    val data = mapOf(
                        "name" to track.name,
                        "artist" to track.artist.name,
                        "url" to track.url
                    )
                    favRef.set(data).addOnSuccessListener {
                        itemView.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, android.R.color.holo_green_light)
                        )
                        itemView.tag = true
                        Toast.makeText(itemView.context, "Added to favorites", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Log.e(TAG, "Failed to add favorite", it)
                    }
                }
            }

            itemView.setOnLongClickListener {
                val selectedItem = adapterPosition
                if (selectedItem != RecyclerView.NO_POSITION) {
                    val track = tracks[selectedItem]
                    val url = track.url
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    itemView.context.startActivity(intent)
                }
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_tracks, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = tracks[position]

        holder.songName.text = currentItem.name
        holder.artistName.text = currentItem.artist.name

        if (currentItem.playcount == 0) {
            holder.playcount.visibility = View.GONE
        } else {
            holder.playcount.visibility = View.VISIBLE
            holder.playcount.text = currentItem.playcount.toString()
        }

        // Check Firestore for favorite status
        val db = FirebaseFirestore.getInstance()
        val trackId = "${currentItem.artist.name}-${currentItem.name}"

        val favRef = db.collection("users").document(userId)
            .collection("favorites").document(trackId)

        favRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.holo_green_light)
                )
                holder.itemView.tag = true
            } else {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.transparent)
                )
                holder.itemView.tag = false
            }
        }

        // Load album artwork
        fetchTrackArtwork(currentItem.artist.name, currentItem.name) { imageUrl ->
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.baseline_audiotrack_24)
                .into(holder.albumArt)
        }
    }

    override fun getItemCount(): Int = tracks.size

    private fun fetchTrackArtwork(artist: String, track: String, callback: (String) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val lastFmAPI = retrofit.create(MusicService::class.java)

        Log.d(TAG, "Searching for artwork - Artist: $artist, Track: $track")

        lastFmAPI.searchTrackInfo("track.getinfo", artist, track, apiKEY, "json")
            .enqueue(object : Callback<TrackInfoResponse> {
                override fun onResponse(
                    call: Call<TrackInfoResponse>,
                    response: Response<TrackInfoResponse>
                ) {
                    val trackInfo = response.body()
                    val sizeOrder = listOf("small", "medium", "large", "extralarge", "mega")
                    val highestQualityImage = trackInfo?.track?.album?.image?.maxByOrNull {
                        sizeOrder.indexOf(it.size)
                    }
                    val imageUrl = highestQualityImage?.text ?: ""
                    callback(imageUrl)
                }

                override fun onFailure(call: Call<TrackInfoResponse>, t: Throwable) {
                    Log.e(TAG, "Image fetch failed", t)
                    callback("")
                }
            })
    }
}
