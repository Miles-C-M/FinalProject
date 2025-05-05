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

class ThirdFragment : Fragment(), AdapterView.OnItemSelectedListener {


    private var searchType = ""
    private var search = ""

    private val baseURL = "https://ws.audioscrobbler.com/"
    private val apiKEY = "4d71bfa02b7255770d74c8147ad16883"

    private val TAG = "ThirdFragment"

    private lateinit var searchTerm: EditText
    private lateinit var spinner: Spinner
    private lateinit var recycler: RecyclerView
    private lateinit var userTracksList: ArrayList<Track>
    private lateinit var adapter: UserTracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        searchTerm = view.findViewById(R.id.search_input)
        spinner = view.findViewById(R.id.search_types)
        recycler = view.findViewById(R.id.event_recycler)
        val searchButton = view.findViewById<Button>(R.id.search_button)

        // Setup spinner
        val optionList = listOf("Choose a search category", "User Top Tracks", "Artist Top Songs", "Songs")
        val optionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionList)
        spinner.adapter = optionAdapter
        spinner.onItemSelectedListener = this


        // Setup RecyclerView
        userTracksList = ArrayList()
        adapter = UserTracksAdapter(userTracksList)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        // Button click listener
        searchButton.setOnClickListener {
            search = searchTerm.text.toString()
            if (searchType.isEmpty() || searchType == "Error") {
                infoMissing("Search type not selected")
            } else if (search.isEmpty()) {
                infoMissing("Type something")
            } else {
                fetchMusic()
            }
        }

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        searchType = when(position){
            0 -> ""
            1 -> "user.gettoptracks"
            2 -> "artist.gettoptracks"
            3 -> "track.search"
            else -> "Error"
        }
        // Update hint text
        when(searchType){
            "user.gettoptracks" -> searchTerm.hint = "Last.fm username"
            "artist.gettoptracks" -> searchTerm.hint = "Artist name"
            "track.search" -> searchTerm.hint = "Song title"
            else -> searchTerm.hint = "Select a search category"
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(requireContext(), "Nothing is selected!", Toast.LENGTH_SHORT).show()
    }

    private fun fetchMusic() {

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val eventAPI = retrofit.create(MusicService::class.java)
        Log.d(TAG, "searchType: $searchType search: $search")
        when(searchType){
            "user.gettoptracks" -> {
                eventAPI.searchUserTopTracks(searchType, search, apiKEY, "json", 20, "overall").enqueue(object : Callback<TopTracksResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(call: Call<TopTracksResponse>, response: Response<TopTracksResponse>) {
                        val body = response.body()

                        if (!response.isSuccessful || body == null) {
                            Toast.makeText(requireContext(), "Something went wrong. Try again.", Toast.LENGTH_LONG).show()
                            return
                        }

                        val topTracks = body.toptracks
                        val tracks = topTracks?.track

                        if (tracks.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), "No tracks found for your search.", Toast.LENGTH_LONG).show()
                            return
                        }

                        userTracksList.clear()
                        userTracksList.addAll(tracks)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<TopTracksResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: $t")
                    }
                })
            }
            "artist.gettoptracks" -> eventAPI.searchArtistTopTracks(searchType, search, apiKEY, "json").enqueue(object : Callback<TopTracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<TopTracksResponse>, response: Response<TopTracksResponse>) {
                    val body = response.body()

                    if (!response.isSuccessful || body == null) {
                        Toast.makeText(requireContext(), "Something went wrong. Try again.", Toast.LENGTH_LONG).show()
                        return
                    }

                    val topTracks = body.toptracks
                    val tracks = topTracks?.track

                    if (tracks.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), "No tracks found for your search.", Toast.LENGTH_LONG).show()
                        return
                    }

                    userTracksList.clear()
                    userTracksList.addAll(tracks)
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<TopTracksResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }
            })
        }
    }

    private fun infoMissing(title: String) {
        // Set message
        val message = if (title == "Type something") {
            "Search cannot be empty. Please type in a song title, artist name, or Last.fm username."
        } else {
            "Search type cannot be empty. Please select a search type."
        }
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setIcon(android.R.drawable.ic_delete)
            .setPositiveButton("Okay", null)
            .show()
    }
}

