import com.example.db.Task.TaskDTO
import com.example.db.comments.CommentDTO
import com.example.db.comments.CommentModel.deletComment
import com.example.db.comments.CommentModel.getComment
import com.example.db.comments.CommentModel.getCommentAll
import com.example.db.comments.CommentModel.insertComment
import com.example.db.comments.CommentModel.updateComment
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.CommentContriller() {
    routing {
        route("/comment") {
            get {
                val commentDTO = getCommentAll()
                val gson = Gson()

                val comment = gson.toJson(commentDTO)

                call.respond(comment)
            }

            get("/{id}") {
                val commentId = call.parameters["id"]?.toIntOrNull()
                if (commentId != null) {
                    val commentDTO = getComment(commentId)
                    call.respond(commentDTO!!)
                }else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }

            post {
                val comment = call.receive<String>()
                val gson = Gson()

                val name = gson.fromJson(comment, CommentDTO::class.java)
                insertComment(name)
                call.respond(HttpStatusCode.Created)
            }

            put("/{id}") {

                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null) {
                    val task = call.receive<String>()
                    val gson = Gson()

                    val commentDTO = gson.fromJson(task, CommentDTO::class.java)
                    call.respond(updateComment(id, commentDTO))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }

            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null) {
                    call.respond(deletComment(id), "Delete")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }


            }

        }
    }
}