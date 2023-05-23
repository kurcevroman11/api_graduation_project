package com.example.database.user

import com.example.db.Task.TaskDTO
import com.example.db.Task.TaskModel
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object UsserModel: Table("usser") {
    val id = UsserModel.integer("id").autoIncrement()
    val login =  UsserModel.varchar("login", 50)
    val password =  UsserModel.varchar("password", 50)
    val token_short =  UsserModel.text("token_short")
    val token_long =  UsserModel.text("token_long")
    val personId = UsserModel.integer("personid").nullable()

    fun  insert (usersDTO: UsersDTO){
        transaction {
            UsserModel.insert {
                it[login] = usersDTO.login
                it[password] = usersDTO.password
                it[token_short] = usersDTO.token_short
                it[token_long] = usersDTO.token_long
            }
        }
    }

    // user
    fun fetchUser(login: String): UsersDTO? {
        return try {
            transaction {
                val user = UsserModel.select { UsserModel.login.eq(login) }.single()
                UsersDTO(
                    id = user[UsserModel.id],
                    login = user[UsserModel.login],
                    password = user[password],
                    token_short = user[token_short],
                    token_long = user[token_long],
                    personId = user[personId]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchUserID(id: Int): UsersDTO? {
        return try {
            transaction {
                val user = UsserModel.select { UsserModel.id.eq(id) }.single()
                UsersDTO(
                    id = user[UsserModel.id],
                    login = user[UsserModel.login],
                    password = user[password],
                    token_short = user[token_short],
                    token_long = user[token_long],
                    personId = user[personId]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchAllUser(): List<UsersDTO?> {
        return try {
            transaction {
                UsserModel.selectAll().map{
                    UsersDTO(
                        it[UsserModel.id],
                        it[login],
                        it[password],
                        it[token_short],
                        it[token_long],
                        it[personId],
                    )
                }
            }
        } catch (e: Exception) {
            ArrayList<UsersDTO?>()
        }
    }
    fun updateUser(id: Int, userDTO: UsersDTO): HttpStatusCode {
        transaction {
            val user = UsserModel.update( { UsserModel.id eq (id) } )
            {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[token_long] = userDTO.token_long
                it[token_short] = userDTO.token_short
                it[personId] = userDTO.personId
            }
            if (user > 0) {
                return@transaction HttpStatusCode.NoContent
            } else {
                return@transaction "User with ID $id not found."
            }
        }
        return HttpStatusCode.OK
    }

    fun deleteUser(id: Int): HttpStatusCode {
        if (id != null) {
            transaction {
                val deletedRowCount = UsserModel.deleteWhere { UsserModel.id eq id }
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