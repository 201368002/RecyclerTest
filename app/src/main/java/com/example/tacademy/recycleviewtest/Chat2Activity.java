package com.example.tacademy.recycleviewtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class Chat2Activity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        onBasic();

        // 일단 글쓰기 ================================
        // 1. 파베 디비 객체 획득
        firebaseDatabase = FirebaseDatabase.getInstance();
        // 2. 디비를 access 할 수 있는 참조 값 (root) 획득
        databaseReference = firebaseDatabase.getReference();
        // 3. 적절한 경로 (가지를 하나 만들어서)에다 메시지 입력
        /*
        databaseReference.child("chat").push()
                .setValue(new ChatMessage("삼다수", "하이"))
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("CHAT", "등록완료");
                }else{
                    Log.i("CHAT", "등록실패");
                }
            }
        }); //push()는 가지에 대한 중복되지 않는 키가 하나 만들어 진다. 거기에 값을 넣는 것이 setValue()
        */
        // 4. select 등록된 데이터 가져오기
        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // s는 전번 데이터의 키를 가르킨다 (링크드리스트) -> 실질적으로 순차적으로 오기때문에 s는 잘 사용하지 X
                //Log.i("CHAT", dataSnapshot.toString() + "/" + s);
                // 파싱 : 데이터를 ChatMessage틀에 넣어준다.
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                Log.i("CHAT", chatMessage.getUsername());
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
        });
        // ===========================================

    }

    public void setChat(String Message){
        databaseReference.child("chat").push()
                .setValue(new ChatMessage("삼다수", Message))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.i("CHAT", "등록완료");
                        }else{
                            Log.i("CHAT", "등록실패");
                        }
                    }
                }); //push()는 가지에 대한 중복되지 않는 키가 하나 만들어 진다. 거기에 값을 넣는 것이 setValue()
    }


    // Basic ==========================================
    public void onBasic(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    // ================================================
}
