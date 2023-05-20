package com.example.db.UserRoleProject

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
object UserRoleProjectModel: Table("usersroleproject"){

    private  val id = UserRoleProjectModel.integer("id").autoIncrement().primaryKey()
    private  val users = UserRoleProjectModel.integer("UserID").nullable()
    private  val task = UserRoleProjectModel.integer("ProjectID")
    private  val role = UserRoleProjectModel.integer("RoleID").nullable()

    fun  insert(userRoleProjectDTO: UserRoleProjectDTO){

        transaction {
            addLogger(StdOutSqlLogger)


            UserRoleProjectModel.insert{
                it[users] = userRoleProjectDTO.users
                it[task] = userRoleProjectDTO.task
                it[role] = userRoleProjectDTO.role
            }


        }
    }
    fun fetchTask(id:Int): UserRoleProjectDTO {

        lateinit var userRoleProjectDTO: UserRoleProjectDTO

        transaction {
            val userRoleProjectModel = UserRoleProjectModel.select { UserRoleProjectModel.id.eq(id) }.single()
            userRoleProjectDTO = UserRoleProjectDTO(
                id = userRoleProjectModel[UserRoleProjectModel.id],
                users = userRoleProjectModel[UserRoleProjectModel.users],
                role = userRoleProjectModel[UserRoleProjectModel.role],
                task = userRoleProjectModel[UserRoleProjectModel.task]
                )
        }
        return userRoleProjectDTO
    }
}