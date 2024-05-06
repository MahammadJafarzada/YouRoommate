package com.mahammadjafarzade.homescreen

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mahammadjafarzade.entities.model.RoomCard
import com.mahammadjafarzade.homescreen.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeScreenAdapter.OnFavoriteItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dataList: ArrayList<RoomCard>
    private lateinit var adapter: HomeScreenAdapter
    private lateinit var auth: FirebaseAuth
    private var eventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        dataList = ArrayList()
        adapter = HomeScreenAdapter(requireContext(), dataList, this)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvRoom.layoutManager = gridLayoutManager
        binding.rvRoom.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("Room card")
        auth = FirebaseAuth.getInstance()

        eventListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val roomdata = itemSnapshot.getValue(RoomCard::class.java)
                    roomdata?.let {
                        dataList.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        adapter.setOnItemClickListener { roomCard ->
            val bundle = Bundle().apply {
                putString("Number", roomCard.number)
                putString("City", roomCard.city)
                putString("Price", roomCard.price.toString())
                putString("Title", roomCard.title)
                putString("Description", roomCard.description)
                putString("Image", roomCard.image)
            }
            findNavController().navigate(R.id.action_homeFragment_to_roomCardDetailFragment, bundle)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Handle submit action if needed
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle text change and perform search
                searchList(newText)
                return true
            }
        })

        return binding.root
    }

    private fun searchList(text: String) {
        val searchList = ArrayList<RoomCard>()
        for (dataClass in dataList) {
            if (dataClass.title?.contains(text, ignoreCase = true) == true ||
                dataClass.description?.contains(text, ignoreCase = true) == true ||
                dataClass.city?.contains(text, ignoreCase = true) == true) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }

    private fun addToFavorites(roomCard: RoomCard) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(currentUser.uid)
            val key = favoritesRef.push().key
            key?.let {
                favoritesRef.child(it).setValue(roomCard)
            }
        }
    }

    private fun removeFromFavorites(roomCard: RoomCard) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(currentUser.uid)
            favoritesRef.orderByChild("title").equalTo(roomCard.title).addListenerForSingleValueEvent(object :
                ValueEventListener {
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
            databaseReference.removeEventListener(it)
        }
    }

    override fun onAddFavorite(roomCard: RoomCard) {
        addToFavorites(roomCard)
    }

    override fun onRemoveFavorite(roomCard: RoomCard) {
        removeFromFavorites(roomCard)
    }
}
