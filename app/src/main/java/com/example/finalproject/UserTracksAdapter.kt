package com.example.finalproject

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserTracksAdapter(private val tracks: ArrayList<Track>) :
    RecyclerView.Adapter<UserTracksAdapter.MyViewHolder>() {

    private val baseURL = "https://ws.audioscrobbler.com/"
    private val apiKEY = "4d71bfa02b7255770d74c8147ad16883"

    private val TAG = "UserTracksAdapter"

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songName = itemView.findViewById<TextView>(R.id.song_name)
        val artistName = itemView.findViewById<TextView>(R.id.artist_name)
        val albumArt = itemView.findViewById<ImageView>(R.id.album_art)
        val playcount = itemView.findViewById<TextView>(R.id.playcount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_tracks, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = tracks[position]

        holder.songName.text = currentItem.name
        holder.artistName.text = currentItem.artist.name
        holder.playcount.text = currentItem.playcount.toString()

        // Use a callback to load the image asynchronously
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
                    Log.d(TAG, "Image URL: $imageUrl")
                    callback(imageUrl)
                }

                override fun onFailure(call: Call<TrackInfoResponse>, t: Throwable) {
                    Log.e(TAG, "Image fetch failed", t)
                    callback("")
                }
            })
    }
}
