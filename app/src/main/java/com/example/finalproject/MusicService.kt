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
                            @Query("period") period: String): Call<TopTracksResponse>

    // https://ws.audioscrobbler.com/2.0/?method=artist.getTopTracks&artist=Hi-C&api_key=4d71bfa02b7255770d74c8147ad16883&format=json
    @GET("2.0/")
    fun searchArtistTopTracks(@Query("method") method: String,
                              @Query("artist") username: String,
                              @Query("api_key") apiKey: String,
                              @Query("format") format: String): Call<TopTracksResponse>

    // https://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=4d71bfa02b7255770d74c8147ad16883&artist=death&track=symbolic&format=json
//    @GET("2.0/")
//    fun searchTracks(@Query("method") method: String,
//                     @Query("track") track: String,
//                     @Query("api_key") apiKey: String,
//                     @Query("format") format: String): Call<SearchTrackResponse>


    // https://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=4d71bfa02b7255770d74c8147ad16883&artist=death&track=symbolic&format=json
    @GET("2.0/")
    fun searchTrackInfo(@Query("method") method: String,
                        @Query("artist") artist: String,
                        @Query("track") track: String,
                        @Query("api_key") apiKey: String,
                        @Query("format") format: String): Call<TrackInfoResponse>

}