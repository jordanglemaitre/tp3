package com.example.myapplication.api

import com.example.myapplication.models.Place
import com.example.myapplication.models.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// ici on définit les requêtes concernant les spots
interface API {

//    @Headers("Content-Type:application/json")
  //  @POST("getaddress.php")
    //fun getPlaces(@Body user:JsonObject): Call<List<Place>>

    @Multipart
    @POST("getaddress.php")
    fun getPlaces(
        @Part("iduser") id : Int
    ): Call<List<Place>>


    @POST("insertuser.php")
    fun insertUser(pseudo: String, mdp: String): Call<User>

    @POST("updateuser.php")
    fun updateUser(pseudo: String, mdp: String): Call<User>

    @POST("getuser.php")
    fun getUser(pseudo: String, mdp: String): Call<List<User>>


    @POST("insertaddress.php")
    fun insertPlace(lon: Double, lat: Double, idUser: Int): Call<Place>

}