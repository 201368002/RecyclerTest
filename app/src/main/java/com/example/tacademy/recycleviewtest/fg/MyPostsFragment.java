package com.example.tacademy.recycleviewtest.fg;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyPostsFragment extends ParentFragment {


    public MyPostsFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // 쿼리 수행
        Query query =
                databaseReference.child("user-posts").child(getUid())
                        .limitToLast(10);
        return query;
    }
}
