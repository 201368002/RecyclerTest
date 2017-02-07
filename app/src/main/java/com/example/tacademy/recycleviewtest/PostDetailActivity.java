package com.example.tacademy.recycleviewtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacademy.recycleviewtest.model.Comment;
import com.example.tacademy.recycleviewtest.model.Post;
import com.example.tacademy.recycleviewtest.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * 글 상세보기 + 댓글달기
 */
public class PostDetailActivity extends BaseActivity {

    String postKey;
    DatabaseReference postReference, commentReference;
    EditText comment_input;
    RecyclerView detail_rv;
    
    ImageView profile, heart;
    TextView nickName, heart_count, title, content;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        
        // 키획득
        postKey = getIntent().getStringExtra("KEY");
        if( postKey == null ){
            Toast.makeText(this, "네트워크가 지연되고 있습니다. 잠시후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 데이터 획득 -> 참조 획득
        postReference = FirebaseDatabase.getInstance().getReference().child("posts").child(postKey);
        // 댓글 가지 -> 참조 획득
        commentReference = FirebaseDatabase.getInstance().getReference().child("post-comment").child(postKey);
        // 글 세팅
        comment_input   = (EditText)findViewById(R.id.comment_input);
        detail_rv       = (RecyclerView)findViewById(R.id.detail_rv);
        profile         = (ImageView)findViewById(R.id.profile);
        heart           = (ImageView)findViewById(R.id.heart);
        nickName        = (TextView)findViewById(R.id.nickName);
        heart_count     = (TextView)findViewById(R.id.heart_count);
        title           = (TextView)findViewById(R.id.title);
        content         = (TextView)findViewById(R.id.content);
        // 댓글 입력 받게 처리
        // 댓글을 쓰면 밑으로 리스팅(RecyclerView)
        detail_rv.setLayoutManager(new LinearLayoutManager(this));
        showTop();
    }

    // 댓글 뿌리기
    // 댓글 입력 이벤트
    public void onComment(View view){
        final String comment_input_str = comment_input.getText().toString();
        if(TextUtils.isEmpty(comment_input_str)){
            comment_input.setError("댓글 값이 없습니다.");
        }

        // 사용자 유효한지 체크
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Comment comment
                                = new Comment(getUid(), comment_input_str, user.getId());

                        // 글입력
                        commentReference.push().setValue(comment);
                        comment_input.setText("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    // onStar에서 걸어주고 onStop에서 빼야하니까 전역변수로.
    ValueEventListener valueEventListener;
    ChildEventListener childEventListener;
    ComAdapter comAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        // 이벤트 등록 세팅
        // 1. 작성자의 글을 가져온다. (1개)
        valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post model = dataSnapshot.getValue(Post.class);
                nickName.setText(model.getAuthor());
                heart_count.setText(""+model.getHeart_count());
                title.setText(model.getTitle());
                content.setText(model.getContent());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        postReference.addValueEventListener(valueEventListener);

        // 댓글을 뿌리는 아답터를 생성하여 리사이클러뷰에 세팅
        comAdapter = new ComAdapter(this, commentReference);
        detail_rv.setAdapter(comAdapter);
    }

    // 실시간 데이터베이스는 멈추는 작업을 해주어야함.
    @Override
    protected void onStop() {
        super.onStop();
        // 이벤트 해제
        // 내 글에 대한 이벤트
        if(valueEventListener != null)
            postReference.removeEventListener(valueEventListener);
        // 댓글에 대한 이벤트
        comAdapter.closeListener();
    }

    // 뷰홀더
    class ComViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_iv;
        TextView uid, comment;
        public ComViewHolder(View itemView) {
            super(itemView);

            profile_iv   = (ImageView) itemView.findViewById(R.id.profile_iv);
            uid          = (TextView) itemView.findViewById(R.id.uid);
            comment      = (TextView) itemView.findViewById(R.id.comment);
        }
    }
    // 아답타
    class ComAdapter extends RecyclerView.Adapter<ComViewHolder>{
        ArrayList<Comment> comments = new ArrayList<Comment>(); // 데이터를 아답타 안에 넣음.
        Context context;
        DatabaseReference root;

        public ComAdapter(Context context, DatabaseReference root){ // 구조적으로 접근 -> 전통적인 방법 ( 모델, 아답타, 뷰홀더 다 따로 구현 -> 서로 영향을 받지 않는다 )
            // 회사에서 소스를 주면 아답타를 뺏는지 안뺏는지 확인 후 접근
            this.context = context;
            this.root = root;
            // 데이터 획득!!
            childEventListener = new ChildEventListener() {    // addChildEventListener : 댓글은 n개니까
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    comments.add(comment);
                    notifyItemInserted(comments.size()-1);  // 0번 데이터가 추가되었다.
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            // 이벤트 등록
            root.addChildEventListener(childEventListener);
        }

        public void closeListener(){
            // 이벤트 제거
            root.removeEventListener(childEventListener);
        }

        @Override
        public ComViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                LayoutInflater.from(context).inflate(R.layout.cell_comment_layout, parent, false);
            return new ComViewHolder(view);
        }

        // PostViewHolder -> bindToPost 기능을 onBindViewHolder가 수행
        @Override
        public void onBindViewHolder(ComViewHolder holder, int position) {
            Comment comment = comments.get(position);       // 데이터 획득
            holder.uid.setText(comment.getAuthor());        // 작성자
            holder.comment.setText(comment.getComment());   // 댓글
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }

    public void showTop(){

    }
}
