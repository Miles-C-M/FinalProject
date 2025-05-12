// Fragment for ticketmaster
package com.example.finalproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FirstFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var eventType = ""
    private var cityName = ""

    private val baseURL = "https://app.ticketmaster.com/discovery/v2/"
    private val apiKEY = "utUZ2vXWbvwVOxG76DO2Fs6yqeiqXpbx"

    private val TAG = "FirstFragment"

    private lateinit var cityInput: EditText
    private lateinit var spinner: Spinner
    private lateinit var recycler: RecyclerView
    private lateinit var eventList: ArrayList<Event>
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        cityInput = view.findViewById(R.id.search_input)
        spinner = view.findViewById(R.id.search_types)
        recycler = view.findViewById(R.id.event_recycler)
        val searchButton = view.findViewById<Button>(R.id.search_button)

        // Setup spinner
        val optionList = listOf("Choose an event category", "Music", "Sports", "Theater", "Family", "Arts & Theater", "Concerts", "Comedy", "Dance")
        val optionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionList)
        spinner.adapter = optionAdapter
        spinner.onItemSelectedListener = this

        // Setup RecyclerView
        eventList = ArrayList()
        adapter = EventAdapter(eventList)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        // Button click listener
        searchButton.setOnClickListener {
            cityName = cityInput.text.toString()
            if (eventType.isEmpty() || eventType == "Error") {
                infoMissing("Event category not selected")
            } else if (cityName.isEmpty()) {
                infoMissing("City not selected")
            } else {
                fetchEvents()
            }
        }

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        eventType = when(position){
            0 -> ""
            1 -> "Music"
            2 -> "Sports"
            3 -> "Theater"
            4 -> "Family"
            5 -> "Arts & Theater"
            6 -> "Concerts"
            7 -> "Comedy"
            8 -> "Dance"
            else -> "Error"
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(requireContext(), "Nothing is selected!", Toast.LENGTH_SHORT).show()
    }

    private fun fetchEvents() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val eventAPI = retrofit.create(EventService::class.java)
        eventAPI.searchEvents(apiKEY, cityName, eventType, "date,asc").enqueue(object : Callback<EventResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    Toast.makeText(requireContext(), "Something went wrong. Try again.", Toast.LENGTH_LONG).show()
                    return
                }

                val embedded = body.embedded
                val events = embedded?.events

                if (events.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "No events found for your search.", Toast.LENGTH_LONG).show()
                    return
                }

                eventList.clear()
                eventList.addAll(events)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }
        })
    }

    private fun infoMissing(title: String) {
        // Set message
        val message = if (title == "City not selected") {
            "City cannot be empty. Please enter a city."
        } else {
            "Event category cannot be empty. Please select an event category."
        }
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setIcon(android.R.drawable.ic_delete)
            .setPositiveButton("Okay", null)
            .show()
    }
}

