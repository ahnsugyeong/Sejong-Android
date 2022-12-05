package com.example.a22_2_sejong_project.chat


//class ChatModel (val users: HashMap<String, Boolean> = HashMap(),
//                 val comments : HashMap<String, Comment> = HashMap()){
//    class Comment(val contents: String? = null, val nickname: String? = null, val time: String? = null, val uid: String? = null)
//}
//class ChatModel (val users: HashMap<String, Boolean> = HashMap(),
//                 val comments : HashMap<String, Comment> = HashMap()){
//    class Comment(val uid: String? = null, val message: String? = null, val time: String? = null)
//}

data class Chat(var senderId:String = "", var receiverId:String = "",
                var message:String = "", var senderName:String = "", var time: String = "", var profileImgUrl: String? = null)