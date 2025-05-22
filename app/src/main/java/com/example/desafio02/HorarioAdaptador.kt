package com.example.desafio02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HorarioAdaptador(private val listaMaterias: List<Materia>) :
    RecyclerView.Adapter<HorarioAdaptador.HorarioViewHolder>() {

    inner class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreMateria: TextView = itemView.findViewById(R.id.tvNombreMateria)
        val dia: TextView = itemView.findViewById(R.id.tvDia)
        val hora: TextView = itemView.findViewById(R.id.tvHora)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horario, parent, false)
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val materia = listaMaterias[position]
        holder.nombreMateria.text = materia.Nombre_materia
        holder.dia.text = materia.Dia
        holder.hora.text = materia.Hora
    }

    override fun getItemCount(): Int = listaMaterias.size
}

