package com.example.myapplication.api.services

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface JdevalikApiService {
    @Multipart
    @POST("insertuserbis.php")
    fun insertUser(
        @Part ("pseudo") pseudo : RequestBody,
        @Part ("mdp") mdp : RequestBody
    ): Call<Any>

    @Multipart
    @POST("getuser.php")
    fun getUser(
        @Part ("pseudo") pseudo : RequestBody,
        @Part ("mdp") mdp : RequestBody
    ) : Call<Any>

    @Multipart
    @POST("insertaddress.php")
    fun insertAddress(
        @Part ("lon") lon : RequestBody,
        @Part ("lat") lat : RequestBody,
        @Part ("iduser") iduser : RequestBody
    ) : Call<Any>


}