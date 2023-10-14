package com.benedetti.ssapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.benedetti.ssapp.databinding.MenuItemBinding
import com.benedetti.ssapp.model.Menu

class AdapterMenu(private val context: Context,private val listaMenu:MutableList<Menu>, var clickMenu: ClickMenu):
    RecyclerView.Adapter<AdapterMenu.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemLista = MenuItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return MenuViewHolder(itemLista)
    }


    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.iconeMenu.setBackgroundResource(listaMenu[position].icone!!)
        holder.txtTituloMenu.text = listaMenu[position].titulo
        holder.btnMenu.text = listaMenu[position].nomeBotao

        val menu: Menu = listaMenu.get(position)

        holder.btnMenu.setOnClickListener {
            clickMenu.clickMenu(menu)
        }
    }

    override fun getItemCount() = listaMenu.size

    interface ClickMenu{

        fun clickMenu(menu: Menu)
    }

    inner class MenuViewHolder(binding: MenuItemBinding): RecyclerView.ViewHolder(binding.root) {
        val iconeMenu = binding.iconMenu
        val txtTituloMenu = binding.txtTituloMenu
        val btnMenu = binding.btnMenu
    }
}