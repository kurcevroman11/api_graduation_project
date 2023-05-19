package com.example.db.Description


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
object DescriptionModel: Table("descriptions") {

    private  val id = DescriptionModel.integer("id").autoIncrement().primaryKey()
    private  val content = DescriptionModel.varchar("content", 64)
    private  val file_resources = DescriptionModel.byte("File_resources").nullable()
    private  val photo_resources = DescriptionModel.byte("Photo_resources").nullable()
    private  val video_resources = DescriptionModel.byte("Video_resources").nullable()

    fun  insert(descriptionDTO: DescriptionDTO){

        transaction {
            addLogger(StdOutSqlLogger)


            DescriptionModel.insert{
            it[content] = descriptionDTO.content
            it[file_resources] = descriptionDTO.file_resources
            it[photo_resources] = descriptionDTO.photo_resources
            it[video_resources] = descriptionDTO.video_resources
            }


        }
    }
    fun fetchTask(id:Int): DescriptionDTO {

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


}