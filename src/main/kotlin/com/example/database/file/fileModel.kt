package com.example.database.file



import com.example.database.file.FileModel.autoIncrement
import com.example.database.file.FileModel.nullable
import com.example.db.Description.DescriptionDTO
import com.example.db.Task.TaskDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.db.Task.TaskModel
import com.example.db.Task.dateTimeToString
import io.ktor.http.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger


object FileModel : Table("file") {

    private val id = FileModel.integer("id").autoIncrement()
    private val taskId = FileModel.integer("taskId").nullable()
    private val orig_filename = FileModel.text("orig_filename").nullable()
    private val type = FileModel.text("type").nullable()

//    fun insert(fileDTO: FileDTO) {
//        transaction {
//
//            addLogger(StdOutSqlLogger)
//
//            FileModel.insert {
//                it[orig_filename] = fileDTO.orig_filename
//                it[taskId] = fileDTO.taskId
//                it[type] = fileDTO.type
//            }
//        }
//    }


    fun getFileInTask(id: Int): List<FileDTO>? {
        return try {
            transaction {
                FileModel.select { taskId.eq(id) }.map {
                    FileDTO(
                    it[FileModel.id],
                    it[orig_filename],
                    it[FileModel.taskId],
                    it[FileModel.type])  }


            }
        } catch (e: Exception) {
           ArrayList<FileDTO>()
        }
    }


    fun getFile(id: Int): FileDTO? {
        return try {
            transaction {
                val fileModel = FileModel.select { FileModel.id.eq(id) }.single()
                FileDTO(
                    id = fileModel[FileModel.id],
                    orig_filename = fileModel[orig_filename],
                    taskId = fileModel[FileModel.taskId],
                    type = fileModel[FileModel.type])

            }
        } catch (e: Exception) {
            FileDTO(null,null,null,null)
        }
    }

    fun deletFile(id: Int): HttpStatusCode {
        if (id != null) {
            transaction {
                val deletedRowCount = FileModel.deleteWhere { FileModel.id eq id }
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




object FileForTask: IdTable<Long>("file") {
    override val id: Column<EntityID<Long>> = FileForTask.long("id").autoIncrement().entityId()

    private val orig_filename = FileForTask.text("orig_filename").nullable()
    private val taskId = FileForTask.integer("taskId").nullable()
    private val type = FileForTask.text("type").nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    fun insertandGetId(fileDTO: FileDTO): Long {
        var newFileId: Long = 0
        transaction {
            addLogger(StdOutSqlLogger)

            newFileId = FileForTask.insertAndGetId {
                it[orig_filename] = fileDTO.orig_filename
                it[taskId] = fileDTO.taskId
                it[type] = fileDTO.type
            }.value
        }
        return newFileId
    }
}