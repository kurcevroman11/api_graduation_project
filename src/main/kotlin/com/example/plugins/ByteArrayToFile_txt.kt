package com.example.plugins

import java.io.File
import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import java.lang.reflect.Type

data class Resource(val file_resources: ByteArray)

fun main(args: Array<String>) {
    // ���������� �������������� ��� �������������� ������� ����� � ������ ������
    val byteArrayDeserializer = JsonDeserializer<ByteArray> {
            json, typeOfT, context ->
        json.asJsonArray.map { it.asInt.toByte() }.toByteArray()
    }

    // ������������ �������������� ��� ���� ByteArray
    val gson = Gson().newBuilder().registerTypeAdapter(
        ByteArray::class.java, byteArrayDeserializer
    ).create()

    // ������ JSON
    val jsonString = "{\"file_resources\":[72,105,44,32,109,101,105,110,32,70,117,104,114,101,114,33,33,33]}"
    val resource = gson.fromJson(jsonString, Resource::class.java)

    // ���������� ������ ������ � ����
    File("file.pdf").writeBytes(resource.file_resources)
}