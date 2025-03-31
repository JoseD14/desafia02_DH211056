package com.example.desafio02

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.constraintlayout.motion.widget.KeyPosition
import com.example.desafio02.datos.Tarea

class AdaptadorTarea(private val context: Activity, var tareas: List<Tarea>) : ArrayAdapter<Tarea?>(context, R.layout.activity_main, tareas){
    override fun getView(position : Int, view: View?, parent: ViewGroup): View {
        val layoutInflater=context.layoutInflater
        var rowview: View? = null
        rowview = view ?: layoutInflater.inflate(R.layout.tarea_layout,null)
        val tvNombre = rowview!!.findViewById<TextView>(R.id.tvNombre)
        val tvDescripcion = rowview!!.findViewById<TextView>(R.id.tvDescripcion)
        val tvEstado = rowview!!.findViewById<TextView>(R.id.tvEstado)
        val tvFecha = rowview!!.findViewById<TextView>(R.id.tvFecha)
        tvNombre.text = "Nombre : " + tareas[position].nombre
        tvDescripcion.text = "Descripcion : " + tareas[position].descripcion
        tvEstado.text = "Estado : " + tareas[position].estado
        tvFecha.text = "Fecha : " + tareas[position].fecha
        return rowview

    }
}
