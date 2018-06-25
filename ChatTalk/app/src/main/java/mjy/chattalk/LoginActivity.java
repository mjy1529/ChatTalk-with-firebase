package mjy.chattalk;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class LoginActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private Button loginActivity_loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance(); //싱글톤 선언
        String splash_background = mFirebaseRemoteConfig.getString("splash_background");
        //getWindow().setStatusBarColor(splash_background);

        loginActivity_loginBtn = (Button) findViewById(R.id.loginActivity_loginBtn);


    }
}
