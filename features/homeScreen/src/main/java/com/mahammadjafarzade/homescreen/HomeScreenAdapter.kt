package com.mahammadjafarzade.homescreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mahammadjafarzade.entities.model.RoomCard

class HomeScreenAdapter(private val context: Context, private val dataList:List<RoomCard>): RecyclerView.Adapter<HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.room_list, parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.cardImg)
        holder.cardCity.text = dataList[position].city
        holder.cardPrice.text = dataList[position].price.toString()
    }
}
class HomeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var cardImg : ImageView
    var cardCity : TextView
    var cardPrice : TextView
    init {
        cardImg = itemView.findViewById(R.id.room_image)
        cardCity = itemView.findViewById(R.id.room_city)
        cardPrice = itemView.findViewById(R.id.room_price)
    }
}