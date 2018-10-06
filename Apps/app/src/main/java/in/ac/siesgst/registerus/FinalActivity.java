package in.ac.siesgst.registerus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {



TextView ssn_textView1;
TextView pwd_textview1;
Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_poi);
        ssn_textView1=(TextView) findViewById(R.id.ssn_textview);
        pwd_textview1=(TextView) findViewById(R.id.pwd_textview);
        button=(Button) findViewById(R.id.backButton);



        ssn_textView1.setText(getIntent().getStringExtra("ssn"));
        pwd_textview1.setText(getIntent().getStringExtra("pwd"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in1=new Intent(FinalActivity.this,MainActivity.class);
                startActivity(in1);
            }
        });
    }
}