package com.example.db.person

import com.example.db.Task.TaskDTO

import com.example.db.Team.TeamModel.autoIncrement
import com.example.db.Team.TeamModel.primaryKey
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object PersonModel: Table("person") {
    private val id = PersonModel.integer("id").autoIncrement().primaryKey()
    private val surname = PersonModel.varchar("surname", 64)
    private val name = PersonModel.varchar("name", 64)
    private val patronymic = PersonModel.varchar("patronymic", 64)
    private val type_of_activity = PersonModel.integer("type_of_activity")


    fun  insert(personDTO: PersonDTO){
        transaction {

            addLogger(StdOutSqlLogger)

            PersonModel.insert{
                it[surname] = personDTO.surname
                it[name] = personDTO.name
                it[patronymic] = personDTO.patronymic
                it[type_of_activity] = personDTO.type_of_activity

            }


        }
    }
    fun fetchTask(id:Int): PersonDTO {

        lateinit var personDTO: PersonDTO

        transaction {
            val personModel = PersonModel.select { PersonModel.id.eq(id) }.single()
            personDTO = PersonDTO(
                id = personModel[PersonModel.id],
                surname = personModel[PersonModel.surname],
                name = personModel[PersonModel.name],
                patronymic= personModel[PersonModel.patronymic],
                type_of_activity = personModel[PersonModel.type_of_activity],

            )
        }
        return personDTO
    }
}