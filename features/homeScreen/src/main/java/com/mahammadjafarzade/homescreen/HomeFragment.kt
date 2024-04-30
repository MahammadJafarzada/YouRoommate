package com.mahammadjafarzade.homescreen

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mahammadjafarzade.entities.model.RoomCard
import com.mahammadjafarzade.homescreen.databinding.FragmentHomeBinding
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    var databaseReference: DatabaseReference? = null
    private lateinit var dataList: ArrayList<RoomCard>
    private lateinit var adapter: HomeScreenAdapter
    var eventListener : ValueEventListener ?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        dataList = ArrayList()
        adapter = HomeScreenAdapter(this.requireContext(),dataList)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvRoom.layoutManager = gridLayoutManager
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar)
        val dialog = builder.create()
        dialog.show()

        binding.rvRoom.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("Room card")
        dialog.show()

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val roomdata = itemSnapshot.getValue(RoomCard::class.java)
                    if (roomdata != null) {
                        dataList.add(roomdata)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }

        })
        binding.search.setOnQueryTextListener(  object : SearchView.OnQueryTextListener {
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
    fun searchList(text: String) {
        val searchList = java.util.ArrayList<RoomCard>()
        for (dataClass in dataList) {
            if (dataClass.title?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }
}