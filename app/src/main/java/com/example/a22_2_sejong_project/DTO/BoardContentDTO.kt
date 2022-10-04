package com.example.a22_2_sejong_project.DTO

data class BoardContentDTO(
    var title: String? = null,
    var description: String? = null,
    var uid: String? = null,
    var stdId: String? = null, // 이용해서 profile image, nickname 뽑아오기
    var nickname: String? = null,
    var timestamp: String? = null,
    var favoriteCount: Int = 0,
    var commentCount: Int = 0,
    var currentHeadCount: Int = 1, // 현재 모집된 인원
    var totalHeadCount: Int? = null,   // 총 모집할 인원
    var favorites: MutableMap<String, Boolean> = HashMap(),
    var contentType: Int = 1  // 참여 or 모집
) {
    data class Comment(
        var uid: String? = null,
        var userId: String? = null,
        var comment: String? = null,
        var timestamp: Long? = null
    )
}
