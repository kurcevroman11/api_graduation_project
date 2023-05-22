package com.example.db.comments

import com.example.db.Description.DescriptionDTO
import com.example.db.Description.DescriptionModel
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
object CommentModel : Table("comments"){
    private  val id = CommentModel.integer("id").autoIncrement().primaryKey()
    private  val user = CommentModel.integer("usser").nullable()
    private  val comments = CommentModel.varchar("comments", 256)


    fun  insertComment(сommentDTO: CommentDTO){
        transaction {

            addLogger(StdOutSqlLogger)

            CommentModel.insert{
                it[user] = сommentDTO.user
                it[comments] = сommentDTO.comments
            }

        }
    }

    fun getComment(id:Int):CommentDTO{

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




    fun getCommentAll(): List<CommentDTO> {

        return transaction {
            CommentModel.selectAll().map {
                CommentDTO(
                    id = it[CommentModel.id],
                    user = it[CommentModel.user],
                    comments = it[CommentModel.comments]
                )
            }
        }


    }

    fun updateComment(id: Int, commentDTO: CommentDTO): HttpStatusCode {

        transaction {
            val task = CommentModel.update({ CommentModel.id eq (id) })
            {
                it[user] = commentDTO.user
                it[comments] = commentDTO.comments
            }
            if (task > 0) {
                return@transaction HttpStatusCode.NoContent
            } else {
                return@transaction "Task with ID $id not found."
            }
        }
        return HttpStatusCode.OK
    }

    fun deletComment(id: Int): HttpStatusCode {
        if (id != null) {
            transaction {
                val deletedRowCount = CommentModel.deleteWhere { CommentModel.id eq id }
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
