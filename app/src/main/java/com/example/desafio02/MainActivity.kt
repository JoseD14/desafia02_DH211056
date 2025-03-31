package com.example.desafio02

import android.app.DownloadManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio02.datos.Tarea
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    var consultaOrdenada: Query = refTareas.orderByChild("nombre")
    var tareas: MutableList<Tarea> = mutableListOf()
    lateinit var listaTareas: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializar()
    }

    private fun inicializar() {
        val fab_agregar: ExtendedFloatingActionButton = findViewById(R.id.fab_add)
        listaTareas = findViewById(R.id.ListaTareas)

        listaTareas.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long
            ) {
                val intent = Intent(getBaseContext(), AddTareaActivity::class.java)
                intent.putExtra("accion", "e")
                intent.putExtra("key", tareas[i].key)
                intent.putExtra("nombre", tareas[i].nombre)
                intent.putExtra("descripcion", tareas[i].descripcion)
                intent.putExtra("estado", tareas[i].estado)
                intent.putExtra("fecha", tareas[i].fecha)
                startActivity(intent)
            }
        })

        listaTareas.setOnItemLongClickListener(object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                l: Long
            ): Boolean {
                val ad = AlertDialog.Builder(this@MainActivity)
                ad.setMessage("Está seguro que desea eliminar la tarea?")
                    .setTitle("Confirmación")
                    .setPositiveButton("Sí") { dialog, id ->
                        tareas[position].key?.let {
                            refTareas.child(it).removeValue()
                        }
                        Toast.makeText(
                            this@MainActivity,
                            "Tarea eliminada", Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        Toast.makeText(
                            this@MainActivity,
                            "No se ha eliminado la tarea", Toast.LENGTH_SHORT
                        ).show()
                    }
                ad.show()
                return true
            }
        })

        fab_agregar.setOnClickListener {
            val i = Intent(getBaseContext(), AddTareaActivity::class.java)
            i.putExtra("accion", "a")
            i.putExtra("key", "")
            i.putExtra("nombre", "")
            i.putExtra("descripcion", "")
            i.putExtra("estado", "")
            i.putExtra("fecha", "")
            startActivity(i)
        }

        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tareas.clear()
                for (dato in snapshot.children) {
                    val tarea: Tarea? = dato.getValue(Tarea::class.java)
                    tarea?.key = dato.key
                    tarea?.let { tareas.add(it) }
                }
                val adapter = AdaptadorTarea(
                    this@MainActivity,
                    tareas as ArrayList<Tarea>
                )
                listaTareas.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refTareas: DatabaseReference = database.getReference("tareas")
    }
}
