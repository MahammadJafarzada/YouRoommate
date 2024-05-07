package com.mahammadjafarzade.homescreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mahammadjafarzade.entities.model.RoomCard
import com.mahammadjafarzade.homescreen.databinding.RoomListBinding

class HomeScreenAdapter(
    private val context: Context,
    private var dataList: List<RoomCard>,
    private var favoriteItemClickListener: OnFavoriteItemClickListener?,
    private var onItemClickListener: ((RoomCard) -> Unit)? = null

) : RecyclerView.Adapter<HomeViewHolder>() {

    interface OnFavoriteItemClickListener {
        fun onAddFavorite(roomCard: RoomCard)
        fun onRemoveFavorite(roomCard: RoomCard)
    }

    fun setOnItemClickListener(listener: (RoomCard) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.room_list, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].image).into(holder.cardImg)
        holder.cardCity.text = dataList[position].city
        holder.cardPrice.text = dataList[position].price.toString()
        holder.cardDescription.text = dataList[position].description
        holder.cardDetail.setOnClickListener {
            onItemClickListener?.invoke(dataList[position])

        }

        // Set favorite icon based on the roomCard state
        val favoriteIconDrawable = if (dataList[position].isFavorite) R.drawable.heart_icon else R.drawable.heart_icon
        holder.favoriteIcon.setImageResource(favoriteIconDrawable)

        // Handle favorite icon click
        holder.favoriteIcon.setOnClickListener {
            val roomCard = dataList[position]
            roomCard.isFavorite = !roomCard.isFavorite
            notifyItemChanged(position)
            if (roomCard.isFavorite) {
                favoriteItemClickListener?.onAddFavorite(roomCard)
            } else {
                favoriteItemClickListener?.onRemoveFavorite(roomCard)
            }
        }
    }

    fun searchDataList(searchList: List<RoomCard>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var cardImg: ImageView
    var cardCity: TextView
    var cardPrice: TextView
    var cardDetail: CardView
    var cardDescription : TextView
    var favoriteIcon: ImageView

    init {
        cardImg = itemView.findViewById(R.id.imageView)
        cardCity = itemView.findViewById(R.id.txt_city)
        cardPrice = itemView.findViewById(R.id.txt_price)
        cardDescription = itemView.findViewById(R.id.txt_address)
        cardDetail = itemView.findViewById(R.id.detailRoom)
        favoriteIcon = itemView.findViewById(R.id.imageButtonFav)
    }
}