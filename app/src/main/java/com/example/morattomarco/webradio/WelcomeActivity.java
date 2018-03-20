package com.example.morattomarco.webradio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class WelcomeActivity extends AppCompatActivity {

    private EditText nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Button binizia= (Button) findViewById(R.id.Bsend);

        nick=(EditText) findViewById(R.id.Enick);
    }

    public void GoMain(View View){
        Log.i("INFO","PREME PULSANTE");
        Intent intent =new Intent(this, MainActivity.class);
        String message=nick.getText().toString();
        Log.i("INFO","INSERISCE INTENT");
        intent.putExtra(EXTRA_MESSAGE,message);
        Log.i("INFO","PARTE INTENT");
        startActivity(intent);

    }
}
