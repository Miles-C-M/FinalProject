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

// Used to grab album artwork since Last.FM API is returning a broken image with current calls
data class TrackInfoResponse(
    @SerializedName("track")
    val track: TrackInfo
)

data class TrackInfo(
    val album: Album
)

data class Album (
    val image: List<MusicImage>
)
