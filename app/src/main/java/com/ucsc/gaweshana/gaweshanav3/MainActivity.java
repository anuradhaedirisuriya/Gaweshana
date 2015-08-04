package com.ucsc.gaweshana.gaweshanav3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }



    public void feedback(View view) {
        startActivity(new Intent(MainActivity.this, SingleActivity.class));
    }
    public void scan(View view) {
        startActivity(new Intent(MainActivity.this, ScanActivity.class));
    }
    public void exit(View view) {
        finish();
        System.exit(0);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



}
