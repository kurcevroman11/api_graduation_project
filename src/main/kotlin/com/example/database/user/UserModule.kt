package com.example.database.user

import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object UserModule: Table("usser") {
    val id = UserModule.integer("id").autoIncrement()
    val login =  UserModule.varchar("login", 50)
    val password =  UserModule.varchar("password", 50)
    val token_long =  UserModule.text("token_long")
    val personId = UserModule.integer("personid").nullable()

    fun  insert (usersDTO: UsersDTO){
        transaction {
            UserModule.insert {
                it[login] = usersDTO.login
                it[password] = usersDTO.password
                it[token_long] = usersDTO.token_long
                it[personId] = usersDTO.personId
            }
        }
    }

    // user
    fun fetchUser(login: String): UsersDTO? {
        return try {
            transaction {
                val user = UserModule.select { UserModule.login.eq(login) }.single()
                UsersDTO(
                    id = user[UserModule.id],
                    login = user[UserModule.login],
                    password = user[password],
                    token_long = user[token_long],
                    personId = user[personId]
                )
            }
        } catch (e: Exception) {
            null
        }
    }
    fun getUserToLogin(login: String): UsersDTO? {
        return try {
            transaction {
                val user = UserModule.select { UserModule.login.eq(login) }.single()
                UsersDTO(
                    id = user[UserModule.id],
                    login = user[UserModule.login],
                    password = user[password],
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
                val user = UserModule.select { UserModule.id.eq(id) }.single()
                UsersDTO(
                    id = user[UserModule.id],
                    login = user[UserModule.login],
                    password = user[password],
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
                UserModule.selectAll().map{
                    UsersDTO(
                        it[UserModule.id],
                        it[login],
                        it[password],
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
            val user = UserModule.update( { UserModule.id eq (id) } )
            {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[token_long] = userDTO.token_long
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
                val deletedRowCount = UserModule.deleteWhere { UserModule.id eq id }
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