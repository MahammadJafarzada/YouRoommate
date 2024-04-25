package com.mahammadjafarzade.addcard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mahammadjafarzade.addcard.databinding.FragmentAddCardBinding

class AddCardFragment : Fragment() {
    private lateinit var binding : FragmentAddCardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCardBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

}