package com.example.db.Task

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object TaskModel : Table("task"){
    private  val id = TaskModel.integer("id").autoIncrement().primaryKey()
    private  val name = TaskModel.varchar("Name", 64)
    private  val status = TaskModel.integer("Status")
    private  val start_date = TaskModel.varchar("Start_data",64)
    private  val scope = TaskModel.varchar("Score",64)
    private  val description = TaskModel.integer("DescriptionID").nullable()
    private  val parent = TaskModel.integer("Parent").nullable()
    private  val generathon = TaskModel.integer("Generation").nullable()
    private  val comments = TaskModel.integer("CommentsID").nullable()

fun  insert(taskDTO: TaskDTO){
    transaction {

        addLogger(StdOutSqlLogger)

        TaskModel.insert{
            it[name] = taskDTO.name
            it[status] = taskDTO.status
            it[start_date] = taskDTO.start_date
            it[scope] = taskDTO.scope
            it[description] = taskDTO.descriptio
            it[parent] = taskDTO.parent
            it[generathon] = taskDTO.generathon
            it[comments] = taskDTO.comments
        }


    }
}
    fun fetchTask(id:Int):TaskDTO{

        lateinit var taskDTO: TaskDTO

        transaction {
            val taskModle = TaskModel.select { TaskModel.id.eq(id) }.single()
            taskDTO = TaskDTO(
                id = taskModle[TaskModel.id],
                name = taskModle[name],
                status = taskModle[TaskModel.status],
                start_date = taskModle[start_date],
                scope = taskModle[scope],
                descriptio = taskModle[description],
                parent = taskModle[parent],
                generathon = taskModle[generathon],
                comments = taskModle[comments]
            )
        }
        return taskDTO
    }
}