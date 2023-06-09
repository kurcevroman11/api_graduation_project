package com.example.plugins

import com.example.database.Description.DescriptionForTask
import com.example.db.Description.DescriptionDTO
import com.example.db.Task.TaskModel
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}
fun createMedia(name:String): Long {



    var filePath = "src\\main\\resources\\media\\${name}\\"


    val file = File(filePath)
    if (!file.exists()) {
        if (!file.mkdirs()) {
            logger.info { "Фаил file уже существует создан" }
        }
        else
        {
            logger.info { "Фаил file создан" }
        }
    }


    val discritionID = DescriptionForTask.insertandGetId(DescriptionDTO(null, null, filePath,null ,null))

    return discritionID

}
