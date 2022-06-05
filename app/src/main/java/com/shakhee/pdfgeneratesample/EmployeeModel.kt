package com.shakhee.pdfgenerateusingjson

class EmployeeModel {

    lateinit var name:String
    lateinit var userId:String
    lateinit var department:String
    lateinit var role:String
    lateinit var designation:String
    lateinit var salary:String

    constructor(
        userId: String,
        name: String,
        department: String,
        role: String,
        designation: String,
        salary: String
    ) {
        this.userId = userId
        this.name = name
        this.department = department
        this.role = role
        this.designation = designation
        this.salary = salary
    }

    constructor()


}
