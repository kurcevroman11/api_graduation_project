package com.example.database.Person

import io.ktor.http.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.statements.InsertStatement

object PersonModule: Table("person") {
    private val id = PersonModule.integer("id").autoIncrement()
    private val surname = PersonModule.varchar("surname", 64)
    private val name = PersonModule.varchar("name", 64)
    private val patronymic = PersonModule.varchar("patronymic", 64).nullable()
    private val type_of_activity = PersonModule.integer("type_of_activity").nullable()


    fun  insertPerson(personDTO: PersonDTO){
        transaction {
            addLogger(StdOutSqlLogger)

            PersonModule.insert{
                it[surname] = personDTO.surname
                it[name] = personDTO.name
                it[patronymic] = personDTO.patronymic
                it[type_of_activity] = personDTO.type_of_activity
            }
        }
    }

    fun fetchPersonID(id:Int): PersonDTO? {
        return try {
            transaction {
                val person = PersonModule.select { PersonModule.id.eq(id) }.single()
                PersonDTO(
                    id = person[PersonModule.id],
                    surname = person[surname],
                    name = person[name],
                    patronymic = person[patronymic],
                    type_of_activity = person[type_of_activity]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchAllPerson(): List<PersonDTO> {
        return try {
            transaction {
                PersonModule.selectAll().map{
                    PersonDTO(
                        it[PersonModule.id],
                        it[surname],
                        it[name],
                        it[patronymic],
                        it[type_of_activity],
                    )
                }
            }
        } catch (e: Exception) {
            ArrayList<PersonDTO>()
        }

    }

    fun updatePerson(id: Int, personDTO: PersonDTO): HttpStatusCode {
        transaction {
            val person = PersonModule.update( { PersonModule.id eq (id) } )
            {
                it[surname] = personDTO.surname
                it[name] = personDTO.name
                it[patronymic] = personDTO.patronymic
                it[type_of_activity] = personDTO.type_of_activity
            }
            if (person > 0) {
                return@transaction HttpStatusCode.NoContent
            } else {
                return@transaction "Person with ID $id not found."
            }
        }
        return HttpStatusCode.OK
    }

    fun deletePerson(id: Int): HttpStatusCode {
        if (id != null) {
            transaction {
                val deletedRowCount = PersonModule.deleteWhere { PersonModule.id eq id }
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






