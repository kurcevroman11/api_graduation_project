package com.example.db.UserRoleProject

import com.example.database.UserRoleProject.UserRoleProjectDTO
import com.example.db.Task.TaskModel
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object UserRoleProjectModel: Table("usersroleproject"){
    private  val id = UserRoleProjectModel.integer("id").autoIncrement()
    private  val users = UserRoleProjectModel.integer("userid").nullable()
    private  val role = UserRoleProjectModel.integer("roleid").nullable()
    private  val task = UserRoleProjectModel.integer("projectid")

    fun  insert(userRoleProjectDTO: UserRoleProjectDTO){
        transaction {
            addLogger(StdOutSqlLogger)
            UserRoleProjectModel.insert{
                it[users] = userRoleProjectDTO.users
                it[role] = userRoleProjectDTO.role
                it[task] = userRoleProjectDTO.task
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
                    it[task]
                )
            }
        }
    }

    fun getUserProject(id: Int) : MutableList<UserRoleProjectDTO>?
    {
        return transaction {
            exec(" SELECT * FROM usersroleproject WHERE projectid = $id;") { rs ->
                val list = mutableListOf<UserRoleProjectDTO>()
                while (rs.next()) {
                    list.add(UserRoleProjectDTO(
                        rs.getInt("id"),
                        rs.getInt("userid"),
                        rs.getInt("roleid"),
                        rs.getInt("projectid"),)
                    )
                }
                return@exec list
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