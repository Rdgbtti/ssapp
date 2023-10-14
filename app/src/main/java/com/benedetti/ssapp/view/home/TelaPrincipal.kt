package com.benedetti.ssapp.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.benedetti.ssapp.R
import com.benedetti.ssapp.adapter.AdapterMenu
import com.benedetti.ssapp.databinding.ActivityTelaPrincipalBinding
import com.benedetti.ssapp.model.Menu
import com.benedetti.ssapp.view.dados.FormDadosPrincipal
import com.benedetti.ssapp.view.login.FormLogin
import com.benedetti.ssapp.view.perfil.FormMeuPerfil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaPrincipal : AppCompatActivity(), AdapterMenu.ClickMenu {
    private lateinit var  binding: ActivityTelaPrincipalBinding
    private val db = FirebaseFirestore.getInstance()
    private val usuarioAtual = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var adapterMenu: AdapterMenu
    private val listaMenu: MutableList<Menu> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerMenu = binding.recyclerIconeMenu
        recyclerMenu.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerMenu.setHasFixedSize(true)
        adapterMenu = AdapterMenu(this, listaMenu,this)
        recyclerMenu.adapter = adapterMenu
        listaIconeMenu()

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
    private fun listaIconeMenu(){
        val icone1 = Menu(R.drawable.baseline_water_drop_24, "Açucar \n no sangue", "Diabetes")
        listaMenu.add(icone1)

        val icone2 = Menu(R.drawable.baseline_water_drop_24, "Pressão \n arterial","Pressão")
        listaMenu.add(icone2)

        val icone3 = Menu(R.drawable.baseline_water_drop_24, "Peso \n corporal","Peso")
        listaMenu.add(icone3)

        val icone4 = Menu(R.drawable.ic_person, "Perfil \n","Clique")
        listaMenu.add(icone4)

        val icone5 = Menu(R.drawable.baseline_water_drop_24, "Compartilhar \n", "Clique")
        listaMenu.add(icone5)

        val icone6 = Menu(R.drawable.baseline_water_drop_24, "Sair \n","Clique")
        listaMenu.add(icone6)
    }
    override fun clickMenu(menu: Menu) {
        val intent = Intent(this, FormDadosPrincipal::class.java)
        val botao = menu.nomeBotao.toString()
        intent.putExtra("segmento",menu.nomeBotao.toString() );
        startActivity(intent)
    }

    private fun IrParaPerfil(){
        val intent = Intent(this, FormMeuPerfil::class.java)
        intent.putExtra("uid", usuarioAtual);
        startActivity(intent)
    }
}