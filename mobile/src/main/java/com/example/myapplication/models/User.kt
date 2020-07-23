package com.example.myapplication.models

import io.realm.RealmObject

open class User(var id: String? = null,
                var pseudo: String? = null,
                var mdp: String? = null) : RealmObject() {

    override fun toString(): String {
        return "User(id=$id, pseudo=$pseudo, mdp=$mdp)"
    }

}