package com.example.db.Task

import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime

import org.jetbrains.exposed.sql.transactions.transaction


object TaskModel : Table("task"){

    private  val id = TaskModel.integer("id").autoIncrement()
    private  val name = TaskModel.varchar("name", 64)
    private  val status = TaskModel.integer("status").nullable()
    private  val start_date = TaskModel.datetime("start_data").nullable()
    private  val scope = TaskModel.datetime("score").nullable()
    private  val description = TaskModel.integer("descriptionid").nullable()
    private  val parent = TaskModel.integer("parent").nullable()
    private  val generathon = TaskModel.integer("generation").nullable()
    private  val comments = TaskModel.integer("commentsid").nullable()

fun  insert(taskDTO: TaskDTO){
    transaction {

        addLogger(StdOutSqlLogger)

        TaskModel.insert{
            it[name] = taskDTO.name
            it[status] = taskDTO.status
            it[start_date] = taskDTO.start_date?.toDateTime()
            it[scope] = taskDTO.scope?.toDateTime()
            it[description] = taskDTO.description
            it[parent] = taskDTO.parent
            it[generathon] = taskDTO.generathon
            it[comments] = taskDTO.comments
        }

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
                        it[start_date]?.toDateTime(),
                        it[scope]?.toDateTime(),
                        it[description],
                        it[parent],
                        it[generathon],
                        it[comments]
                    )
                }
            }
        }catch (e: Exception) {
            ArrayList<TaskDTO>()
        }!!
}



    fun getTask(id:Int): TaskDTO? {
        return try{
            transaction {
                val taskModle = TaskModel.select { TaskModel.id.eq(id) }.single()
                TaskDTO(
                id = taskModle[TaskModel.id],
                name = taskModle[name],
                status = taskModle[status],
                start_date = taskModle[start_date]?.toDateTime(),
                scope = taskModle[scope]?.toDateTime(),
                description = taskModle[description],
                parent = taskModle[parent],
                generathon = taskModle[generathon],
                comments = taskModle[comments]
            )
        }
        }
    catch (e: Exception) {
        TaskDTO()
    }
    }

    fun updateTask(id: Int, taskDTO: TaskDTO): HttpStatusCode {

            transaction {
                val task = TaskModel.update({ TaskModel.id eq (id) })
                {
                    it[name] = taskDTO.name
                    it[status] = taskDTO.status
                    it[start_date] = taskDTO.start_date?.toDateTime()
                    it[scope] = taskDTO.scope?.toDateTime()
                    it[description] = taskDTO.description
                    it[parent] = taskDTO.parent
                    it[generathon] = taskDTO.generathon
                    it[comments] = taskDTO.comments
                }
                if (task > 0) {
                    return@transaction HttpStatusCode.NoContent
                } else {
                    return@transaction "Task with ID $id not found."
                }
            }
  return HttpStatusCode.OK
}

fun deletTask(id: Int): HttpStatusCode {
    if (id != null) {
        transaction {
            val deletedRowCount = TaskModel.deleteWhere { TaskModel.id eq id }
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





//    fun get(id:Int): Unit?{
//
//        return try {
//
//        transaction {
//            val taskModle = TaskModel.select { TaskModel.id.eq(id) }.single()
//            var taskDTO = TaskDTO(
//                id = taskModle[TaskModel.id],
//                name = taskModle[name],
//                status = taskModle[status],
//                start_date = taskModle[start_date],
//                scope = taskModle[scope],
//                descriptio = taskModle[description],
//                parent = taskModle[parent],
//                generathon = taskModle[generathon],
//                comments = taskModle[comments]
//            )
//        }
//        }catch (e: Exception) {
//            null
//        }
//
//    }
