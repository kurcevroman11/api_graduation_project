package com.example.database.Status

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object StatusModel: Table("status") {
    private val id = StatusModel.integer("id").autoIncrement()
    private val name = StatusModel.varchar("name", 64)

    fun getStatus(id: Int): StatusDTO {
        lateinit var roleDTO: StatusDTO

        transaction {
            val roleModel = StatusModel.select { StatusModel.id.eq(id) }.single()
            roleDTO = StatusDTO(
                id = roleModel[StatusModel.id],
                name = roleModel[name]

            )
        }
        return roleDTO
    }

    fun getStatusAll(): List<StatusDTO> {
        return transaction {
            StatusModel.selectAll().map {
                StatusDTO(
                    id = it[StatusModel.id],
                    name = it[name]
                )
            }
        }
    }
}