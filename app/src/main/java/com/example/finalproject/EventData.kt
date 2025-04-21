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
/*
{
    "_embedded": {
        "events": [
            {
                "name": "Omar Courtz",
                "type": "event",
                "id": "Z7r9jZ1A7ffFa",
                "test": false,
                "url": "https://www.ticketmaster.com/event/Z7r9jZ1A7ffFa",
                "locale": "en-us",
                "images": [
                    {
                        "ratio": "16_9",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_RETINA_LANDSCAPE_16_9.jpg",
                        "width": 1136,
                        "height": 639,
                        "fallback": false
                    },
                    {
                        "ratio": "3_2",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_TABLET_LANDSCAPE_3_2.jpg",
                        "width": 1024,
                        "height": 683,
                        "fallback": false
                    },
                    {
                        "ratio": "3_2",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_RETINA_PORTRAIT_3_2.jpg",
                        "width": 640,
                        "height": 427,
                        "fallback": false
                    },
                    {
                        "ratio": "16_9",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_TABLET_LANDSCAPE_LARGE_16_9.jpg",
                        "width": 2048,
                        "height": 1152,
                        "fallback": false
                    },
                    {
                        "ratio": "16_9",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_SOURCE",
                        "width": 2426,
                        "height": 1365,
                        "fallback": false
                    },
                    {
                        "ratio": "16_9",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_RECOMENDATION_16_9.jpg",
                        "width": 100,
                        "height": 56,
                        "fallback": false
                    },
                    {
                        "ratio": "3_2",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_ARTIST_PAGE_3_2.jpg",
                        "width": 305,
                        "height": 203,
                        "fallback": false
                    },
                    {
                        "ratio": "16_9",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_EVENT_DETAIL_PAGE_16_9.jpg",
                        "width": 205,
                        "height": 115,
                        "fallback": false
                    },
                    {
                        "ratio": "16_9",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_RETINA_PORTRAIT_16_9.jpg",
                        "width": 640,
                        "height": 360,
                        "fallback": false
                    },
                    {
                        "ratio": "16_9",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_TABLET_LANDSCAPE_16_9.jpg",
                        "width": 1024,
                        "height": 576,
                        "fallback": false
                    },
                    {
                        "ratio": "4_3",
                        "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_CUSTOM.jpg",
                        "width": 305,
                        "height": 225,
                        "fallback": false
                    }
                ],
                "sales": {
                    "public": {
                        "startDateTime": "1900-01-01T06:00:00Z",
                        "startTBD": false,
                        "startTBA": false,
                        "endDateTime": "2025-04-16T23:30:00Z"
                    }
                },
                "dates": {
                    "start": {
                        "localDate": "2025-04-16",
                        "localTime": "19:30:00",
                        "dateTime": "2025-04-16T23:30:00Z",
                        "dateTBD": false,
                        "dateTBA": false,
                        "timeTBA": false,
                        "noSpecificTime": false
                    },
                    "status": {
                        "code": "rescheduled"
                    },
                    "spanMultipleDays": false
                },
                "classifications": [
                    {
                        "primary": true,
                        "segment": {
                            "id": "KZFzniwnSyZfZ7v7nJ",
                            "name": "Music"
                        },
                        "genre": {
                            "id": "KnvZfZ7vAv1",
                            "name": "Hip-Hop/Rap"
                        },
                        "subGenre": {
                            "id": "KZazBEonSMnZfZ7vke1",
                            "name": "Latin Hip-Hop"
                        },
                        "family": false
                    }
                ],
                "outlets": [
                    {
                        "url": "https://thewebsterct.com/",
                        "type": "venueBoxOffice"
                    },
                    {
                        "url": "https://www.ticketmaster.com/omar-courtz-hartford-connecticut-04-16-2025/event/Zu0FM1R0e5Fll8J",
                        "type": "tmMarketPlace"
                    }
                ],
                "seatmap": {
                    "staticUrl": "https://content.resale.ticketmaster.com/maps/3054-1-1-main.gif",
                    "id": "seatmap"
                },
                "ticketing": {
                    "allInclusivePricing": {
                        "enabled": true
                    },
                    "id": "ticketing"
                },
                "_links": {
                    "self": {
                        "href": "/discovery/v2/events/Z7r9jZ1A7ffFa?locale=en-us"
                    },
                    "attractions": [
                        {
                            "href": "/discovery/v2/attractions/K8vZ917hq_0?locale=en-us"
                        }
                    ],
                    "venues": [
                        {
                            "href": "/discovery/v2/venues/ZFr9jZ7vkA?locale=en-us"
                        }
                    ]
                },
                "_embedded": {
                    "venues": [
                        {
                            "name": "Webster Theatre",
                            "type": "venue",
                            "id": "ZFr9jZ7vkA",
                            "test": false,
                            "locale": "en-us",
                            "postalCode": "06114",
                            "timezone": "America/New_York",
                            "city": {
                                "name": "Hartford"
                            },
                            "state": {
                                "name": "Connecticut",
                                "stateCode": "CT"
                            },
                            "country": {
                                "name": "United States Of America",
                                "countryCode": "US"
                            },
                            "address": {
                                "line1": "31 Webster St",
                                "line2": "Hartford, CT"
                            },
                            "location": {
                                "longitude": "-72.670303000",
                                "latitude": "41.739101000"
                            },
                            "dmas": [
                                {
                                    "id": 296
                                }
                            ],
                            "upcomingEvents": {
                                "tmr": 4,
                                "_total": 4,
                                "_filtered": 0
                            },
                            "_links": {
                                "self": {
                                    "href": "/discovery/v2/venues/ZFr9jZ7vkA?locale=en-us"
                                }
                            }
                        }
                    ],
                    "attractions": [
                        {
                            "name": "Omar Courtz",
                            "type": "attraction",
                            "id": "K8vZ917hq_0",
                            "test": false,
                            "url": "https://www.ticketmaster.com/omar-courtz-tickets/artist/3026340",
                            "locale": "en-us",
                            "externalLinks": {
                                "musicbrainz": [
                                    {
                                        "id": "61ee1029-4f35-43ce-83dd-2eb8d4eef85e",
                                        "url": "https://musicbrainz.org/artist/61ee1029-4f35-43ce-83dd-2eb8d4eef85e"
                                    }
                                ]
                            },
                            "images": [
                                {
                                    "ratio": "16_9",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_RETINA_LANDSCAPE_16_9.jpg",
                                    "width": 1136,
                                    "height": 639,
                                    "fallback": false
                                },
                                {
                                    "ratio": "3_2",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_TABLET_LANDSCAPE_3_2.jpg",
                                    "width": 1024,
                                    "height": 683,
                                    "fallback": false
                                },
                                {
                                    "ratio": "3_2",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_RETINA_PORTRAIT_3_2.jpg",
                                    "width": 640,
                                    "height": 427,
                                    "fallback": false
                                },
                                {
                                    "ratio": "16_9",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_TABLET_LANDSCAPE_LARGE_16_9.jpg",
                                    "width": 2048,
                                    "height": 1152,
                                    "fallback": false
                                },
                                {
                                    "ratio": "16_9",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_SOURCE",
                                    "width": 2426,
                                    "height": 1365,
                                    "fallback": false
                                },
                                {
                                    "ratio": "16_9",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_RECOMENDATION_16_9.jpg",
                                    "width": 100,
                                    "height": 56,
                                    "fallback": false
                                },
                                {
                                    "ratio": "3_2",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_ARTIST_PAGE_3_2.jpg",
                                    "width": 305,
                                    "height": 203,
                                    "fallback": false
                                },
                                {
                                    "ratio": "16_9",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_EVENT_DETAIL_PAGE_16_9.jpg",
                                    "width": 205,
                                    "height": 115,
                                    "fallback": false
                                },
                                {
                                    "ratio": "16_9",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_RETINA_PORTRAIT_16_9.jpg",
                                    "width": 640,
                                    "height": 360,
                                    "fallback": false
                                },
                                {
                                    "ratio": "16_9",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_TABLET_LANDSCAPE_16_9.jpg",
                                    "width": 1024,
                                    "height": 576,
                                    "fallback": false
                                },
                                {
                                    "ratio": "4_3",
                                    "url": "https://s1.ticketm.net/dam/a/c59/5cddd46e-5c5f-4c83-84a8-74b0e0e22c59_CUSTOM.jpg",
                                    "width": 305,
                                    "height": 225,
                                    "fallback": false
                                }
                            ],
                            "classifications": [
                                {
                                    "primary": true,
                                    "segment": {
                                        "id": "KZFzniwnSyZfZ7v7nJ",
                                        "name": "Music"
                                    },
                                    "genre": {
                                        "id": "KnvZfZ7vAv1",
                                        "name": "Hip-Hop/Rap"
                                    },
                                    "subGenre": {
                                        "id": "KZazBEonSMnZfZ7vke1",
                                        "name": "Latin Hip-Hop"
                                    },
                                    "type": {
                                        "id": "KZAyXgnZfZ7v7la",
                                        "name": "Individual"
                                    },
                                    "subType": {
                                        "id": "KZFzBErXgnZfZ7vAde",
                                        "name": "Singer/Vocalist"
                                    },
                                    "family": false
                                }
                            ],
                            "upcomingEvents": {
                                "tmr": 5,
                                "ticketmaster": 13,
                                "_total": 18,
                                "_filtered": 0
                            },
                            "_links": {
                                "self": {
                                    "href": "/discovery/v2/attractions/K8vZ917hq_0?locale=en-us"
                                }
                            }
                        }
                    ]
                }
            },
 */

