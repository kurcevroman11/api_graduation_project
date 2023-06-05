package com.example.database.type_of_activity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Type_of_activityModel: Table("type_of_activity") {
    private val id = Type_of_activityModel.integer("id").autoIncrement()
    private val name = Type_of_activityModel.varchar("name", 64)

    fun getType_of_activity(id: Int): Type_of_activityDTO {

        lateinit var type_of_activityDTO: Type_of_activityDTO

        transaction {
            val type_of_activityModel = Type_of_activityModel.select { Type_of_activityModel.id.eq(id) }.single()
            type_of_activityDTO = Type_of_activityDTO(
                id = type_of_activityModel[Type_of_activityModel.id],
                name = type_of_activityModel[name]
            )
        }
        return type_of_activityDTO
    }

    fun getType_of_activityAll(): List<Type_of_activityDTO> {

        return transaction {
            Type_of_activityModel.selectAll().map {
                Type_of_activityDTO(
                    id = it[Type_of_activityModel.id],
                    name = it[name]
                )
            }
        }
    }
}