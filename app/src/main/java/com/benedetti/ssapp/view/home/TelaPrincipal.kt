package com.benedetti.ssapp.view.home

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.Window
import android.widget.TextView
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
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerMenu = binding.recyclerIconeMenu
        recyclerMenu.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerMenu.setHasFixedSize(true)
        adapterMenu = AdapterMenu(this, listaMenu,this)
        recyclerMenu.adapter = adapterMenu
        listaIconeMenu()

        binding.btDeslogar.setOnClickListener{
            val mensagem :String? = "Tem certeza de que deseja sair?"
            exibirCaixaDeDialogo(mensagem)
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

        val icone2 = Menu(R.drawable.ic_pressao, "Pressão \n arterial","Pressão")
        listaMenu.add(icone2)

        val icone3 = Menu(R.drawable.ic_peso, "Peso \n corporal","Peso")
        listaMenu.add(icone3)

        val icone4 = Menu(R.drawable.ic_person, "Perfil \n","Clique")
        listaMenu.add(icone4)

        val icone5 = Menu(R.drawable.ic_compartilhar, "Compartilhar \n", "Clique")
        listaMenu.add(icone5)

        val icone6 = Menu(R.drawable.ic_logout, "Sair \n","Clique")
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

     fun exibirCaixaDeDialogo(message: String?){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.activity_caixa_dialogo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtMensagem: TextView = dialog.findViewById(R.id.txtMensagem)
        val btnSim: TextView = dialog.findViewById(R.id.btnSim)
        val btnNao: TextView = dialog.findViewById(R.id.btnNao)

        txtMensagem.text = message

        btnSim.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val voltarTelaLogin = Intent(this,FormLogin::class.java)
            startActivity(voltarTelaLogin)
            finish()
        }
        btnNao.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}