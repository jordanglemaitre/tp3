package cci.android.jlocalisation.api

import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

// le RetroFitHolder contient l'url de base ou on renseignera les détails de la requête dans une interface selon ce que nous voulons récupérer
object RetroFitHolder {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://jdevalik.fr/tpiot/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient().newBuilder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build())
        .build()
}
