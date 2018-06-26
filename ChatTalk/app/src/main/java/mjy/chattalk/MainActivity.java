package mjy.chattalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mjy.chattalk.fragment.PeopleFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.frameLayout, new PeopleFragment()).commit();
    }
}
