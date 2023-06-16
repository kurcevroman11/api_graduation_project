package com.example.plugins

//data class Task(val name: String, val dependencies: List<Int>)
//
//fun addTasksWithDependencies(tasks: MutableList<Task>, newTasks: List<Task>) {
//    val existingTaskNames = tasks.map { it.name }
//
//    // ��������� ����� ������ ��� ����� ������������
//    tasks.addAll(newTasks)
//
//    // ��������� ������ ����� ������ �� ������� ������������ � ��������� ��������������� �����
//    for (newTask in newTasks) {
//        for (dependency in newTask.dependencies) {
//            // ���������, ���������� �� ������, �� ������� ������� ������� ����� ������
//            val dependencyTask = tasks.find { it.name == dependency }
//            if (dependencyTask != null) {
//                // ��������� ����� ����� ����� ������� � �������-������������
//                dependencyTask.dependencies += newTask.name
//            } else {
//                // ���� ������-����������� ��� �� ��������� � ������, ��������� ��
//                if (!existingTaskNames.contains(dependency)) {
//                    tasks.add(Task(dependency, emptyList()))
//                }
//                // ��������� ����� ����� ����� ������� � �������-������������
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
//// ������� ������ ����� � ������ ������������
//for (task in tasks) {
//    println("������: ${task.name}")
//    println("�����������: ${task.dependencies}")
//    println()
//}
