package com.example.a22_2_sejong_project.DTO

data class UserDTO(
    val uid : String,
    val stdId : String,
    val email : String,
    val name : String,
    var nickname : String,
    val college : String,
    val major : String,
    var note : String?,
    var profileUrl : String?
)
