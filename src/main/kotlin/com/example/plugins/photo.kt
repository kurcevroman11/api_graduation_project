package com.example.plugins

//data class Task(val name: String, val dependencies: List<Int>)
//
//fun addTasksWithDependencies(tasks: MutableList<Task>, newTasks: List<Task>) {
//    val existingTaskNames = tasks.map { it.name }
//
//    // ƒобавл€ем новые задачи без учета зависимостей
//    tasks.addAll(newTasks)
//
//    // ѕровер€ем каждую новую задачу на наличие зависимостей и добавл€ем соответствующие св€зи
//    for (newTask in newTasks) {
//        for (dependency in newTask.dependencies) {
//            // ѕровер€ем, существует ли задача, от которой зависит текуща€ нова€ задача
//            val dependencyTask = tasks.find { it.name == dependency }
//            if (dependencyTask != null) {
//                // ƒобавл€ем св€зь между новой задачей и задачей-зависимостью
//                dependencyTask.dependencies += newTask.name
//            } else {
//                // ≈сли задача-зависимость еще не добавлена в список, добавл€ем ее
//                if (!existingTaskNames.contains(dependency)) {
//                    tasks.add(Task(dependency, emptyList()))
//                }
//                // ƒобавл€ем св€зь между новой задачей и задачей-зависимостью
//                tasks.last().dependencies += newTask.name
//            }
//        }
//    }
//}
//
//val tasks: MutableList<Task> = mutableListOf(
//    Task("Backend", emptyList()),
//    Task("Design", emptyList())
//)
//
//val newTasks: List<Task> = listOf(
//    Task("Frontend", listOf("Design")),
//    Task("QA", listOf("Frontend", "Backend"))
//)
//
//addTasksWithDependencies(tasks, newTasks)
//
//// ¬ыводим список задач с учетом зависимостей
//for (task in tasks) {
//    println("«адача: ${task.name}")
//    println("«ависимости: ${task.dependencies}")
//    println()
//}
