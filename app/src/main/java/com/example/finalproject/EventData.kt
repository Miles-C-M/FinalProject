// Data classes for ticketmaster api
package com.example.finalproject

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("_embedded")
    val embedded: Embedded
)

data class Embedded(
    val events: List<Event>
)

data class Event(
    val name: String,
    val dates: Dates,
    @SerializedName("_embedded")
    val embedded: EventEmbedded,
    val url: String, // Ticketmaster event URL
    val priceRange: PriceRanges,
    val images: List<EventImage>
)

data class Dates(
    val start: Start
)

data class Start(
    val localDate: String,
    val localTime: String
)

data class EventEmbedded(
    val venues: List<Venue>
)

data class Venue(
    val name: String,
    val city: City,
    val state: State,
    val address: Address,
    val url: String? = null // In case some venues have direct URLs
)


data class City(val name: String)
data class State(val stateCode: String)
data class Address(val line1: String)

data class PriceRanges(
    val priceRanges: List<PriceRange>
)

data class PriceRange(
    val min: Int,
    val max: Int
)

data class EventImage(
    val url: String,
    val width: Int,
    val height: Int
)