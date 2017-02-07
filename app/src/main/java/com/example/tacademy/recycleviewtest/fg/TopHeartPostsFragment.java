package com.example.tacademy.recycleviewtest.fg;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TopHeartPostsFragment extends ParentFragment {
    public TopHeartPostsFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // 쿼리 수행
        Query query =
                databaseReference.child("user-posts").child(getUid())
                        .orderByChild("heart_count");
        return query;
    }
}
