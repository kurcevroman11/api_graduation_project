package com.example.db.comments

import com.example.db.Task.TaskModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
object CommentModel : Table("comments"){
    private  val id = CommentModel.integer("id").autoIncrement().primaryKey()
    private  val user = CommentModel.integer("User").nullable()
    private  val comments = CommentModel.varchar("Comments", 256)


    fun  insert(сommentDTO: CommentDTO){
        transaction {

            addLogger(StdOutSqlLogger)

            CommentModel.insert{
                it[user] = сommentDTO.user
                it[comments] = сommentDTO.comments
            }

        }
    }

    fun fetchTask(id:Int):CommentDTO{

        lateinit var commentDTO: CommentDTO

        transaction {
            val commentModel = CommentModel.select { CommentModel.id.eq(id)}.single()
            commentDTO = CommentDTO(
                id = commentModel[CommentModel.id],
                user = commentModel[CommentModel.user],
                comments = commentModel[CommentModel.comments]
            )
        }
        return commentDTO
    }
}
