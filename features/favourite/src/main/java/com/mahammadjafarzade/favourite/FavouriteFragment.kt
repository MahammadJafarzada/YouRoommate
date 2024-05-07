package com.mahammadjafarzade.favourite

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mahammadjafarzade.entities.model.RoomCard
import com.mahammadjafarzade.favourite.databinding.FragmentFavouriteBinding
import com.mahammadjafarzade.homescreen.HomeScreenAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var favoriteList: ArrayList<RoomCard>
    private lateinit var adapter: HomeScreenAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var favoritesRef: DatabaseReference
    private var eventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        favoriteList = ArrayList()
        adapter = HomeScreenAdapter(requireContext(), favoriteList, null)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFavorite.layoutManager = gridLayoutManager
        binding.rvFavorite.adapter = adapter

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(currentUser.uid)
            eventListener = favoritesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    favoriteList.clear()
                    for (itemSnapshot in snapshot.children) {
                        val roomdata = itemSnapshot.getValue(RoomCard::class.java)
                        roomdata?.let {
                            favoriteList.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }

        return binding.root
    }

    private fun showExitDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are your sure exit from app?")
            .setPositiveButton("Yes") { _, _ ->
                requireActivity().finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.setOnShowListener {
            val messageView = dialog.findViewById<TextView>(android.R.id.message)
            messageView.setTextColor(Color.parseColor("#FFFFFFFF"))

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(Color.WHITE)

            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(Color.WHITE)
        }
        dialog.show()

    }
    private fun removeFromFavorites(roomCard: RoomCard) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val query = favoritesRef.orderByChild("id").equalTo(roomCard.uid)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        childSnapshot.ref.removeValue()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventListener?.let {
            favoritesRef.removeEventListener(it)
        }
    }
}
