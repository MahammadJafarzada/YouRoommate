package com.mahammadjafarzade.homescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mahammadjafarzade.entities.model.RoomCard
import com.mahammadjafarzade.homescreen.databinding.FragmentRoomCardDetailBinding

class RoomCardDetailFragment : Fragment() {
    private lateinit var binding: FragmentRoomCardDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomCardDetailBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            backButton()
        }
        val number = arguments?.getInt("Number")

        val city = arguments?.getString("City")
        val price = arguments?.getDouble("Price")
        val title = arguments?.getString("Title")
        val description = arguments?.getString("Description")
        val image = arguments?.getString("Image")

        number?.let { binding.detailNumber.text = it.toString() }
        city?.let { binding.detailCity.text = it }
        price?.let { binding.detailPrice.text = it.toString() }
        title?.let { binding.detailTitle.text = it }
        description?.let { binding.detailDesc.text = it }
        image?.let { Glide.with(this).load(it).into(binding.detailImage) }

        return binding.root
    }
    private fun backButton() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}