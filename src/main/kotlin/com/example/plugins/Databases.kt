package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases() {
    val database = Database.connect(
            url = "jdbc:h2:tcp://localhost:9092/./users;INIT=CREATE SCHEMA IF NOT EXISTS USERS\\;SET SCHEMA USERS;DB_CLOSE_DELAY=-1;",
            user = "",
            driver = "org.h2.Driver",
            password = ""
        )
    val userService = UserService(database)
    routing {
        route("/users") {
            post {
                val user = call.receive<ExposedUser>()
                val id = userService.create(user)
                call.respond(HttpStatusCode.Created, id)
            }

            get("{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = userService.read(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put("{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = call.receive<ExposedUser>()
                userService.update(id, user)
                call.respond(HttpStatusCode.OK)
            }

            delete("{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                userService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
