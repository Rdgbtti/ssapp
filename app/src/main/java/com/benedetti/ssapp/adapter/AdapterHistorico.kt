package com.benedetti.ssapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.benedetti.ssapp.databinding.ListaDadosBinding
import com.benedetti.ssapp.databinding.MenuItemBinding
import com.benedetti.ssapp.model.Dados
import com.benedetti.ssapp.model.Menu

class AdapterHistorico(private val context: Context, private val listaHistorico:MutableList<Dados>):
    RecyclerView.Adapter<AdapterHistorico.HistoricoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder  {
        val itemLista = ListaDadosBinding.inflate(LayoutInflater.from(context),parent,false)
        return HistoricoViewHolder(itemLista)
    }


    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        holder.data.text = listaHistorico[position].data
        holder.hora.text = listaHistorico[position].hora
        holder.dadoUm.text = listaHistorico[position].dadoUm
        holder.dadoDois.text = listaHistorico[position].dadoDois


        val dados: Dados = listaHistorico.get(position)

       /* holder.btnMenu.setOnClickListener {
            clickMenu.clickMenu(menu)
        }*/
    }

    override fun getItemCount() = listaHistorico.size

    /*interface ClickMenu{
        fun clickMenu(menu: Menu)
    }*/

    inner class HistoricoViewHolder(binding: ListaDadosBinding): RecyclerView.ViewHolder(binding.root) {
        val data = binding.txtData
        val hora = binding.txtHora
        val dadoUm = binding.txtDadoUm
        val dadoDois = binding.txtDadoDois
        val botaoEditar = binding.btnEditar
        val botaoExcluir = binding.btnExcluir
    }
}