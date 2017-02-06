package com.example.tacademy.recycleviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.tacademy.recycleviewtest.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends BaseActivity {

    EditText email_et, password_et;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email_et = (EditText)findViewById(R.id.email_et);
        password_et = (EditText)findViewById(R.id.password_et);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // 자동로그인 처리
        String email =
            StorageHelper.getInstance().getString(SignUpActivity.this, "email");
        String pwd =
            StorageHelper.getInstance().getString(SignUpActivity.this, "password");
        if(email != null && pwd != null && !email.equals("") && !pwd.equals("")){
            email_et.setText(email);
            password_et.setText(pwd);
            onLogin(null);
        }

    }

    public void onSignup(View view){
        if(!isValidate()) return;

        // 1. 로딩
        showProgress("회원가입중...");
        // 2. 이메일 비번 획득
        final String email = email_et.getText().toString();
        String pwd = password_et.getText().toString();
        // 3. 인증쪽에 디비에 입력
        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 4. 로딩닫기 -> 성공이든 실패든 닫는다
                        hideProgress();
                        // 성공일 경우, 실패일 경우
                        if(task.isSuccessful()){
                            // 4-1. 회원정보를 디비에 입력
                            onUserSaved(email);
                            // 5. 로그인 처리로 이동
                        }else{
                            Log.i("CHAT", "실패 : 관리실에 문의하시오.");
                        }
                    }
                });
    }

    // 로그인 버튼 클릭시
    public void onLogin(View view){
        if(!isValidate()) return;

        // 1. 로딩
        showProgress("로그인중...");
        // 2. 이메일 비번 획득
        final String email = email_et.getText().toString();
        final String pwd = password_et.getText().toString();
        // 3. 인증쪽에 디비에 입력
        firebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 4. 로딩닫기 -> 성공이든 실패든 닫는다
                        hideProgress();
                        // 성공일 경우, 실패일 경우
                        if(task.isSuccessful()){
                            Log.i("CHAT", "성공");

                            // 자동로그인을 위한 아이디, 비밀번호를 StorageHelper에 저장
                            StorageHelper.getInstance().setString(SignUpActivity.this, "email", email);
                            StorageHelper.getInstance().setString(SignUpActivity.this, "password", pwd);

                            // 서비스로 이동
                            goCenter();
                        }else{
                            // 실패
                            Log.i("CHAT", "실패" + task.getException());
                        }
                    }
                });

    }

    // 로그인 성공일 경우 서비스로 이동
    public void goCenter(){
        Intent intent = new Intent(this, CenterActivity.class);
        startActivity(intent);
        finish();
    }

    // 이메일과 비밀번호 비어있는지 확인
    public boolean isValidate(){
        if(TextUtils.isEmpty(email_et.getText().toString())){   // TextUtils.isEmpty : 비어있는지 아닌지 -> 널이나 공백 -> 이 경우는 공백
            email_et.setError("이메일을 입력하세요");
            return false;
        }else {
            String email = email_et.getText().toString();
//            boolean flag = Pattern.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+$", email);
//            if(!flag) {
//                email_et.setError("이메일 형식에 맞추어 입력하세요");
//                return false;
//            }
            email_et.setError(null);
        }
        if(TextUtils.isEmpty(password_et.getText().toString())){
            password_et.setError("비밀번호를 입력하세요");
            return false;
        }else{
            if(password_et.getText().toString().length()<6) {
                password_et.setError("비밀번호는 6자 이상이어야 합니다.");
                return false;
            }
            password_et.setError(null);
        }
        return true;
    }

    // 회원 정보 디비에 저장
    public void onUserSaved(String emailparam){

        // 회원 정보 디비에 입력
        String id = emailparam.split("@")[0];
        String email = emailparam;

        // 자바 클래스 User 만들기
        // 회원정보 생성
        User user = new User(id, email);

        // 디비 입력
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("users").child(uid).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            // 로그인
                        }else{

                        }
                    }
        });


    }
}
