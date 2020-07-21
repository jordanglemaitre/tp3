package com.spotto.android.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Place
import kotlinx.android.synthetic.main.item_address.view.*

class PlaceAdapter(var context: Context, val places: List<Place>) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>(){

    // à la création de la vue nous inflatons le layout item_address pour définir qu'un objet dans la liste ressemblera a ce layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false )
        return PlaceViewHolder(view)
    }

    // ici nous renseignons combien d'item il y a dans cette liste
    override fun getItemCount(): Int = places.size

    // pour chaque item de la liste nous déclanchons la méthode bind dans le AddressViewHolder
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(places[position])
    }

    // qui elle permet de renseigner le nom, les km ou encore l'image. On y passe également l'id du spot en Extra sur le spot cliqué associé
    inner class PlaceViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(place: Place){
            Log.d("Place6", place.toString())
            view.textAddress.text = place.lat.toString()
        }
    }
}