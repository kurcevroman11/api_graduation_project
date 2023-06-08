package com.example.db.Description

import com.google.gson.Gson
import io.ktor.http.*
import mu.KotlinLogging
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

private val logger = KotlinLogging.logger {}
object DescriptionModel: Table("description") {

    private val id = DescriptionModel.integer("id").autoIncrement()
    private val content = DescriptionModel.varchar("content", 64).nullable()
    private val file_resources = DescriptionModel.text("file_resources").nullable()
    private val photo_resources = DescriptionModel.text("photo_resources").nullable()
    private val video_resources = DescriptionModel.text("video_resources").nullable()

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


    fun readImegeString(phat : String): MutableList<String> {

        val imegeList = mutableListOf<String>()

        Files.walk(Paths.get(phat))
            .filter { Files.isRegularFile(it) }
            .forEach {
                imegeList.add(it.toString())
            }



        return imegeList
    }

    fun readImegeByte(phat : String): MutableList<photoClass> {


        val imegeList = mutableListOf<String>()

        Files.walk(Paths.get(phat))
            .filter { Files.isRegularFile(it) }
            .forEach {
                imegeList.add(it.toString())
            }

        val imegeByte = mutableListOf<photoClass>()

        for (imege in  imegeList)
        {

            val fileName = imege.substringAfterLast("\\")
            val name = fileName.substringBeforeLast(".")
            val extension = fileName.substringAfterLast(".")
            logger.info { "Фото: $imege, Name:$name, Type: $extension" }

            imegeByte.add(photoClass(name, extension,imageToByteArray(imege)))
        }
        logger.info { "Список фото отправлен в обработчик" }
        return imegeByte
    }

    fun writeImegeByte(imegeByte :  MutableList<photoClass>, phat : String)
    {
        val imegeByteFile = readImegeByte(phat)

        imegeByteFile.addAll(imegeByte)

        val folder = File(phat)
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                println("Фаил уже существует создан")
            }
            else
            {
                println("Фаил создан")
            }
        }

        for (image in imegeByteFile)
        {

            val imagePath = phat + "${image.filename}.${image.filetype}"

            byteArrayToImage(image.photo, imagePath, image.filetype!!)
        }
    }

    fun readFileByte(phat : String): MutableList<File> {
        val fileList = mutableListOf<File>()

        Files.walk(Paths.get(phat))
            .filter { Files.isRegularFile(it) }
            .forEach {
                fileList.add(File(it.toString()))
            }



        logger.info { "Список файлов отправлен в обработчик" }
        return fileList
    }
//
//    fun writeFileByte(imegeByte :  MutableList<fileClass>, phat : String)
//    {
//        val fileByteFile = readFileByte(phat)
//
//        fileByteFile.addAll(imegeByte)
//
//        val folder = File(phat)
//        if (!folder.exists()) {
//            if (!folder.mkdirs()) {
//                println("Фаил уже существует создан")
//            }
//            else
//            {
//                println("Фаил создан")
//            }
//        }
//
//        for ((index, file) in fileByteFile.withIndex())
//        {
//            val imagePath = phat + "${index + 1}_"+"${file.filename}.${file.filetype}"
//
//            //C:\Users\sergk\OneDrive\Рабочий стол\estimate-backend-dev_SergeyKalinin/1_file.txt
//
//            byteArrayToImage(file.file, imagePath, file.filetype!!)
//        }
//    }

    fun imageToByteArray(imagePath: String): ByteArray {
        val imageFile = File(imagePath)
        val inputStream: InputStream = imageFile.inputStream()
        return inputStream.readBytes()
    }

    fun byteArrayToImage(imageBytes: ByteArray, outputFilePath: String, typePhoto : String) {
        val inputStream = ByteArrayInputStream(imageBytes)
        val image = ImageIO.read(inputStream)
        val outputFile = File(outputFilePath)
        ImageIO.write(image, typePhoto, outputFile)
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
