package id.ac.umn.jameschristianwira;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String DB_NAME = "credential.db";
    TextView testUsername, testPassword;
    EditText edtUsername, edtPassword;
    String username, password;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testUsername = findViewById(R.id.test_username);
        testPassword = findViewById(R.id.test_password);

        btnLogin = findViewById(R.id.main_btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Login button clicked", Toast.LENGTH_SHORT).show();

                DBAdapter dbAdapter = new DBAdapter(getApplicationContext(), DB_NAME, null, 1);
                dbAdapter.openDatabase();

                Cursor cursor = dbAdapter.getCredential();

                cursor.moveToFirst();
                Log.d(this.getClass().toString(), "Start cursor");
                if(cursor.getString(1) == null) Log.d(this.getClass().toString(), "No data");
                else Log.d(this.getClass().toString(), cursor.getString(1));
                Log.d(this.getClass().toString(), "End cursor");

                //cursor.moveToFirst();

                username = cursor.getString(0);
                password = cursor.getString(1);

                Log.d(this.getClass().toString(), username);
                Log.d(this.getClass().toString(), password);

                testUsername.setText(username);
                testPassword.setText(password);

                dbAdapter.close();
            }
        });
    }
}
