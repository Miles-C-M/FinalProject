package com.example.finalproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitexample.EventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var eventType = ""
    private var cityName = ""

    private val baseURL = "https://app.ticketmaster.com/discovery/v2/"
    private val apiKEY = "utUZ2vXWbvwVOxG76DO2Fs6yqeiqXpbx"

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create list of strings to appear in spinner
        val optionList = listOf("Choose an event category", "Music", "Sports", "Theater", "Family", "Arts & Theater", "Concerts", "Comedy", "Dance")

        // Create an adapter with 3 parameters: activity (this), layout (using a pre-built layout), list to show
        val optionAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, optionList)

        // Store the the listView widget in a variable
        val optionSpinner = findViewById<Spinner>(R.id.event_types)

        // set the adapter to the spinner
        optionSpinner.adapter = optionAdapter

        // set the onItemSelectedListener as (this).  (this) refers to this activity that implements OnItemSelectedListener interface
        optionSpinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Callback method to be invoked when the selection disappears from this view. For example,
        // if the list becomes empty and the ArrayAdapter is notified, this callback will be invoked
        Toast.makeText(this, "Nothing is selected!", Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        // Based on the index of position selected, set the corresponding event type
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


    // Submit button functionality
    fun buttonClick(view: View) {
        // Set cityName
        cityName = findViewById<EditText>(R.id.city_input).text.toString()

        // Input validation
        if (eventType == "" || eventType == "Error") {
            eventMissing()
        } else if (cityName == "") {
            locationMissing()
        } else {

            // Set up recycler view
            val eventList = ArrayList<Event>()
            val adapter = EventAdapter(eventList)
            val recycler = findViewById<RecyclerView>(R.id.event_recycler)

            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(this)


            // Call ticketmaster api with cityName and eventType info
            // Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val eventAPI = retrofit.create(EventService::class.java)
            eventAPI.searchEvents(apiKEY, cityName, eventType, "date,asc").enqueue(object :
                Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    Log.d(TAG, "onResponse: $response")

                    val body = response.body()

                    if (!response.isSuccessful || body == null) {
                        Log.d(TAG, "Response not successful or body is null")
                        Toast.makeText(this@MainActivity, "Something went wrong. Try again.", Toast.LENGTH_LONG).show()
                        return
                    }

                    val embedded = body.embedded
                    val events = embedded?.events

                    if (events.isNullOrEmpty()) {
                        Log.d(TAG, "No events found for the given city or category.")
                        Toast.makeText(this@MainActivity, "No events found for your search.", Toast.LENGTH_LONG).show()
                        return
                    }

                    eventList.clear()
                    Log.d(TAG, events[0].name)
                    eventList.addAll(events)
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }

            })
        }
    }

    // Shows dialog message for missing location input
    private fun locationMissing() {
        // Create an alertdialog builder object,
        // then set attributes that you want the dialog to have
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Location missing")
        builder.setMessage("City cannot be empty. Please enter a city.")
        // Set an icon, optional
        builder.setIcon(android.R.drawable.ic_delete)

        // Set the button actions, all of them are optional
        builder.setPositiveButton("Okay"){ dialog, which ->
            // code to run when YES is pressed
        }

        // create the dialog and show it
        val dialog = builder.create()
        dialog.show()
    }

    // Shows dialog message for missing event category
    private fun eventMissing() {
        // Create an alertdialog builder object,
        // then set attributes that you want the dialog to have
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Event category missing")
        builder.setMessage("Event category cannot be empty. Please select an event category")
        // Set an icon, optional
        builder.setIcon(android.R.drawable.ic_delete)

        // Set the button actions, all of them are optional
        builder.setPositiveButton("Okay"){ dialog, which ->
            // code to run when YES is pressed
        }

        // create the dialog and show it
        val dialog = builder.create()
        dialog.show()
    }
}