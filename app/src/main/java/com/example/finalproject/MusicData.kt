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

// Search tracks
data class SearchTrackResponse(
    @SerializedName("results")
    val results: SearchResults
)

data class SearchResults(
    @SerializedName("trackmatches")
    val trackmatches: TrackMatches
)

data class TrackMatches(
    @SerializedName("track")
    val track1: List<Track1>
)

// Alternate track data class for track search
data class Track1(
    val name: String,
    val url: String,
    val artist: String,
    val image: List<MusicImage>
)