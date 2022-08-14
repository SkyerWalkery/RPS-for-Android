package com.example.artificialstupidity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void jumpToGithub(View view) {
        openWebPage(getString(R.string.open_source_url));
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.fail_to_find_browser),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    public void clickVersionTextView(View view) {
        int min_api = getApplicationContext().getApplicationInfo().minSdkVersion;
        int target_api = getApplicationContext().getApplicationInfo().targetSdkVersion;
        Toast.makeText(
                getApplicationContext(),
                getString(R.string.sdk_version_info, min_api, target_api),
                Toast.LENGTH_SHORT
        ).show();
    }

    public void clickSloganTextView(View view) {
        Toast.makeText(
                getApplicationContext(),
                getString(R.string.app_slogan),
                Toast.LENGTH_SHORT
        ).show();
    }

}