package com.example.desafio02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio02.Event
import com.example.desafio02.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarioFragmento : Fragment() {

    private lateinit var recyclerEventos: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_calendario_fragmento, container, false)
        recyclerEventos = view.findViewById(R.id.rvEvents)
        recyclerEventos.layoutManager = LinearLayoutManager(requireContext())

        cargarEventos()

        return view
    }

    private fun cargarEventos() {
        RetrofitClient.api.getEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    val eventos = response.body() ?: emptyList()
                    recyclerEventos.adapter = CalendarioAdaptador(eventos)
                } else {
                    Toast.makeText(requireContext(), "Error al obtener eventos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
