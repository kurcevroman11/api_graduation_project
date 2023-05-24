package com.example.database.Role

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object RoleModel: Table("role") {
    private val id = RoleModel.integer("id").autoIncrement()
    private val name = RoleModel.varchar("name", 64)

    fun getRole(id:Int): RoleDTO {

        lateinit var roleDTO: RoleDTO

        transaction {
            val roleModel = RoleModel.select { RoleModel.id.eq(id) }.single()
            roleDTO = RoleDTO(
                id = roleModel[RoleModel.id],
                name = roleModel[RoleModel.name]

            )
        }
        return roleDTO
    }

    fun getRoleAll(): List<RoleDTO> {

        return transaction {
            RoleModel.selectAll().map {
                RoleDTO(
                    id = it[RoleModel.id],
                    name = it[RoleModel.name],
                )
            }
        }
    }
}