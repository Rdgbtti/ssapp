package com.benedetti.ssapp.view.dados

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.benedetti.ssapp.R
import com.benedetti.ssapp.databinding.ActivityFormDadosPrincipalBinding
import com.benedetti.ssapp.model.Menu
import com.benedetti.ssapp.view.home.TelaPrincipal
import com.benedetti.ssapp.view.perfil.FormMeuPerfil
import com.google.firebase.firestore.FirebaseFirestore

class FormDadosPrincipal : AppCompatActivity() {
    private lateinit var binding: ActivityFormDadosPrincipalBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormDadosPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btVoltar.setOnClickListener {
            val intent = Intent(this, TelaPrincipal::class.java)
            startActivity(intent)
        }

        binding.btnHistorico.setOnClickListener {
            val intent = Intent(this, FormHistorico::class.java)
            startActivity(intent)
        }

        binding.btnAdicionar.setOnClickListener {
            clicarBotaoAdicionar()
        }

        recuperDados()?.let {
            db.collection("segmento").document(it)
                .addSnapshotListener{dadosSegmento, error ->
                    if(dadosSegmento !=null){
                        binding.nomeSegmento.text = dadosSegmento.getString("nome")
                        binding.tituloSegmento. text = dadosSegmento.getString("titulo")
                        binding.descricaosegmento.text = dadosSegmento.getString("descricao")
                    }
                }
        }
    }
    private fun recuperDados(): String? {
        val segmento = intent.getStringExtra("segmento")
        return segmento
    }
    fun clicarBotaoAdicionar() {
        val intent = Intent(this, FormAdicionarDados::class.java)
        intent.putExtra("segmento",recuperDados());
        startActivity(intent)
    }
}