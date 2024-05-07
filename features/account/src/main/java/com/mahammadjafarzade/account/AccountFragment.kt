package com.mahammadjafarzade.account

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mahammadjafarzade.account.databinding.FragmentAccountBinding
import com.mahammadjafarzade.common.util.toLogin
import com.mahammadjafarzade.entities.model.UserData
import dagger.hilt.android.AndroidEntryPoint

// AccountFragment.kt
@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        if (uid != null) {
            databaseReference.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val userData = dataSnapshot.getValue(UserData::class.java)
                        if (userData != null) {
                            displayUserData(userData)
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
        binding.signOutBtn.setOnClickListener {
            signOut()
        }
        binding.editButton.setOnClickListener {
            editProfile()
        }
        return binding.root
    }

    private fun displayUserData(user: UserData) {
        binding.profileName.text = user.fullName
        binding.profileNumber.text = user.number
        binding.profileEmail.text = user.email
    }

    private fun editProfile() {
        val currentUser = auth.currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            databaseReference.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val userData = dataSnapshot.getValue(UserData::class.java)
                        if (userData != null) {
                            val dialogView = layoutInflater.inflate(R.layout.fragment_account, null)
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setView(dialogView)

                            val editProfileName = dialogView.findViewById<EditText>(R.id.profileName)
                            val editProfileNumber = dialogView.findViewById<EditText>(R.id.profileNumber)
                            val editProfileEmail = dialogView.findViewById<EditText>(R.id.profileEmail)

                            editProfileName.setText(userData.fullName)
                            editProfileNumber.setText(userData.number)
                            editProfileEmail.setText(userData.email)

                            dialogBuilder.setTitle("Edit Profile")
                                .setPositiveButton("Save") { _, _ ->
                                    val newFullName = editProfileName.text.toString()
                                    val newNumber = editProfileNumber.text.toString()
                                    val newEmail = editProfileEmail.text.toString()

                                    val updatedUser = UserData(newFullName, newNumber, newEmail)

                                    databaseReference.child("users").child(uid).setValue(updatedUser)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                                displayUserData(updatedUser)
                                            } else {
                                                Toast.makeText(requireContext(), "An error occurred while updating the profile. Please try again.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }
                                .setNegativeButton("Cancel") { dialog, _ ->
                                    dialog.dismiss()
                                }

                            val alertDialog = dialogBuilder.create()
                            alertDialog.show()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Do nothing in case of error
                }
            })
        }
    }

    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        Log.d("SignOut", "User signed out")
        findNavController().toLogin()
    }
}
