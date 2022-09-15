package com.example.a22_2_sejong_project.DTO

data class BoardContentDTO(
    var title: String? = null,
    var description: String? = null,
    var uid: String? = null,
    var userId: String? = null, // 이용해서 profile image, nickname 뽑아오기
    var timestamp: Long? = null,
    var favoriteCount: Int = 0,
    var commentCount: Int = 0,
    var currentNumber: Int? = null, // 현재 모집된 인원
    var totalNumber: Int? = null,   // 총 모집할 인원
    var favorites: MutableMap<String, Boolean> = HashMap()
) {
    data class Comment(
        var uid: String? = null,
        var userId: String? = null,
        var comment: String? = null,
        var timestamp: Long? = null
    )
}
