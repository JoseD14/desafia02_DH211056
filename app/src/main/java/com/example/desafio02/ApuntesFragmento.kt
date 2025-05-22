package com.example.desafio02

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio02.utils.FileUtils.getMimeType
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ApuntesFragmento : Fragment() {

    private lateinit var btnSubir: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ApunteArchivoAdapter

    private val apuntes = mutableListOf<File>()
    private val SELECT_FILE_REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_apuntes_fragmento, container, false)
        btnSubir = view.findViewById(R.id.btnSubirApunte)
        recyclerView = view.findViewById(R.id.rvApuntes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ApunteArchivoAdapter(apuntes) { file ->
            openFile(requireContext(), file)
        }
        recyclerView.adapter = adapter

        btnSubir.setOnClickListener {
            abrirSelectorDeArchivos()
        }

        cargarApuntesLocales()

        return view
    }

    private fun abrirSelectorDeArchivos() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*" // permite cualquier tipo de archivo
        startActivityForResult(intent, SELECT_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val file = guardarArchivoEnLocal(uri)
                if (file != null) {
                    apuntes.add(file)
                    adapter.notifyItemInserted(apuntes.size - 1)
                }
            }
        }
    }

    private fun guardarArchivoEnLocal(uri: Uri): File? {
        val context = requireContext()
        val contentResolver = context.contentResolver

        val fileName = uri.lastPathSegment?.split("/")?.last() ?: "apunte_${System.currentTimeMillis()}"
        val inputStream = contentResolver.openInputStream(uri)
        val dir = File(context.filesDir, "apuntes")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, fileName)

        return try {
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun cargarApuntesLocales() {
        val dir = File(requireContext().filesDir, "apuntes")
        if (dir.exists()) {
            val archivos = dir.listFiles()
            archivos?.let {
                apuntes.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun openFile(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val mime = getMimeType(file)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, mime)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No hay una app para abrir este archivo", Toast.LENGTH_SHORT).show()
        }
    }
}
