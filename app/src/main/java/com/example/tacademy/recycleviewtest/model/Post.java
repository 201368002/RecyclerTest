package com.example.tacademy.recycleviewtest.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tacademy on 2017-02-06.
 */

public class Post {

    // 제목, 내용, uid(내부 관리용-회원고유키), author(작성자 아이디-이메일@뺀아이디)
    String title, content, uid, author;
    // heart 카운트(좋아요 개수)
    int heart_count;
    // 누가 좋아요 했는지..
    Map<String, Boolean> hearts = new HashMap<>();

    public Post() {
    }

    public Post(String title, String content, String uid, String author) {
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.author = author;
    }

    public Map<String, Object> toPostMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("content", content);
        map.put("uid", uid);
        map.put("author", author);
        map.put("heart_count", heart_count);
        map.put("hearts", hearts);
        return map;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getHeart_count() {
        return heart_count;
    }

    public void setHeart_count(int heart_count) {
        this.heart_count = heart_count;
    }

    public Map<String, Boolean> getHearts() {
        return hearts;
    }

    public void setHearts(Map<String, Boolean> hearts) {
        this.hearts = hearts;
    }
}
