package com.example.tacademy.recycleviewtest.fg;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TotalPostsFragment extends ParentFragment {
    public TotalPostsFragment() {
    }

    // 부모 함수 재정의
    @Override
    public Query getQuery(DatabaseReference databaseReference){
        // 쿼리 수행
        Query query =
                databaseReference.child("posts")
                        .limitToLast(10);
        //.limitToFirst(10);
        return query;
    }

}
