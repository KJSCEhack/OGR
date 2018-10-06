package in.ac.siesgst.registerus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText nameField;
    EditText emailField;
    Button sbmtBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        sbmtBtn = findViewById(R.id.nextUniversal);

        sbmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UploadPoi.class);
                intent.putExtra("name",nameField.getText().toString());
                intent.putExtra("email",emailField.getText().toString());
                startActivity(intent);
            }
        });
    }
}
