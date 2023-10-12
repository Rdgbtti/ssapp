package com.benedetti.ssapp.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benedetti.ssapp.databinding.ActivityTelaPrincipalBinding
import com.benedetti.ssapp.view.login.FormLogin
import com.benedetti.ssapp.view.perfil.FormMeuPerfil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaPrincipal : AppCompatActivity() {
    private lateinit var  binding: ActivityTelaPrincipalBinding
    private val db = FirebaseFirestore.getInstance()
    private val usuarioAtual = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btDeslogar.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val voltarTelaLogin = Intent(this,FormLogin::class.java)
            startActivity(voltarTelaLogin)
            finish()

            }
        binding.btPerfil.setOnClickListener {
            IrParaPerfil()
        }

        if (usuarioAtual != null) {
            db.collection("Usuários").document(usuarioAtual)
                .addSnapshotListener{dadosUsuario, error ->
                    if(dadosUsuario !=null){
                        binding.txtUser.text = "Olá " + dadosUsuario.getString("nome")
                    }
                }
        }
        db.collection("Informações").document("7vLYlOUsjEKcpUpFwI0G")
            .addSnapshotListener{dadosInformacoes, error ->
                if(dadosInformacoes !=null){
                    binding.fraseFixaTelaPrincipal.text =dadosInformacoes.getString("frase_fixa_tela_principal")
                    binding.campanhaBannerDiabetes.text =dadosInformacoes.getString("campanha_banner_diabetes")
                }
            }
    }
    private fun IrParaPerfil(){
        val intent = Intent(this, FormMeuPerfil::class.java)
        intent.putExtra("uid", usuarioAtual);
        startActivity(intent)
    }

}