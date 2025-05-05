package com.example.finalproject

import com.google.gson.annotations.SerializedName

// User's top tracks
data class TopTracksResponse(
    @SerializedName("toptracks")
    val toptracks: TopTracks
)

data class TopTracks (
    val track: List<Track>
)

data class Track (
    val name: String,
    val image: List<MusicImage>,
    val artist: Artist,
    val url: String,
    val playcount: Int
)

data class MusicImage (
    val size: String,
    @SerializedName("#text")
    val text: String
)

data class Artist (
    val url: String,
    val name: String
)

//// Artist's top tracks
//data class TopArtistResponse(
//    @SerializedName("topartists")
//    val topalbums: TopAlbums
//)
