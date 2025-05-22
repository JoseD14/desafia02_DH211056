package com.example.desafio02
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PendienteAdaptador(private val listaTarea: List<Tarea>) :
    RecyclerView.Adapter<PendienteAdaptador.PendienteViewHolder>() {

    inner class PendienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo : TextView = itemView.findViewById(R.id.tvTitulo)
        val nombreMateria: TextView = itemView.findViewById(R.id.tvMateria)
        val fecha: TextView = itemView.findViewById(R.id.tvFechaEntrega)
        val estado: TextView = itemView.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pendiente, parent, false)
        return PendienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendienteViewHolder, position: Int) {
        val tarea = listaTarea[position]
        holder.titulo.text = tarea.titulo
        holder.nombreMateria.text = tarea.materia
        holder.fecha.text = tarea.fechaEntrega
        holder.estado.text = tarea.estado
    }

    override fun getItemCount(): Int = listaTarea.size
}
