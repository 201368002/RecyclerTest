package com.example.tacademy.recycleviewtest.model;

/**
 * Created by Tacademy on 2017-02-03.
 * 채팅 메시지 구조 -> 디비 구조
 */

public class ChatMessage {
    String username;
    String message;

    // 기본 생성자를 꼭 만든다
    public ChatMessage() {
    }

    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
