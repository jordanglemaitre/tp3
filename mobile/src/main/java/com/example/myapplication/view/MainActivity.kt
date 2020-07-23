package com.example.myapplication.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.api.RetrofitHolder
import com.example.myapplication.api.services.JdevalikApiService
import com.example.myapplication.models.User
import com.google.gson.JsonParser
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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Realm (just once per application)
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
            onTestClicked("-50.", "-14.1", "33")
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
                                    /*jdevalikApiService.getUser(it.id.toString(), "Bearer " + it.token).also {
                                    GlobalScope.launch {
                                        it.await().also {
                                            withContext(Dispatchers.Main) {
                                                if (it.id != 0) {
                                                    it.save()
                                                    Log.d("Debug", it.toString())
                                                    Toast.makeText(
                                                        context,
                                                        "Bonjour " + it.identifiant,
                                                        Toast.LENGTH_SHORT
                                                    ).show();
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Erreur lors de la recupération des informations",
                                                        Toast.LENGTH_SHORT
                                                    ).show();
                                                }
                                            }
                                        }
                                    }
                                }*/
                                } else {
                                    Log.d("Debug", "yes")
                                }
                            }
                        }
                    } catch(e: HttpException) {
                        manageApiErrorResponse(e)
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
                                if(it !is Boolean) {
                                    Toast.makeText(
                                        context,
                                        "Utilisateur créé avec succès",
                                        Toast.LENGTH_SHORT
                                    ).show();
                                }
                            } catch(e: HttpException) {
                                manageApiErrorResponse(e)
                            }
                        }
                    }
                }
            }
        }
    }

    fun onTestClicked(lon: String, lat: String, iduser: String) {
        var context = this
        val jdevalikApiService: JdevalikApiService =
            RetrofitHolder.retrofit.create(JdevalikApiService::class.java)

        var lonParam: RequestBody = lon.toRequestBody(lon.toMediaTypeOrNull())
        var latParam: RequestBody = lat.toRequestBody(lat.toMediaTypeOrNull())
        var iduserParam: RequestBody = iduser.toRequestBody(iduser.toMediaTypeOrNull())
        jdevalikApiService.insertAddress(lonParam, latParam, iduserParam).also {
            GlobalScope.launch {
                // TODO("implementer try/catch")
                it.await().also {
                    withContext(Dispatchers.Main) {
                        try {
                            //it.save()
                            if (it !is Boolean) {
                                Toast.makeText(
                                    context,
                                    "Adresse créée avec succès",
                                    Toast.LENGTH_SHORT
                                ).show();
                            }
                        } catch (e: HttpException) {
                            manageApiErrorResponse(e)
                        }
                    }
                }
            }
        }
    }

    fun manageApiErrorResponse(e: HttpException) {
        // Déclaration et initialisation d'un message d'erreur à vide
        var message = ""
        // Parse de l'objet Json sous forme de tableau de string
        val errorsArray = JsonParser().parse(e.response()?.errorBody()?.string()?.trim())
            .asJsonObject["errors"]
            .asJsonArray
        // Parcours du tableau d'erreurs
        errorsArray.forEach { item ->
            if (message == "") {
                // Premier message du tableau
                message = item.toString()
            } else {
                // Concaténation du message pour former une seule string avec retour à la ligne
                message += System.lineSeparator() + item.toString()
            }
        }
        message = message.replace("\"", "")

        // Si le message d'erreur n'a pas été initialisé
        if (message == null || message == "") {
            // Assignation d'un message d'erreur générique
            message = "Une erreur est survenue"
        }
        // Création du toast pour afficher l'erreur
        Looper.prepare()
        Toast.makeText(
            this,
            "Les identifiants sont érronés",
            Toast.LENGTH_SHORT
        ).show();
        Looper.loop()
    }

}

