package com.mahammadjafarzade.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mahammadjafarzade.common.util.toMain
import com.mahammadjafarzade.common.util.toRegister
import com.mahammadjafarzade.entities.model.UserData
import com.mahammadjafarzade.login.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private lateinit var  firebaseDatabase : FirebaseDatabase
    private lateinit var  databaseReference : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        binding.registerButton.setOnClickListener {
            findNavController().toRegister()
        }
        binding.loginButton.setOnClickListener {
            val loginEmail = binding.userMailEditText.text.toString()
            val logPassword = binding.passwordEditText.text.toString()
            if(loginEmail.isNotEmpty() && logPassword.isNotEmpty()){
                loginUser(loginEmail, logPassword)
            }else{
                Toast.makeText(
                    requireContext(),
                    "All fields mandatory",
                    Toast.LENGTH_SHORT
                ).show()
            }
            findNavController().toMain()
        }
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")


        return binding.root
    }
    private fun loginUser(userEmail : String,userPassword : String){
        databaseReference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for(userSnapshot in dataSnapshot.children){
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if(userData != null && userData.password == userPassword){
                            Toast.makeText(
                                requireContext(),
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().toMain()
                        }
                        return
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Login failed ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}