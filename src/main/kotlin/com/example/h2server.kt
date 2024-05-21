package com.example

import org.h2.tools.Server
import java.sql.SQLException

fun main(){
    try{
        val h2server = Server.createTcpServer(
            "-tcpAllowOthers",
            "-tcpPort", "9092",  // Specify the TCP port if the default is not suitable
            "-baseDir", "/home/fillipe/Projects/h2db",  // Set the base directory for databases
            "-ifNotExists"
        ).start()
        println("Server started and connection is open.")
        println("URL: jdbc:h2:${h2server.url}/test")
    } catch (e: SQLException) {
        println("Error starting server ${e.message}")
    }
}