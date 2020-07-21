package cci.android.jlocalisation.api

import cci.android.jlocalisation.models.Adresse
import cci.android.jlocalisation.models.User
import com.example.myapplication.models.UserResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

// ici on définit les requêtes concernant les spots
interface API {
    @POST("insertuser.php")
    fun insertUser(pseudo: String, mdp: String): Call<User>

    @POST("updateuser.php")
    fun updateUser(pseudo: String, mdp: String): Call<User>

    @Multipart
    @POST("getuserbis.php")
    fun getUser(
        @Part("pseudo") pseudo : RequestBody,
        @Part("mdp") mdp : RequestBody
    ): Call<UserResponse>

    @POST("insertaddress.php")
    fun insertAddress(lon: Double, lat: Double, idUser: Int): Call<Adresse>

    @GET("getaddress.php")
    fun getAddress(idUser: Int): Call<List<Adresse>>

}