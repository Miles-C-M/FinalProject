// Interface for ticketmaster api
package com.example.finalproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EventService {

    // https://app.ticketmaster.com/discovery/v2/events.json?keyword=eventType&city=cityName&sort=date.asc&apikey=utUZ2vXWbvwVOxG76DO2Fs6yqeiqXpbx

    @GET("events.json")
    fun searchEvents(@Query("apikey") apiKey: String,
                     @Query("city") city: String,
                     @Query("keyword") eventType: String,
                     @Query("sort") sort: String): Call<EventResponse>
}