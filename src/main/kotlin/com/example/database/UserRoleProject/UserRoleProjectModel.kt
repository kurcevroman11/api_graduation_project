package com.example.db.UserRoleProject

import com.example.database.UserRoleProject.UserRoleProjectDTO
import com.example.db.Task.TaskDTO
import com.example.db.Task.TaskModel
import com.google.gson.GsonBuilder
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object UserRoleProjectModel: Table("usersroleproject"){
    private  val id = UserRoleProjectModel.integer("id").autoIncrement()
    private  val users = UserRoleProjectModel.integer("userid").nullable()
    private  val role = UserRoleProjectModel.integer("roleid").nullable()
    private  val task = UserRoleProjectModel.integer("projectid")
    private val type_of_activity = UserRoleProjectModel.integer("type_of_activityid").nullable()
    private val score = UserRoleProjectModel.integer("score").nullable()

    fun  insert(userRoleProjectDTO: UserRoleProjectDTO){
        transaction {
            addLogger(StdOutSqlLogger)
            UserRoleProjectModel.insert{
                it[users] = userRoleProjectDTO.users
                it[role] = userRoleProjectDTO.role
                it[task] = userRoleProjectDTO.task
                it[type_of_activity] = userRoleProjectDTO.type_of_activity
                it[score] = userRoleProjectDTO.score
            }
        }
    }

    fun getURPAll(): List<UserRoleProjectDTO> {
        return transaction {
            UserRoleProjectModel.selectAll().map {
                UserRoleProjectDTO(
                    it[UserRoleProjectModel.id],
                    it[users],
                    it[role],
                    it[task],
                    it[type_of_activity],
                    it[score]
                )
            }
        }
    }

    fun getALLUserProject(id: Int) : MutableList<UserRoleProjectDTO>?
    {
        return transaction {
            exec(" SELECT * FROM usersroleproject WHERE projectid = $id;") { rs ->
                val list = mutableListOf<UserRoleProjectDTO>()
                while (rs.next()) {
                    list.add(UserRoleProjectDTO(
                        rs.getInt("id"),
                        rs.getInt("userid"),
                        rs.getInt("roleid"),
                        rs.getInt("projectid"),
                        rs.getInt("type_of_activityid"),
                        rs.getInt("score")
                    )
                    )
                }
                return@exec list
            }
        }
    }

    // Метод выводит только те проекты, в которых участвует пользователь
    fun getUserProject(userID: Int): String? {
        return transaction {
            exec(
                "SELECT task.id, task.name, " +
                        "task.status, task.start_data, " +
                        "task.descriptionid, task.parent, " +
                        "task.score, " +
                        "(SELECT COUNT(userid) FROM usersroleproject WHERE projectid=task.id) as user_count " +
                    "FROM usersroleproject " +
                    "INNER JOIN task ON task.id = projectid " +
                    "WHERE userid = $userID AND projectid IS NOT NULL AND task.parent IS NULL;") { rs ->
                val list = mutableListOf<TaskDTO>()
                while (rs.next()) {
                    val userCount = rs.getInt("user_count")

                    list.add(TaskDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("status"),
                        rs.getString("start_data"),
                        rs.getInt("score"),
                        rs.getInt("descriptionid"),
                        rs.getInt("parent"),
                        userCount,
                        rs.getInt("generation"),
                        rs.getInt("typeofactivityid"),
                    ))
                }
                val gson = GsonBuilder().create()
                return@exec gson.toJson(list)
            }
        }
    }



    fun getUserProjectRole(idProjekt: Int, idUser: Int ) : Int?
    {
        return transaction {
            exec(" SELECT roleid FROM usersroleproject WHERE projectid = $idProjekt AND userid = $idUser;") { rs ->
                var role : Int? = null
                while (rs.next()) {
                    role = rs.getInt("roleid")
                }
                return@exec role!!
            }

        }
    }

    fun getURP(id:Int): UserRoleProjectDTO? {
        return try{
            transaction {
                val UrpModle = UserRoleProjectModel.select { UserRoleProjectModel.id.eq(id) }.single()
                UserRoleProjectDTO(
                    id = UrpModle[UserRoleProjectModel.id],
                    users = UrpModle[users],
                    role = UrpModle[role],
                    task = UrpModle[task],
                    type_of_activity = UrpModle[type_of_activity],
                    score = UrpModle[score]
                )
            }
        }
        catch (e: Exception) {
            UserRoleProjectDTO()
        }
    }

    fun updateURP(id: Int, userRoleProjectDTO: UserRoleProjectDTO): HttpStatusCode {

        transaction {
            val urp = TaskModel.update({ UserRoleProjectModel.id eq (id) })
            {
                it[users] = userRoleProjectDTO.users
                it[role] = userRoleProjectDTO.role
                it[task] = userRoleProjectDTO.task
                it[type_of_activity] = userRoleProjectDTO.type_of_activity
                it[score] = userRoleProjectDTO.score
            }
            if (urp > 0) {
                return@transaction HttpStatusCode.NoContent
            } else {
                return@transaction "Task with ID $id not found."
            }
        }
        return HttpStatusCode.OK
    }

    fun deleteURP(id: Int): HttpStatusCode {
        if (id != null) {
            transaction {
                val deletedRowCount = UserRoleProjectModel.deleteWhere { UserRoleProjectModel.id eq id }
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