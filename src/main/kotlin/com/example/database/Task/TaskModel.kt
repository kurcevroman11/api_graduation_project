package com.example.db.Task

import com.example.database.Description.DescriptionForTask
import com.example.database.file.FileDTO
import com.example.database.file.FileForTask.autoIncrement
import com.example.database.file.FileForTask.entityId
import com.example.database.file.FileForTask.nullable
import com.example.db.Description.DescriptionDTO
import com.example.db.Description.DescriptionModel
import com.example.db.Task.TaskModel.autoIncrement
import com.example.db.Task.TaskModel.nullable
import com.example.db.UserRoleProject.UserRoleProjectModel
import io.ktor.http.*
import mu.KotlinLogging
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.io.File


object TaskModel : Table("task") {

    private val id = TaskModel.integer("id").autoIncrement()
    private val name = TaskModel.varchar("name", 64)
    private val status = TaskModel.integer("status").nullable()
    private val start_date = TaskModel.datetime("start_data").autoIncrement()
    private val scope = TaskModel.integer("score").nullable()
    private val description = TaskModel.integer("descriptionid").nullable()
    private val parent = TaskModel.integer("parent").nullable()
    private val userCount: Int = 0

    fun insert(taskDTO: TaskDTO) {
        transaction {

            addLogger(StdOutSqlLogger)

            TaskModel.insert {
                it[name] = taskDTO.name
                it[status] = taskDTO.status
                it[scope] = taskDTO.scope
                it[parent] = taskDTO.parent
                it[description] = taskDTO.description
            }
        }
    }

    fun getProjectAll(): List<TaskDTO> {
        return try {
            transaction {
                TaskModel.select { TaskModel.parent.isNull() }.map {
                    TaskDTO(
                        it[TaskModel.id],
                        it[name],
                        it[status],
                        dateTimeToString(it[start_date]?.toDateTime()!!),
                        it[scope],
                        it[description],
                        it[parent],
                    )
                }
            }
        } catch (e: Exception) {
            ArrayList<TaskDTO>()
        }
    }

    fun getTaskAll(): List<TaskDTO> {
        return try {
            transaction {
                TaskModel.selectAll().map {
                    TaskDTO(
                        it[TaskModel.id],
                        it[name],
                        it[status],
                        dateTimeToString(it[start_date]?.toDateTime()!!),
                        it[scope],
                        it[description],
                        it[parent],

                    )
                }
            }
        } catch (e: Exception) {
            ArrayList<TaskDTO>()
        }
    }

    fun getTask(id: Int): TaskDTO? {
        return try {
            transaction {
                val taskModle = TaskModel.select { TaskModel.id.eq(id) }.single()
                TaskDTO(
                    id = taskModle[TaskModel.id],
                    name = taskModle[name],
                    status = taskModle[status],
                    start_date = dateTimeToString(taskModle[start_date]?.toDateTime()!!),
                    scope = taskModle[scope],
                    description = taskModle[description],
                    parent = taskModle[parent],
                )
            }
        } catch (e: Exception) {
            TaskDTO()
        }
    }

    fun updateTask(id: Int, taskDTO: TaskDTO): HttpStatusCode {

        transaction {
            val task = TaskModel.update({ TaskModel.id eq (id) })
            {
                it[name] = taskDTO.name
                it[status] = taskDTO.status
                it[scope] = taskDTO.scope
                it[description] = taskDTO.description
                it[parent] = taskDTO.parent
            }
            if (task > 0) {
                return@transaction HttpStatusCode.NoContent
            } else {
                return@transaction "Task with ID $id not found."
            }
        }
        return HttpStatusCode.OK
    }

    fun deleteTaskInternal(id: Int): Int {
        var m_task = getTask(id)
        val deletedRowCount = TaskModel.deleteWhere { TaskModel.id eq id }
        DescriptionModel.deletDescription(m_task?.description)
        UserRoleProjectModel.deleteURPByTask(id)

        val tasks = getTaskAll()
        for (task in tasks) {
            var parent_id = task.parent
            if (parent_id != null && parent_id == id) {
                deleteTaskInternal(task.id!!)
            }
        }
        return deletedRowCount
    }

    fun deletTask(id: Int): HttpStatusCode {
        if (id != null) {
            transaction {
                val deletedRowCount = deleteTaskInternal(id)

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

fun dateTimeToString(dateTime: DateTime): String {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormat.forPattern(pattern)
    return formatter.print(dateTime)
}

fun stringToDateTime(dateString: String): DateTime {
    val formatter =  DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = formatter.parseDateTime(dateString)
    return dateTime
}


object TaskForId: IdTable<Long>("task") {
    override val id: Column<EntityID<Long>> = TaskForId.long("id").autoIncrement().entityId()
    private val name = TaskForId.varchar("name", 64)
    private val status = TaskForId.integer("status").nullable()
    private val start_date = TaskForId.datetime("start_data").autoIncrement()
    private val scope = TaskForId.integer("score").nullable()
    private val description = TaskForId.integer("descriptionid").nullable()
    private val parent = TaskForId.integer("parent").nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    fun insertandGetIdTask(taskDTO: TaskDTO): Long {
        var newTaskId: Long = 0
        transaction {
            addLogger(StdOutSqlLogger)

            newTaskId = TaskForId.insertAndGetId {
                it[name] = taskDTO.name
                it[status] = taskDTO.status
                it[scope] = taskDTO.scope
                it[description] = taskDTO.description
                it[parent] = taskDTO.parent
            }.value
        }
        return newTaskId
    }
}


