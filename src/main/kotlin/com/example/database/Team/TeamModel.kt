package com.example.db.Team
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
object TeamModel: Table("team") {
    private  val id = TeamModel.integer("id").autoIncrement().primaryKey()
    private  val users = TeamModel.integer("Users").nullable()
    private  val task = TeamModel.integer("Task")
    private  val evaluation = TeamModel.varchar("Evaluation",64).nullable()
    private  val time = TeamModel.varchar("Time",64).nullable()

    fun  insert(teamDTO: TeamDTO){

        transaction {
            addLogger(StdOutSqlLogger)


            TeamModel.insert{
                it[users] = teamDTO.users
                it[task] = teamDTO.task
                it[evaluation] = teamDTO.evaluation
                it[time] = teamDTO.time
            }


        }
    }
    fun fetchTask(id:Int): TeamDTO {

        lateinit var teamDTO: TeamDTO

        transaction {
            val teamModel = TeamModel.select { TeamModel.id.eq(id) }.single()
            teamDTO = TeamDTO(
                id = teamModel[TeamModel.id],
                users = teamModel[TeamModel.users],
                task = teamModel[TeamModel.task],
                time = teamModel[TeamModel.time],
                evaluation = teamModel[TeamModel.evaluation],

                )
        }
        return teamDTO
    }

}