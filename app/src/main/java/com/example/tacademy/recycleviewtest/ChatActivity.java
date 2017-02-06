package com.example.tacademy.recycleviewtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.tacademy.recycleviewtest.model.ChatMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        onBasic();

        // 1. 파베 디비 객체 획득
        firebaseDatabase = FirebaseDatabase.getInstance();
        // 2. 디비를 access 할 수 있는 참조 값 (root) 획득
        databaseReference = firebaseDatabase.getReference();
//        // 3. insert 적절한 경로 ( 가지를 하나 만들어서 ) 에다 메시지 입력
//        databaseReference.child("chat").push()
//                .setValue(new ChatMessage("삼다수", "하이"))
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Log.i("CHAT", "등록완료");
//                        }else{
//                            Log.i("CHAT", "등록실패");
//                        }
//                    }
//                }); //push()는 가지에 대한 중복되지 않는 키가 하나 만들어 진다. 거기에 값을 넣는 것이 setValue()
        // 4. select 등록된 데이터 가져오기
        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            // 아이템을 검색하거나, 추가될 때 호출 (select, insert)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // s는 전번 데이터의 키를 가르킨다 (링크드리스트) -> 실질적으로 순차적으로 오기때문에 s는 잘 사용하지 X
                //Log.i("CHAT", dataSnapshot.toString() + "/" + s);
                // 파싱 : 데이터를 ChatMessage틀에 넣어준다.
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                Log.i("CHAT", chatMessage.getUsername());
            }
            // 아이템의 변화가 감지되면 호출 (update)
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            // 아이템이 삭제되면 호출 (delete)
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                Log.i("CHAT", dataSnapshot.getKey() + "삭제완료");
            }

            // 아이템의 순서가
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // ===========================================

    }

    // Basic ==========================================
    public void onBasic(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // delete 특정 메시지 삭제
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("chat")
                        .child("-Kc0U0ZM9sWYUq17_rYi")
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.i("CHAT", "삭제완료");
                                }else{
                                    Log.i("CHAT", "삭제실패");
                                }
                            }
                        });
            }
        });
    }
    // ================================================
}
