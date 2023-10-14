package com.benedetti.ssapp.view.cadastro

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log


import com.benedetti.ssapp.databinding.ActivityFormCadastroBinding
import com.benedetti.ssapp.view.login.FormLogin
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class FormCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityFormCadastroBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btVoltar.setOnClickListener {
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
        }


        binding.btCadastrar.setOnClickListener { view ->
            val nome = binding.editNomeCompleto.text.toString()
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar =
                    Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {
                            val uid = auth.currentUser?.uid.toString()
                            val snackbar = Snackbar.make(
                                view,
                                "Sucesso ao cadastrar usuário!",
                                Snackbar.LENGTH_SHORT)
                                snackbar.setBackgroundTint(Color.BLUE)
                                snackbar.show()
                                binding.editNomeCompleto.setText("")
                                binding.editEmail.setText("")
                                binding.editSenha.setText("")

                            val usuatiosMap = hashMapOf(
                                "uid" to uid,
                                "nome" to nome,
                                "E-mail" to email,
                                "senha" to senha
                            )
                            db.collection("Usuários").document(uid)
                                .set(usuatiosMap).addOnCompleteListener {
                                    Log.d("db_create", "Sucesso ao salvar os dados do usuário!")
                                }.addOnFailureListener {

                                }
                        }
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
        }
    }
}