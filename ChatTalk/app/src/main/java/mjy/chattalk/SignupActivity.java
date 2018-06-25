package mjy.chattalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import mjy.chattalk.model.UserModel;

public class SignupActivity extends AppCompatActivity {

    EditText signup_emailEt;
    EditText signup_nameEt;
    EditText signup_passwordEt;
    Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_emailEt = (EditText) findViewById(R.id.signup_emailEt);
        signup_nameEt = (EditText) findViewById(R.id.signup_nameEt);
        signup_passwordEt = (EditText) findViewById(R.id.signup_passwordEt);
        signupBtn = (Button) findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (signup_emailEt.getText().toString() == null || signup_nameEt.getText().toString() == null
                        || signup_passwordEt.getText().toString() == null) {
                    Toast.makeText(SignupActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(signup_emailEt.getText().toString(), signup_passwordEt.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                UserModel userModel = new UserModel();
                                userModel.userName = signup_nameEt.getText().toString();

                                String uid = task.getResult().getUser().getUid();
                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
                            }
                        });
            }
        });

    }
}
