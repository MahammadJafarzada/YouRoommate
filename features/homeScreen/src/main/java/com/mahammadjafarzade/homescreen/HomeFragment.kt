package com.mahammadjafarzade.homescreen

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
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
import com.mahammadjafarzade.entities.model.UserData
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

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showExitDialog()
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvRoom.layoutManager = gridLayoutManager
        binding.rvRoom.adapter = adapter
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Room card")
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser


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


        if (currentUser != null) {
            val uid = currentUser.uid
            databaseReference = FirebaseDatabase.getInstance().getReference("Room card")
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

            // Fetch user data separately
            val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val userData = dataSnapshot.getValue(UserData::class.java)
                        userData?.let {
                            displayUserName(it)
                        }
                    } else {
                        // Do nothing if user data doesn't exist
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Do nothing in case of error
                }
            })
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

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showExitDialog()
        }

        return binding.root
    }

    private fun displayUserName(user: UserData){
        binding.userName.text = user.fullName
    }

    private fun searchList(text: String) {
        val searchList = ArrayList<RoomCard>()
        for (dataClass in dataList) {
            if (dataClass.title?.contains(text, ignoreCase = true) == true ||
                dataClass.city?.contains(text, ignoreCase = true) == true) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }

    private fun showExitDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to exit the app?")
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

    override fun onAddFavorite(roomCard: RoomCard) {
        addToFavorites(roomCard)
    }

    override fun onRemoveFavorite(roomCard: RoomCard) {
        removeFromFavorites(roomCard)
    }

    private fun addToFavorites(roomCard: RoomCard) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(currentUser.uid)
            favoritesRef.orderByChild("id").equalTo(roomCard.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        val key = favoritesRef.push().key
                        key?.let {
                            favoritesRef.child(it).setValue(roomCard)
                            // Update UI to indicate addition to favorites
                            // For example, show a message or change UI state
                            Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    private fun removeFromFavorites(roomCard: RoomCard) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(currentUser.uid)
            favoritesRef.orderByChild("id").equalTo(roomCard.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        childSnapshot.ref.removeValue()
                        // Update UI to indicate removal from favorites
                        // For example, show a message or change UI state
                        Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }
    override fun onResume() {
        super.onResume()
        eventListener?.let {
            databaseReference.addValueEventListener(it)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        eventListener?.let {
            databaseReference.removeEventListener(it)
        }
    }
}
