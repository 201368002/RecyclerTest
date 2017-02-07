package com.example.tacademy.recycleviewtest.fg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tacademy.recycleviewtest.PostDetailActivity;
import com.example.tacademy.recycleviewtest.R;
import com.example.tacademy.recycleviewtest.model.Post;
import com.example.tacademy.recycleviewtest.model.PostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public abstract class ParentFragment extends Fragment {


    RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    public ParentFragment() {
    }

    // abstract : 나는 정의만 할게 니가 가져다가 정의해!
    // abstract하나만 들어가도 클래스는 public abstract class ParentFragment로 변경
    // 쿼리를 획득하는 메소드를 만들어서, 자식이 재정의 하여
    // 쿼리 결과가 달라지게 처리한다.
    public abstract Query getQuery(DatabaseReference databaseReference);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_posts, container, false);
        // 화면 구성 세팅..
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        // 레이아웃 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 쿼리 수행
        Query query = getQuery(FirebaseDatabase.getInstance().getReference());
//        Query query =
//                FirebaseDatabase.getInstance().getReference().child("posts")
//                        .limitToLast(10);
//        //.limitToFirst(10);

        // 아답터 생성
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.cell_post_layout,
                PostViewHolder.class,
                // 쿼리 결과
                query
        ) {

            // 레이아웃을 담기는 그릇, 데이터가 담기는 그릇, 필요한 인덱스
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {
                // 1. position -> 데이터 획득 (참조 획득) -> 줄기
                final DatabaseReference databaseReference = getRef(position);
                // 2. viewHolder -> 이벤트 등록
                // 하나 클릭시 상세 게시글 보기로.
                viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // 상세보기로 이동
                        Intent intent = new Intent(getContext(), PostDetailActivity.class);
                        intent.putExtra("KEY", databaseReference.getKey()); // new View.OnClickListener() 이너 클래스이기 때문에 databaseReference변수를 사용하기위해선 final 처리
                        getContext().startActivity(intent);
                    }
                });
                if(model.getHearts().containsKey(getUid())){
                    // 별처리 (색을 채운다)
                }else{
                    // 별처리 (색을 비운다)
                }
                // 3. viewHolder -> 데이터 세팅 (PostViewHolder.class -> bindToPost())
                viewHolder.bindToPost(model, new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // 별을 클릭해서 좋아요 처리, 다시 눌러서 해제 처리

                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        return view;
    }

    // 나의 아이디 -> 로그인 이후에만 성립된다.
    public String getUid()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
