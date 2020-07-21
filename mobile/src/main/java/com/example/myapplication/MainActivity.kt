package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import cci.android.jlocalisation.api.API
import cci.android.jlocalisation.api.RetroFitHolder
import cci.android.jlocalisation.models.User
import com.example.myapplication.models.UserResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connexion.setOnClickListener{
            var result = onConnexionClicked(editTextTextEmailAddress.text.toString() , editTextTextPassword.text.toString())
            if(result.success){
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }else{
                errorMsg.visibility = View.VISIBLE
            }
        }
    }

    fun onConnexionClicked(pseudo: String, mdp: String) : UserResponse {
        var result: UserResponse = UserResponse()
        if (pseudo.trim() == "" || mdp.trim() == "") {
            Toast.makeText(this, "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show();
        } else {
            val jdevalikApiService: API =
                RetroFitHolder.retrofit.create(API::class.java)
            var pseudoParam: RequestBody = pseudo.toRequestBody(pseudo.toMediaTypeOrNull())
            var mdpParam: RequestBody = mdp.toRequestBody(mdp.toMediaTypeOrNull())
            jdevalikApiService.getUser(pseudoParam, mdpParam).also {
                GlobalScope.launch {
                    try {
                        it.await().also {
                            withContext(Dispatchers.Main) {
                                if(it.success) {
                                    //it.save()
                                    Log.d("Debug", "test")
                                } else {
                                    Log.d("Debug", it.toString())
                                }
                            }
                            result = it
                        }
                    } catch(e: HttpException) {
                        Log.d("Debug", "azdadzadzadzadzad")
                    }
                }
            }
        }
        return result
    }
}
