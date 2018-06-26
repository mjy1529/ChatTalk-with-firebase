package mjy.chattalk.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import mjy.chattalk.R;
import mjy.chattalk.model.ChatModel;

public class MessageActivity extends AppCompatActivity {

    private String destinationUID;
    private Button sendBtn;
    private EditText messageEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        destinationUID = getIntent().getStringExtra("destinationUID");

        sendBtn = (Button)findViewById(R.id.sendBtn);
        messageEt = (EditText)findViewById(R.id.messageEt);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //내 uid
                chatModel.destinationUID = destinationUID; //상대방 uid

                FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel);
            }
        });

    }
}
