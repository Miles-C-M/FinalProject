package com.example.finalproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {

    // https://ws.audioscrobbler.com/2.0/?method=user.gettoptracks&user=TheMurf10&api_key=4d71bfa02b7255770d74c8147ad16883&format=json&limit=10&period=7day
    @GET("2.0/")
    fun searchUserTopTracks(@Query("method") method: String,
                            @Query("user") username: String,
                            @Query("api_key") apiKey: String,
                            @Query("format") format: String,
                            @Query("limit") limit: Int,
                            @Query("period") period: String): Call<UserTopTrackResponse>

    @GET("2.0/")
    fun searchUserTopAlbums(@Query("method") method: String,
                            @Query("user") username: String,
                            @Query("api_key") apiKey: String,
                            @Query("format") format: String,
                            @Query("limit") limit: Int,
                            @Query("period") period: String): Call<TopAlbumResponse>

    @GET("2.0/")
    fun searchUserTopArtists(@Query("method") method: String,
                             @Query("user") username: String,
                             @Query("api_key") apiKey: String,
                             @Query("format") format: String,
                             @Query("limit") limit: Int,
                             @Query("period") period: String): Call<TopArtistResponse>

    // Search for artists
    // https://ws.audioscrobbler.com/2.0/?method=artist.search&artist=Hi-C&api_key=4d71bfa02b7255770d74c8147ad16883&format=json

    // Search for artists top albums
    // https://ws.audioscrobbler.com/2.0/?method=artist.getTopAlbums&artist=Hi-C&api_key=4d71bfa02b7255770d74c8147ad16883&format=json

    // Search for artists top songs
    // https://ws.audioscrobbler.com/2.0/?method=artist.getTopTracks&artist=Hi-C&api_key=4d71bfa02b7255770d74c8147ad16883&format=json

}