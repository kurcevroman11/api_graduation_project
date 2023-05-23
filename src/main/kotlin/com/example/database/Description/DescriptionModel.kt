package com.example.db.Description

import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.*
import java.nio.file.Files
import javax.imageio.ImageIO

object DescriptionModel: Table("description") {

    private val id = DescriptionModel.integer("id").autoIncrement().primaryKey()
    private val content = DescriptionModel.varchar("content", 64)
    private val file_resources = DescriptionModel.binary("file_resources").nullable()
    private val photo_resources = DescriptionModel.binary("photo_resources").nullable()
    private val video_resources = DescriptionModel.binary("video_resources").nullable()

    fun insertDescription(descriptionDTO: DescriptionDTO) {

        transaction {
            addLogger(StdOutSqlLogger)


            DescriptionModel.insert {
                it[content] = descriptionDTO.content
                it[file_resources] = descriptionDTO.file_resources
                it[photo_resources] = descriptionDTO.photo_resources
                it[video_resources] = descriptionDTO.video_resources
            }


        }
    }

    fun imageToByteArray(imageFile: File): ByteArray {
        val inputStream: InputStream = imageFile.inputStream()
        return inputStream.readBytes()
    }

    fun byteArrayToImage(imageBytes: ByteArray, outputFilePath: String) {
        val inputStream = ByteArrayInputStream(imageBytes)
        val image = ImageIO.read(inputStream)
        val outputFile = File(outputFilePath)
        ImageIO.write(image, "jpg", outputFile)
    }

    fun fileToByteArray(filePath: String): ByteArray? {
        val file = File(filePath)
        if (!file.exists()) {
            println("Файл не существует: $filePath")
            return null
        }

        try {
            return Files.readAllBytes(file.toPath())
        } catch (e: Exception) {
            println("Ошибка при чтении файла: ${e.message}")
            return null
        }
    }

    fun videoToByteArray(filePath: String): ByteArray? {
        try {
            val file = File(filePath)
            val inputStream = FileInputStream(file)
            val outputStream = ByteArrayOutputStream()

            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            inputStream.close()
            outputStream.close()

            return outputStream.toByteArray()
        } catch (e: Exception) {
            println("Ошибка при чтении видео: ${e.message}")
            return null
        }
    }

    fun byteArrayToFile(byteArray: ByteArray, filePath: String): Boolean {
        try {
            val file = File(filePath)
            val outputStream = FileOutputStream(file)
            outputStream.write(byteArray)
            outputStream.close()
            return true
        } catch (e: Exception) {
            println("Ошибка при записи файла: ${e.message}")
            return false
        }
    }

    fun getDescription(id: Int): DescriptionDTO {

        lateinit var descriptionDTO: DescriptionDTO

        transaction {
            val descriptionModel = DescriptionModel.select { DescriptionModel.id.eq(id) }.single()
            descriptionDTO = DescriptionDTO(
                id = descriptionModel[DescriptionModel.id],
                content = descriptionModel[DescriptionModel.content],
                file_resources = descriptionModel[DescriptionModel.file_resources],
                photo_resources = descriptionModel[DescriptionModel.photo_resources],
                video_resources = descriptionModel[DescriptionModel.video_resources],

                )
        }
        return descriptionDTO
    }


    fun getDescriptionAll(): List<DescriptionDTO> {

        return transaction {
            DescriptionModel.selectAll().map {
                DescriptionDTO(
                    id = it[DescriptionModel.id],
                    content = it[DescriptionModel.content],
                    file_resources = it[DescriptionModel.file_resources],
                    photo_resources = it[DescriptionModel.photo_resources],
                    video_resources = it[DescriptionModel.video_resources],

                    )
            }
        }


    }

    fun updateDescription(id: Int, descriptionDTO: DescriptionDTO): HttpStatusCode {

        transaction {
            val task = DescriptionModel.update({ DescriptionModel.id eq (id) })
            {
                it[content] = descriptionDTO.content
                it[file_resources] = descriptionDTO.file_resources
                it[photo_resources] = descriptionDTO.photo_resources
                it[video_resources] = descriptionDTO.video_resources
            }
            if (task > 0) {
                return@transaction HttpStatusCode.NoContent
            } else {
                return@transaction "Task with ID $id not found."
            }
        }
        return HttpStatusCode.OK
    }

    fun deletDescription(id: Int): HttpStatusCode {
        if (id != null) {
            transaction {
                val deletedRowCount = DescriptionModel.deleteWhere { DescriptionModel.id eq id }
                if (deletedRowCount > 0) {
                    return@transaction HttpStatusCode.NoContent
                } else {
                    return@transaction HttpStatusCode.NoContent
                }
            }
        } else {
            return HttpStatusCode.BadRequest
        }
        return HttpStatusCode.OK
    }
}
