package com.example.taka

class Task_Content {
    var id = 0
    var content: String
    internal constructor(content: String) {
        this.content = content
    }
    internal constructor(id: Int, content: String) {
        this.id = id
        this.content = content
    }
}