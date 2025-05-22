package com.example.desafio02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ApunteArchivoAdapter(
    private val archivos: MutableList<File>,
    private val onItemClick: (File) -> Unit
) : RecyclerView.Adapter<ApunteArchivoAdapter.ArchivoViewHolder>() {


    class ArchivoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreArchivo: TextView = itemView.findViewById(R.id.txtNombreArchivo)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchivoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_apunte, parent, false)
        return ArchivoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArchivoViewHolder, position: Int) {
        val file = archivos[position]
        holder.nombreArchivo.text = file.name
        holder.itemView.setOnClickListener {
            onItemClick(file)
        }
        holder.btnEliminar.setOnClickListener {
            archivos.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, archivos.size)
        }

    }

    override fun getItemCount(): Int = archivos.size
}
