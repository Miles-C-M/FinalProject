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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserAlbumsAdapter(private val albums: ArrayList<Album>) : RecyclerView.Adapter<UserAlbumsAdapter.MyViewHolder>() {

    private val TAG = "UserAlbumsAdapter"
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {
        // This class will represent a single row in our recyclerView list
        // This class also allows caching views and reuse them
        // Each MyViewHolder object keeps a reference to 3 view items in our row_item.xml file
        val albumName = itemView.findViewById<TextView>(R.id.song_name)
        val artistName = itemView.findViewById<TextView>(R.id.artist_name)
        val albumArt = itemView.findViewById<ImageView>(R.id.album_art)
        val playcount = itemView.findViewById<TextView>(R.id.playcount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate a layout from our XML (row_item.XML) and return the holder
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_tracks, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val currentItem = albums[position]
        Log.d(TAG, "albumName check")
        holder.albumName.text = currentItem.name
        Log.d(TAG, "artistName check")
        holder.artistName.text = currentItem.artist.name
        Log.d(TAG, "playcount check")
        holder.playcount.text = currentItem.playcount.toString()

        // Pick highest quality image
        Log.d(TAG, "images check")
        val sizeOrder = listOf("small", "medium", "large", "extralarge", "mega")
        val highestQualityImage = currentItem.image.maxByOrNull {
            sizeOrder.indexOf(it.size)
        }
        if (highestQualityImage != null) {
            Log.d(TAG, highestQualityImage.text)
        }
        // Get the context for glide
        val context = holder.itemView.context

        // Load the image from the url using Glide library
        // There is an issue right now with the last.fm api only returning the default image with each top artist search. currently this can be fixed by making another api call with track.getinfo but this will hurt the efficiency of the system
        if (highestQualityImage != null) {
            Glide.with(context)
                .load(highestQualityImage.text)
                .placeholder(R.drawable.baseline_audiotrack_24) // In case the image is not loaded show this placeholder image
                .into(holder.albumArt)
        }
    }

    override fun getItemCount(): Int {
        // Return the size of your dataset (invoked by the layout manager)
        return albums.size
    }
}