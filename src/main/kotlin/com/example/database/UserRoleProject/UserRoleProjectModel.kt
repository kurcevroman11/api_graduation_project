package com.example.db.UserRoleProject

import com.example.database.UserRoleProject.UserRoleProjectDTO
import com.example.db.Task.TaskDTO
import com.example.db.Task.TaskDependence
import com.example.db.Task.TaskModel
import com.google.gson.GsonBuilder
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object UserRoleProjectModel : Table("usersroleproject") {
    private val id = UserRoleProjectModel.integer("id").autoIncrement()
    private val users = UserRoleProjectModel.integer("userid").nullable()
    private val role = UserRoleProjectModel.integer("roleid").nullable()
    private val task = UserRoleProjectModel.integer("projectid").nullable()
    private val type_of_activity = UserRoleProjectModel.integer("type_of_activityid").nullable()
    private val score = UserRoleProjectModel.integer("score").nullable()
    private val current_task = UserRoleProjectModel.integer("current_task_id").nullable()

    fun insert(userRoleProjectDTO: UserRoleProjectDTO) {
        transaction {
            addLogger(StdOutSqlLogger)
            UserRoleProjectModel.insert {
                it[users] = userRoleProjectDTO.userid
                it[role] = userRoleProjectDTO.roleid
                it[task] = userRoleProjectDTO.projectid
                it[type_of_activity] = userRoleProjectDTO.type_of_activityid
                it[score] = userRoleProjectDTO.score
            }
        }
    }

    fun getURPAll(): List<UserRoleProjectDTO> {
        return transaction {
            UserRoleProjectModel.selectAll().map {
                UserRoleProjectDTO(
                    it[UserRoleProjectModel.id],
                    it[users],
                    it[role],
                    it[task],
                    it[type_of_activity],
                    it[score],
                    it[current_task]
                )
            }
        }
    }

    fun getALLUserProject(id: Int): MutableList<UserRoleProjectDTO>? {
        return transaction {
            exec(" SELECT * FROM usersroleproject WHERE projectid = $id;") { rs ->
                val list = mutableListOf<UserRoleProjectDTO>()
                while (rs.next()) {
                    list.add(
                        UserRoleProjectDTO(
                            rs.getInt("id"),
                            rs.getInt("userid"),
                            rs.getInt("roleid"),
                            rs.getInt("projectid"),
                            rs.getInt("type_of_activityid"),
                            rs.getInt("score"),
                            rs.getInt("current_task_id")
                        )
                    )
                }
                return@exec list
            }
        }
    }

    // Метод выводит только те проекты, в которых участвует пользователь
    fun getUserProject(userID: Int): String? {
        return transaction {
            exec(
                "SELECT task.id, task.name, " +
                        "task.status, to_char(task.start_data, 'YYYY-MM-DD HH24:MI:SS') as start_date, " +
                        "task.score, " +
                        "(SELECT COUNT(userid) FROM usersroleproject WHERE projectid=task.id) as user_count " +
                        "FROM usersroleproject " +
                        "INNER JOIN task ON task.id = projectid " +
                        "WHERE userid = $userID;"
            ) { rs ->
                val list = mutableListOf<TaskDTO>()
                while (rs.next()) {
                    val userCount = rs.getInt("user_count")

                    list.add(
                        TaskDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("status"),
                            rs.getString("start_date"),
                            rs.getInt("score"),
                            null,
                            null,
                            userCount,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    )
                }
                val gson = GsonBuilder().create()
                return@exec gson.toJson(list)
            }
        }
    }

    // Метод, который раскладывает всех исполнителей по задачам
    fun getTask_executors() {
        var urp_all = getURPAll()
        for (urp in urp_all) {
            urp_all = getURPAll()
            if (urp.current_task_id == null) {
                var project = urp.projectid
                if (project != null) {
                    var task_list = TaskModel.getTaskWithChilds(project)
                    if (task_list.size > 0) {
                        var needed_gen = task_list[0].generation
                        for (task in task_list) {
                            var need_continue = false
                            for (urp2 in urp_all) {
                                if (urp2.current_task_id == task.id) {
                                    need_continue = true
                                    break
                                }
                            }
                            if (need_continue || task.generation != needed_gen || task.typeofactivityid != urp.type_of_activityid)
                                continue

                            updateURP2(urp.id!!, task.id!!)
                            break
                        }
                    }
                }
            }
        }
    }

    fun getUserProjectRole(idProjekt: Int, idUser: Int): Int? {
        return transaction {
            exec(" SELECT roleid FROM usersroleproject WHERE projectid = $idProjekt AND userid = $idUser;") { rs ->
                var role: Int? = null
                while (rs.next()) {
                    role = rs.getInt("roleid")
                }
                return@exec role!!
            }

        }
    }

    @Serializable
    data class Calendar_plan(
        val taskId: Int,
        val userScore: Int,
        val taskName: String,
        val taskParent: Int,
        val taskScore: Int,
        val taskDependence: String?
    )

    @Serializable
    data class CalendarPlan(
        val nameTask: String,
        val execution: Int,
        var start: Int = 0
    )

    // Метод, который планирует календарный план
    fun scheduling():  MutableList<UserRoleProjectModel.CalendarPlan> {
        val list = mutableListOf<Calendar_plan>()
        transaction {
            exec(
                "SELECT " +
                        "task.id, " +
                        "usersroleproject.score, " +
                        "task.name, " +
                        "task.parent, " +
                        "task.score, " +
                        "task.dependence FROM person " +
                        "INNER JOIN usser ON person.id = usser.personid " +
                        "INNER JOIN usersroleproject ON usser.id = usersroleproject.userid " +
                        "INNER JOIN task ON usersroleproject.current_task_id = task.id;"
            ) { rs ->
                while (rs.next()) {
                    list.add(
                        Calendar_plan(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getInt(5),
                            rs.getString(6)
                        )
                    )
                }
            }
        }

        val listOfPlan = mutableListOf<CalendarPlan>()



        //sheduling_task - в качестве ключа название задачи, а значение число (кол-во дней)
        val sheduling_task = mutableMapOf(list[0].taskName to 1)

        var taskScore = 0
        for (item in list) {
            var task_doing = 1 // кол-во дней на выполение задания
            taskScore = item.taskScore
            // Цикл подсчитывает количество дней на выполнение задания
            while (taskScore - item.userScore > 0) {
                task_doing += 1
                taskScore -= item.userScore
            }

            listOfPlan.add(
                CalendarPlan(
                nameTask = item.taskName,
                execution = task_doing
                )
            )
        }


        val regex = "\\[(\\d+(?:,\\s*\\d+)*)\\]".toRegex()
        val Id_blocking_tasks = mutableListOf<Int>()
        for(item in list){
            if(item.taskDependence != null){
                regex.findAll(item.taskDependence!!).forEach {
                    val values = it.groupValues[1].split(",").map { it.trim().toInt() }
                    Id_blocking_tasks.addAll(values)
                }
            }
        }

        val days_execution = mutableListOf<Int>()
        for(item in list){
            if(Id_blocking_tasks.contains(item.taskId) || Id_blocking_tasks.contains(item.taskParent)){
                for(plan in listOfPlan){
                    if(plan.nameTask == item.taskName){
                        days_execution.add(plan.execution)
                    }
                }
            }
        }

        for(item in list){
            if(item.taskDependence != null){
                for(plan in listOfPlan){
                    if(plan.nameTask == item.taskName){
                        plan.start = days_execution.max()
                    }
                }
            }
        }




        return listOfPlan
    }

    fun getURP(id: Int): UserRoleProjectDTO? {
        return try {
            transaction {
                val UrpModle = UserRoleProjectModel.select { UserRoleProjectModel.id.eq(id) }.single()
                UserRoleProjectDTO(
                    id = UrpModle[UserRoleProjectModel.id],
                    userid = UrpModle[users],
                    roleid = UrpModle[role],
                    projectid = UrpModle[task],
                    type_of_activityid = UrpModle[type_of_activity],
                    score = UrpModle[score],
                    current_task_id = UrpModle[current_task]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun updateURP(id: Int, userRoleProjectDTO: UserRoleProjectDTO): HttpStatusCode {
        transaction {
            val urp = TaskModel.update({ UserRoleProjectModel.id eq (id) })
            {
                it[users] = userRoleProjectDTO.userid
                it[role] = userRoleProjectDTO.roleid
                it[task] = userRoleProjectDTO.projectid
                it[type_of_activity] = userRoleProjectDTO.type_of_activityid
                it[score] = userRoleProjectDTO.score
                it[current_task] = userRoleProjectDTO.current_task_id
            }
            if (urp > 0) {
                return@transaction HttpStatusCode.NoContent
            } else {
                return@transaction "Task with ID $id not found."
            }
        }
        return HttpStatusCode.OK
    }

    fun updateURP2(id: Int, task_id: Int): HttpStatusCode {
        transaction {
            val urp = UserRoleProjectModel.update({ UserRoleProjectModel.id eq (id) })
            {
                it[current_task] = task_id
            }
            if (urp > 0) {
                return@transaction HttpStatusCode.NoContent
            } else {
                return@transaction "Task with ID $id not found."
            }
        }
        return HttpStatusCode.OK
    }

    fun deleteURP(id: Int): HttpStatusCode {
        if (id != null) {
            transaction {
                val deletedRowCount = UserRoleProjectModel.deleteWhere { UserRoleProjectModel.id eq id }
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

    fun deleteURPByTask(task_id: Int): HttpStatusCode {
        if (id != null) {
            transaction {
                val deletedRowCount = UserRoleProjectModel.deleteWhere { UserRoleProjectModel.task eq task_id }
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