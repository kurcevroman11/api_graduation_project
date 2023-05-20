package com.example.db


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
class dbHelper {

    fun creatyTable(name_user : String) {
        // Создаем соединение с базой данных PostgreSQL
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/sebbia1",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "123321"
        )

        // Создаем таблицу users
        transaction {
            addLogger(StdOutSqlLogger) // Логируем SQL-запросы


            // Вставляем одну запись в таблицу
            Users1.insert {
                it[name] = name_user
            }
        }
    }

    // Описание таблицы users
    object Users1 : Table() {
        val id: Column<Int> = integer("id").autoIncrement().primaryKey()
        val name: Column<String> = varchar("name", 50)
    }
    fun addUser() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/Sebbia",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "123321"
        )

        // Создаем таблицу users
        transaction {

            addLogger(StdOutSqlLogger) // Логируем SQL-запросы


            // Вставляем одну запись в таблицу
            Users.insert {
                it[Users.name] = "1"
                it[Users.login] = "1"
                it[Users.surname] = "1"
                it[Users.middle_name] = "1"
                it[Users.token_short] = "1"
                it[Users.token_long] = "1"
                it[Users.password] = "1"
                it[Users.Type_of_activity] = 3
            }
        }
    }

    object Users : Table() {
        val id: Column<Int> = integer("id").autoIncrement().primaryKey()
        val login: Column<String> = varchar("Login", 64)
        val password: Column<String> = varchar("Password", 64)
        val surname: Column<String> = varchar("Surname", 64)
        val name: Column<String> = varchar("Name", 64)
        val middle_name: Column<String> = varchar("Middle_name", 64)
        val Type_of_activity: Column<Int> = integer("Type_of_activity")
        val token_short : Column<String> = varchar("token_short", 64)
        val token_long : Column<String> = varchar("token_long", 64)
    }
}




