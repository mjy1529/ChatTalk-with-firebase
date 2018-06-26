package mjy.chattalk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class LoginActivity extends AppCompatActivity {

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Button loginBtn;
    private TextView signupTv;
    private EditText idEt;
    private EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance(); //싱글톤 선언
        String splash_background = firebaseRemoteConfig.getString("splash_background");
        //getWindow().setStatusBarColor(splash_background);

        firebaseAuth = FirebaseAuth.getInstance(); //싱글톤 선언
        firebaseAuth.signOut();

        loginBtn = (Button) findViewById(R.id.loginBtn);
        signupTv = (TextView) findViewById(R.id.signupTv);
        idEt = (EditText) findViewById(R.id.idEt);
        passwordEt = (EditText) findViewById(R.id.passwordEt);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEvent();
            }
        });

        signupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        //로그인 인터페이스 리스너, 로그인 되었는지 확인
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) { //로그인
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else { //로그아웃

                }
            }
        };
    }

    void loginEvent() {
        firebaseAuth.signInWithEmailAndPassword(idEt.getText().toString(), passwordEt.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() { //로그인이 정상적으로 되었는지 판단해주는 역할
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) { //로그인 실패하였을 경우
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
