package com.example.database.Description

import com.example.db.Description.DescriptionDTO
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

object DescriptionForTask: IdTable<Long>("description") {
    override val id: Column<EntityID<Long>> = DescriptionForTask.long("id").autoIncrement().entityId()

    private val content = DescriptionForTask.varchar("content", 64)
    private val file_resources = DescriptionForTask.varchar("file_resources", 64).nullable()
    private val photo_resources = DescriptionForTask.varchar("photo_resources", 64).nullable()
    private val video_resources = DescriptionForTask.varchar("video_resources", 64).nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    fun insertandGetId(descriptionDTO: DescriptionDTO): Long {
        var newDescriptionId: Long = 0
        transaction {
            addLogger(StdOutSqlLogger)

            newDescriptionId = DescriptionForTask.insertAndGetId {
                it[content] = descriptionDTO.content
                it[file_resources] = descriptionDTO.file_resources
                it[photo_resources] = descriptionDTO.photo_resources
                it[video_resources] = descriptionDTO.video_resources
            }.value
        }
        return newDescriptionId
    }
}