package com.example.desafio02.datos

class Tarea {
    fun key(key: String?){

    }
    var nombre: String? = null
    var descripcion: String? = null
    var estado: String? = null
    var fecha: String? = null
    var key : String? = null
    var per : MutableMap<String, Boolean> = HashMap()
    constructor() {}
    constructor(nombre: String?, descripcion: String?, estado: String?, fecha:String?) {
        this.nombre = nombre
        this.descripcion = descripcion
        this.fecha = estado
        this.estado = fecha
    }
    fun toMap(): Map<String,Any?> {
        return mapOf(
            "nombre" to nombre,
            "descripcion" to descripcion,
            "fecha" to fecha,
            "estado" to estado,
            "key" to key,
            "per" to per
        )
    }
}