package com.benedetti.ssapp.view.dados

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benedetti.ssapp.databinding.ActivityFormAdicionarDadosBinding
import com.google.firebase.firestore.FirebaseFirestore

class FormAdicionarDados : AppCompatActivity() {

    private lateinit var binding: ActivityFormAdicionarDadosBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




    }
}