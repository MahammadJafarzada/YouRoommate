package com.mahammadjafarzade.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.mahammadjafarzade.common.util.toLogin
import com.mahammadjafarzade.common.util.toMain
import com.mahammadjafarzade.common.util.toRegister
import com.mahammadjafarzade.entities.model.UserData
import com.mahammadjafarzade.register.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding : FragmentRegisterBinding

    private lateinit var  firebaseDatabase : FirebaseDatabase
    private lateinit var  databaseReference : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.registerButton.setOnClickListener {
            val userFullName = binding.edtName.text.toString()
            val userEmail = binding.edtEmail.text.toString()
            val userNumber = binding.edtnumber.text.toString()
            val userPassword = binding.edtPassword.text.toString()
            if(userFullName.isNotEmpty() && userEmail.isNotEmpty() && userNumber.isNotEmpty() && userPassword.isNotEmpty()){
                registerUser(userFullName,userEmail,userNumber,userPassword)
            }else{
                Toast.makeText(
                    requireContext(),
                    "All fields mandatory",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }
    private fun registerUser( fullName : String, email : String,number : String,password : String){
        databaseReference.orderByChild("fullName").equalTo(fullName).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!dataSnapshot.exists()){
                    val id = databaseReference.push().key
                    val userData =  UserData(id,fullName,email, number, password)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(
                        requireContext(),
                        "Register Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().toMain()
                }else{
                    Toast.makeText(
                        requireContext(),
                        "User already exists",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Register Unsuccessful ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}