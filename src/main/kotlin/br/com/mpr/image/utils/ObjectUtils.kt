package br.com.mpr.image.utils

import com.google.gson.GsonBuilder
import java.lang.reflect.Type


private val g = GsonBuilder().setDateFormat("dd/MM/yyyy").create()

fun <T> toObject(json: String, clazz: Class<T>): T {
    return g.fromJson(json, clazz)
}

fun <T> toObject(json: String, type: Type): T {
    return g.fromJson(json, type)
}