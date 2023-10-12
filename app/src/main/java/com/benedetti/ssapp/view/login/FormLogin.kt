package com.benedetti.ssapp.view.login

import android.content.Intent
import android.graphics.Color

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.benedetti.ssapp.FormEsqueceuSenha
import com.google.android.material.snackbar.Snackbar
import com.benedetti.ssapp.databinding.ActivityFormLoginBinding
import com.benedetti.ssapp.view.cadastro.FormCadastro
import com.benedetti.ssapp.view.home.TelaPrincipal
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class FormLogin : ComponentActivity() {

    private lateinit var binding: ActivityFormLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btEntrar.setOnClickListener {view ->
            
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            
            if(email.isEmpty() || senha.isEmpty()){
                val snackbar = Snackbar.make(view,"",Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }else{
                auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener{autenticacao ->
                    if(autenticacao.isSuccessful){
                        navegarHome()
                    }
                }.addOnFailureListener{exception ->
                    val mensagemErro = when(exception){
                        is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres!"
                        is FirebaseAuthInvalidCredentialsException -> "Digite um email válido"
                        is FirebaseNetworkException -> "Sem conexão com a internet!(1014)"
                        else -> " Erro ao fazer o login do usuário!"
                    }
                    val snackbar = Snackbar.make(view,mensagemErro,Snackbar.LENGTH_LONG)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()

                }

            }
        }

        binding.btCriarConta.setOnClickListener{
            val intent = Intent(this,FormCadastro::class.java)
            startActivity(intent)
        }

        binding.btEsqueceuSenha.setOnClickListener{
            val intent = Intent(this,FormEsqueceuSenha::class.java)
            startActivity(intent)
        }

    }


    private fun navegarHome(){
        val intent = Intent(this,TelaPrincipal::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if(usuarioAtual != null){
            navegarHome()
        }
    }


}