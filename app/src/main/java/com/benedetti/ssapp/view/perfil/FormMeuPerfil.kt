package com.benedetti.ssapp.view.perfil

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.benedetti.ssapp.R
import com.benedetti.ssapp.databinding.ActivityFormMeuPerfilBinding
import com.benedetti.ssapp.view.home.TelaPrincipal
import com.benedetti.ssapp.view.login.FormLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class FormMeuPerfil : AppCompatActivity() {
    private lateinit var binding: ActivityFormMeuPerfilBinding
    private val db = FirebaseFirestore.getInstance()
    private var uri_Imagem: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormMeuPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btVoltar.setOnClickListener {
            val intent = Intent(this, TelaPrincipal::class.java)
            startActivity(intent)
        }


        recuperDados()?.let {
            db.collection("Usuários").document(it)
                .addSnapshotListener{dadosUsuario, error ->
                    if(dadosUsuario !=null){
                        binding.txtNomeCompleto.text = dadosUsuario.getString("nome")
                        binding.txtEmail.text = dadosUsuario.getString("E-mail")
                        binding.txtSenha.text = dadosUsuario.getString("senha")
                    }
                }
        }
        /*binding.btAlterarSenha.setOnClickListener {
            db.collection("Usuários").document("Analista1")
                .update("nome","variavelNova").addOnCompleteListener {
                    Log.d("db_update","Sucesso ao atualizar os dados do usuário")
                }
        }*/
    }
    private fun recuperDados(): String? {
        val uid = intent.getStringExtra("uid")
        return uid
    }

    private fun capturaFoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    }

}