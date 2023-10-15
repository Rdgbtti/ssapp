package com.benedetti.ssapp.view.perfil

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.widget.TextView
import coil.load
import coil.transform.CircleCropTransformation
import com.benedetti.ssapp.R
import com.benedetti.ssapp.databinding.ActivityFormMeuPerfilBinding
import com.benedetti.ssapp.view.home.TelaPrincipal
import com.benedetti.ssapp.view.login.FormLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class FormMeuPerfil : AppCompatActivity() {
    private lateinit var binding: ActivityFormMeuPerfilBinding
    private val db = FirebaseFirestore.getInstance()
    private val CAMERA_REQUEST_CODE = 1
    private var uri_Imagem: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormMeuPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btVoltar.setOnClickListener {
            val intent = Intent(this, TelaPrincipal::class.java)
            startActivity(intent)
        }

        binding.btDeslogar.setOnClickListener{
            val mensagem :String? = "Tem certeza de que deseja sair?"
            exibirCaixaDeDialogo(mensagem)
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
        binding.txtAlterarSenha.setOnClickListener {
            db.collection("Usuários").document("Analista1")
                .update("nome","variavelNova").addOnCompleteListener {
                    Log.d("db_update","Sucesso ao atualizar os dados do usuário")
                }
        }

        binding.Avatar.setOnClickListener {
            cameraCheckPermission()
        }

    }

    private fun exibirCaixaDeDialogo(mensagem: String?) {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.activity_caixa_dialogo)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val txtMensagem: TextView = dialog.findViewById(R.id.txtMensagem)
            val btnSim: TextView = dialog.findViewById(R.id.btnSim)
            val btnNao: TextView = dialog.findViewById(R.id.btnNao)

            txtMensagem.text = mensagem

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


    private fun recuperDados(): String? {
        val uid = intent.getStringExtra("uid")
        return uid
    }

    private fun cameraCheckPermission(){

        Dexter.withContext(this)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA
                ).withListener(

                object: MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                       report?.let{
                           if(report.areAllPermissionsGranted()){
                               camera()
                           }
                       }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRorationalDialogForPermiossion()
                    }

                }
            ).onSameThread().check()
    }
    private fun camera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){

            when(requestCode){
                CAMERA_REQUEST_CODE->{
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.Avatar.load(bitmap){
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }

    }

    private fun showRorationalDialogForPermiossion(){
        AlertDialog.Builder(this)
            .setMessage("Parece que você desativou as permissões" +
            "necessário para esse recurso. Ele pode ser habilitado em Configurações do aplicativo!!!")
            .setPositiveButton("Go To settings"){_,_->
                try {
                    val intent =Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    val uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)

                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
        }
            .setNegativeButton("CANCELAR"){dialog, _->
                dialog.dismiss()
            }.show()
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

}