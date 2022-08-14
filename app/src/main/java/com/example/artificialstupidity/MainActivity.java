package com.example.artificialstupidity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // start main game
    public void startMainGame(View view) {
        startActivity(new Intent(this, MainGameActivity.class));
    }

    public void startAbout(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

}