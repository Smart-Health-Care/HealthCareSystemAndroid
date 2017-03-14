package io.iqube.healthcaresystem.NotNeeded;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import io.iqube.healthcaresystem.R;

public class RegistrationActivity extends AppCompatActivity {
    EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        username = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

    }
}
