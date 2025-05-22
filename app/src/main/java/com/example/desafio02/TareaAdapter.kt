package com.example.desafio02

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class TareaAdapter(
    private val listaTareas: MutableList<Tarea>,
    private val onEliminarClick: (Tarea) -> Unit  // Pasa la tarea a que se elimine
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = listaTareas[position]
        holder.tvTitulo.text = tarea.titulo
        holder.tvMateria.text = "Materia: ${tarea.materia}"
        holder.tvFechaEntrega.text = "Fecha entrega: ${tarea.fechaEntrega}"
        holder.tvNota.text = "Nota: ${tarea.nota}"
        holder.tvEstado.text = "Estado: ${tarea.estado}"

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(tarea)  // Llama a la función que se le paso antes
        }
    }

    override fun getItemCount(): Int = listaTareas.size

    // Función para actualizar la lista internamente
    fun eliminarTarea(tarea: Tarea) {
        val index = listaTareas.indexOfFirst { it.id == tarea.id }
        if (index != -1) {
            listaTareas.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvMateria: TextView = itemView.findViewById(R.id.tvMateria)
        val tvFechaEntrega: TextView = itemView.findViewById(R.id.tvFechaEntrega)
        val tvNota: TextView = itemView.findViewById(R.id.tvNota)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)

    }
}
