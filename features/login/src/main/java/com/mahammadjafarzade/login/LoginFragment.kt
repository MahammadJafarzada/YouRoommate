package com.mahammadjafarzade.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var auth: FirebaseAuth
    private var callbackManager: CallbackManager = CallbackManager.Factory.create()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()

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
        }
        binding.loginButtonfb.setOnClickListener {
            loginFacebook()
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

    //Login with Facebook
    fun loginFacebook() {
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d("TAG", "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("TAG", "facebook:onError", error)
                }
            },
        )

        LoginManager.getInstance().logInWithReadPermissions(this, callbackManager, PERMISSIONS)
    }

    val PERMISSIONS = listOf("public_profile", "email")

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    findNavController().toMain()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }
    //Login with google
}