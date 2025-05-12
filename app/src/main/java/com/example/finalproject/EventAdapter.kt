// Adapter for ticketmaster api recycler
package com.example.finalproject

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class EventAdapter(private val events: ArrayList<Event>) : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {

    private val TAG = "EventAdapter"
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {
        // This class will represent a single row in our recyclerView list
        // This class also allows caching views and reuse them
        // Each MyViewHolder object keeps a reference to 3 view items in our row_item.xml file
        val eventName = itemView.findViewById<TextView>(R.id.event_name)
        val venueName = itemView.findViewById<TextView>(R.id.venue_name)
        val address = itemView.findViewById<TextView>(R.id.address)
        val date = itemView.findViewById<TextView>(R.id.date)
        val price = itemView.findViewById<TextView>(R.id.price_range)
        val eventImage = itemView.findViewById<ImageView>(R.id.event_image)
        val ticketButton = itemView.findViewById<Button>(R.id.ticket_button)

        // init block is used as part of the primary constructor (It is executed when a ViewHolder instance is created)
        init {
            ticketButton.setOnClickListener {
                val selectedPosition = adapterPosition
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    val currentEvent = events[selectedPosition]
                    val url = currentEvent.url
                    val context = itemView.context
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            }

            itemView.setOnClickListener {
                val selectedPosition = adapterPosition
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    val currentEvent = events[selectedPosition]
                    // Optional: handle item click if needed
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate a layout from our XML (row_item.XML) and return the holder
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val currentItem = events[position]
        Log.d(TAG, "eventName check")
        holder.eventName.text = currentItem.name
        Log.d(TAG, "venueName check")
        holder.venueName.text = currentItem.embedded.venues[0].name
        Log.d(TAG, "address check")
        holder.address.text = buildString {
            append(currentItem.embedded.venues[0].address.line1)
            append(", ")
            append(currentItem.embedded.venues[0].city.name)
            append(", ")
            append(currentItem.embedded.venues[0].state.stateCode)
        }

        //"localDate": "2025-04-16",
        //"localTime": "19:30:00"
        val dateText = try {
            val localDate = currentItem.dates.start.localDate?.let { LocalDate.parse(it) }
            val localTime = currentItem.dates.start.localTime?.let { LocalTime.parse(it) }

            val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)

            if (localDate != null && localTime != null) {
                "${localDate.format(dateFormatter)} @ ${localTime.format(timeFormatter)}"
            } else if (localDate != null) {
                localDate.format(dateFormatter)
            } else {
                "Date/time not available"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Date/time parsing error: ${e.message}")
            "Date/time not available"
        }
        holder.date.text = dateText


        // Check for price range, not all events return a price range
        val priceRange = currentItem.priceRange?.priceRanges?.firstOrNull()
        val priceText = if (priceRange != null) {
            "From \$${priceRange.min} to \$${priceRange.max}"
        } else {
            "Price info unavailable"
        }
        Log.d(TAG, "dateTime check")
        holder.price.text = priceText

        // Pick highest quality image

        Log.d(TAG, "images check")
        val highestQualityImage = currentItem.images.maxByOrNull {  it.width * it.height  }
        if (highestQualityImage != null) {
            Log.d(TAG, highestQualityImage.url)
        }

        // Get the context for glide
        val context = holder.itemView.context

        // Load the image from the url using Glide library
        if (highestQualityImage != null) {
            Glide.with(context)
                .load(highestQualityImage.url)
                .placeholder(R.drawable.ic_launcher_background) // In case the image is not loaded show this placeholder image
                .circleCrop() // optional - Circle image with rounded corners
                .into(holder.eventImage)
        }

    }

    override fun getItemCount(): Int {
        // Return the size of your dataset (invoked by the layout manager)
        return events.size
    }

}