package com.example.foodorderapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.FragmentAccountBinding
import com.example.foodorderapp.utils.navigate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var binding: FragmentAccountBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_account, container, false)
        binding.accountFragment = this
        auth = FirebaseAuth.getInstance()
        binding.userEmail = auth.currentUser?.email.toString()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)


        return binding.root
    }

    fun logOutOnClick(view: View){

        Snackbar.make(view,"Are you sure you want to log out ?", Snackbar.LENGTH_LONG)
            .setAction("Yes"){
                auth.signOut()
                googleSignInClient.signOut()
                Navigation.navigate(view,R.id.action_accountFragment_to_signInFragment)
            }.show()

    }
}