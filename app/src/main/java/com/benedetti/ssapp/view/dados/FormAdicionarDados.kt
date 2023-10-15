package com.benedetti.ssapp.view.dados

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.benedetti.ssapp.databinding.ActivityFormAdicionarDadosBinding
import com.benedetti.ssapp.databinding.ActivityFormDadosPrincipalBinding
import com.benedetti.ssapp.view.home.TelaPrincipal
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

import com.google.firebase.firestore.FirebaseFirestore

class FormAdicionarDados : AppCompatActivity() {

    private lateinit var binding: ActivityFormAdicionarDadosBinding
    private val auth = FirebaseAuth.getInstance()
    private val usuarioAtual = FirebaseAuth.getInstance().currentUser?.uid
    val uid = auth.currentUser?.uid.toString()

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAdicionarDadosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btVoltar.setOnClickListener {
            val intent = Intent(this, FormDadosPrincipal::class.java)
            intent.putExtra("segmento", recuperDados());
            startActivity(intent)
        }

        binding.btCadastrar.setOnClickListener { view ->
            val data = binding.txtData.text.toString()
            val hora = binding.txtHora.text.toString()
            val dadoUm = binding.txtdadoUm.text.toString()
            val dadoDois = binding.txtdadoDois.text.toString()

            if (dadoUm.isEmpty() || dadoDois.isEmpty()) {
                val snackbar =
                    Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                val snackbar = Snackbar.make(
                    view,
                    "Sucesso ao cadastrar usuário!",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.setBackgroundTint(Color.BLUE)
                snackbar.show()
                binding.txtdadoUm.setText("")
                binding.txtdadoDois.setText("")

                val usuatiosMap = hashMapOf(
                    "data" to data,
                    "hora" to hora,
                    "dadoUm" to dadoUm,
                    "dadoDois" to dadoDois
                )
                db.collection("Marcacoes").document(uid)
                    .set(usuatiosMap).addOnCompleteListener {
                        Log.d("db_create_marcacao", "Sucesso ao salvar os dados do usuário!")
                    }.addOnFailureListener {

                    }.addOnFailureListener { exception ->
                        val mensagemErro = when (exception) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres!"
                            is FirebaseAuthInvalidCredentialsException -> "Digite um email válido"
                            is FirebaseAuthUserCollisionException -> "Usuário já cadastrado"
                            is FirebaseNetworkException -> "Sem conexão com a internet!(1014)"
                            else -> " Erro ao cadastrar o usuário. Tente novamente"
                        }
                        val snackbar = Snackbar.make(view, mensagemErro, Snackbar.LENGTH_LONG)
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.show()

                    }
            }


            if (usuarioAtual != null) {
                db.collection("Usuários").document(usuarioAtual)
                    .addSnapshotListener { dadosUsuario, error ->
                        if (dadosUsuario != null) {
                            binding.txtUser.text = "Olá " + dadosUsuario.getString("nome")
                        }
                    }
            }

            recuperDados()?.let {
                db.collection("segmento").document(it)
                    .addSnapshotListener{dadosSegmento, error ->
                        if(dadosSegmento !=null){
                            binding.fraseEfeito.text = dadosSegmento.getString("frase_efeito")

                        }
                    }
            }

        }
    }

    private fun recuperDados(): String? {
        val segmento = intent.getStringExtra("segmento")
        return segmento
    }
}
