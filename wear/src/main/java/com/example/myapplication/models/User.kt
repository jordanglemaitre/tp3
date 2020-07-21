package com.example.myapplication.models

import com.google.gson.JsonObject

public class User{

    val id : Int = -1
    val pseudo : String = ""
    val mdp : String = ""

    fun toJsonObject(id: Int): JsonObject {
        // Création de l'objet principal contenant toutes les propriétés
        val params: JsonObject = JsonObject()
        // Ajout des propriétés
        params.addProperty("iduser", id)

        return params
    }
}