package com.example.fixzy_ketnoikythuatvien.data.repository

object DatabaseFactory {
    fun init() {
        val jdbcUrl = "jdbc:mysql://localhost:3306/your_database_name"
        val driver = "com.mysql.cj.jdbc.Driver"
        val user = "your_username"
        val password = "your_password"



        println("✅ Connected to MySQL database!")
    }
}