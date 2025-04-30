package com.example.finalproject

import com.google.gson.annotations.SerializedName

// User's top tracks
data class UserTopTrackResponse(
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

// User's top albums
data class TopAlbumResponse(
    @SerializedName("topalbums")
    val topalbums: TopAlbums
)

data class TopAlbums (
    val album: List<Album>
)

data class Album (
    val artist: Artist,
    val image: List<MusicImage>,
    val url: String,
    val playcount: Int,
    val name: String
)

// User's top artists
data class TopArtistResponse(
    @SerializedName("topartists")
    val topartists: TopArtists
)

data class TopArtists(
    val artist: List<ArtistData>
)

data class ArtistData(
    val image: List<MusicImage>,
    val url: String,
    val playcount: Int,
    val name: String
)
