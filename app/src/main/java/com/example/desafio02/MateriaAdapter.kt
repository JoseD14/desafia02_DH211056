package com.example.desafio02

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio02.TareaAdapter.TareaViewHolder

class MateriaAdapter (
    private val listaMateria: MutableList<Materia>,
    private val onRetirarClick: (Materia) -> Unit
): RecyclerView.Adapter<MateriaAdapter.MateriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_materia, parent, false)
        return MateriaViewHolder(view)
    }
    override fun onBindViewHolder(holder: MateriaViewHolder, position: Int) {
        val materia = listaMateria[position]
        holder.tvNombreMateria.text = "Materia: ${materia.Nombre_materia}"
        holder.tvCodigo.text = "Código: ${materia.Codigo}"
        holder.tvGrupo.text = "Grupo: ${materia.Grupo}"
        holder.tvDia.text = "Día: ${materia.Dia}"
        holder.tvHora.text = "Hora: ${materia.Hora}"
        holder.tvUbicacion.text = "Ubicacion: ${materia.Aula}"
        holder.tvDocente.text = "Docente: ${materia.Nombre_profesor}"



        holder.btnEliminar.setOnClickListener {
            onRetirarClick(materia)  // Llama a la función que se le paso antes
        }
    }
    override fun getItemCount(): Int = listaMateria.size
    fun eliminarMateria(materia: Materia) {
        val index = listaMateria.indexOfFirst { it.id == materia.id }
        if (index != -1) {
            listaMateria.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class MateriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreMateria: TextView = itemView.findViewById(R.id.tvNombreMateria)
        val tvCodigo: TextView = itemView.findViewById(R.id.tvCodigo)
        val tvGrupo: TextView = itemView.findViewById(R.id.tvGrupo)
        val tvDia: TextView = itemView.findViewById(R.id.tvDia)
        val tvHora: TextView = itemView.findViewById(R.id.tvHora)
        val tvUbicacion: TextView = itemView.findViewById(R.id.tvUbicacion)
        val tvDocente: TextView = itemView.findViewById(R.id.tvDocente)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)

    }

}