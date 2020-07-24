package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.api.RetrofitHolder
import com.example.myapplication.api.services.JdevalikApiService
import com.example.myapplication.models.User
import com.example.myapplication.view.MapsActivity
import com.google.android.gms.wearable.MessageClient.OnMessageReceivedListener
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.await


class MainActivity : AppCompatActivity(), OnMessageReceivedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this);

        var realm = Realm.getDefaultInstance()

        // Listener sur lr bouton de connexion
        connexion.setOnClickListener {
            Log.d("Debug", email.text.toString() + password.text.toString())
            // onConnexionClicked(email.text.toString(), password.text.toString())
            onConnexionClicked("bob", "123456")
        }
        inscription.setOnClickListener {
            Log.d("Debug", email.text.toString() + password.text.toString())
            // onConnexionClicked(email.text.toString(), password.text.toString())
            onInscriptionPressed("Framboise", "123456")
        }
        test.setOnClickListener {
            Log.d("Debug", email.text.toString() + password.text.toString())
            // onConnexionClicked(email.text.toString(), password.text.toString())
            // onTestClicked("-50.", "-14.1", "33")
            // EnvoieNotificationSimple("testazdadzad")
        }
    }

    fun onConnexionClicked(pseudo: String, mdp: String) {
        var context = this
        if (pseudo.trim() == "" || mdp.trim() == "") {
            Toast.makeText(this, "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show();
        } else {
            val jdevalikApiService: JdevalikApiService =
                RetrofitHolder.retrofit.create(JdevalikApiService::class.java)
            var pseudoParam: RequestBody = pseudo.toRequestBody(pseudo.toMediaTypeOrNull())
            var mdpParam: RequestBody = mdp.toRequestBody(mdp.toMediaTypeOrNull())
            val result = jdevalikApiService.getUser(pseudoParam, mdpParam).also {
                GlobalScope.launch {
                    try {
                        it.await().also {
                            withContext(Dispatchers.Main) {
                                if(it is User) {
                                    //it.save()
                                    Log.d("Debug", "test")
                                } else {
                                    Log.d("Debug", "yes")
                                }
                            }
                        }
                    } catch(e: HttpException) {
                        // manageApiErrorResponse(e)
                    }
                }
            }
        }
    }

    fun onInscriptionPressed(pseudo: String, mdp: String) {
        var context = this
        if (pseudo.trim() == "" || mdp.trim() == "") {
            Toast.makeText(this, "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show();
        } else {
            val jdevalikApiService: JdevalikApiService =
                RetrofitHolder.retrofit.create(JdevalikApiService::class.java)

            val pseudoParam: RequestBody = pseudo.toRequestBody(pseudo.toMediaTypeOrNull())
            val mdpParam: RequestBody = mdp.toRequestBody(mdp.toMediaTypeOrNull())
            jdevalikApiService.insertUser(pseudoParam, mdpParam).also {
                GlobalScope.launch {
                    // TODO("implementer try/catch")
                    it.await().also {
                        withContext(Dispatchers.Main) {
                            try {
                                //it.save()
                                if (it !is Boolean) {
                                    Toast.makeText(
                                        context,
                                        "Utilisateur créé avec succès",
                                        Toast.LENGTH_SHORT
                                    ).show();
                                }
                            } catch (e: HttpException) {
                                // manageApiErrorResponse(e)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        Wearable.getMessageClient(this).addListener(this)
    }


    override fun onMessageReceived(messageEvent: MessageEvent) {

        Log.i("CIO", "Received message: ")
        Log.i("CIO", "  - Path: " + messageEvent.path)
        val message = String(messageEvent.data)
        var latlon = message.split("-")
        Log.i("CIO", "  - Content: $message")
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("Lat", latlon[0].replace(",", "."))
        intent.putExtra("Lon", latlon[1].replace(",", "."))
        startActivity(intent)
    }
}

