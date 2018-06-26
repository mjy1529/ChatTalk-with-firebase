package mjy.chattalk;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import mjy.chattalk.model.UserModel;

public class SignupActivity extends AppCompatActivity {

    private EditText signup_emailEt;
    private EditText signup_nameEt;
    private EditText signup_passwordEt;
    private Button signupBtn;
    private ImageView profileIv;
    private Uri profileUri;

    final static int GALLERY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (signup_emailEt.getText().toString() == null || signup_nameEt.getText().toString() == null
                        || signup_passwordEt.getText().toString() == null || profileUri == null) {
                    Toast.makeText(SignupActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(signup_emailEt.getText().toString(), signup_passwordEt.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid(); //user 고유의 id가 uid

                                //이미지 경로를 저장
                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(profileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        @SuppressWarnings("VisibleForTests")
                                        String imageUrl = task.getResult().getDownloadUrl().toString();

                                        UserModel userModel = new UserModel();
                                        userModel.userName = signup_nameEt.getText().toString();
                                        userModel.userProfile = imageUrl;
                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        SignupActivity.this.finish(); //인터페이스 안에 있을 때는 이렇게 finish
                                                    }
                                                });
                                    }
                                });
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("uri", "uri : " + data.getData());
            profileIv.setImageURI(data.getData());
            profileUri = data.getData(); //이미지 경로 저장
        }
    }

    public void init() {
        signup_emailEt = (EditText) findViewById(R.id.signup_emailEt);
        signup_nameEt = (EditText) findViewById(R.id.signup_nameEt);
        signup_passwordEt = (EditText) findViewById(R.id.signup_passwordEt);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        profileIv = (ImageView) findViewById(R.id.profileIv);
    }
}
