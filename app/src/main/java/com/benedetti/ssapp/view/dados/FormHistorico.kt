package com.benedetti.ssapp.view.dados

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benedetti.ssapp.R
import com.benedetti.ssapp.adapter.AdapterHistorico
import com.benedetti.ssapp.databinding.ActivityHistoricoBinding
import com.benedetti.ssapp.model.Dados
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FormHistorico : AppCompatActivity() {
    private lateinit var binding: ActivityHistoricoBinding
    private val db = FirebaseFirestore.getInstance()
    private val _todosHistoricos = MutableStateFlow<MutableList<Dados>>(mutableListOf())
    private val todosHistoricos: StateFlow<MutableList<Dados>> = _todosHistoricos


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView_historico = findViewById<RecyclerView>(R.id.recyclerViewHistorico)
        recyclerView_historico.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView_historico.setHasFixedSize(true)


        val listaHistorico: MutableList<Dados> = mutableListOf()
        /*val adapterHistorico =AdapterHistorico(this, listaHistorico)
         recyclerView_historico.adapter = adapterHistorico*/




        fun recuperarHistorico(): Flow<MutableList<Dados>> {
            recuperDados()?.let {
                db.collection("Marcacoes")
                    .get().addOnCompleteListener { querySnapshot ->
                        if (querySnapshot.isSuccessful) {
                            for (document in querySnapshot.result) {
                                val lista = document.toObject(Dados::class.java)
                                listaHistorico.add(lista)
                                _todosHistoricos.value = listaHistorico

                            }
                        }

                    }
            }
            return todosHistoricos
        }
    }

    private fun recuperDados(): String? {
        val uid = intent.getStringExtra("uid")
        return uid
    }
}