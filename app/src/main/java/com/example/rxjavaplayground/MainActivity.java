package com.example.rxjavaplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    private Button bCreate,bFromIterable,bRangeRepeat;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            switch(v.getId()) {
                case R.id.bCreate:
                    intent = new Intent(MainActivity.this,CreateActivity.class);
                    break;
                case R.id.bFromIterable:
                    intent = new Intent(MainActivity.this,FromIterableActivity.class);
                    break;
                case R.id.bRangeRepeat:
                    intent = new Intent(MainActivity.this,RangeRepeatActivity.class);
                default:
                    break;
            }
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    public void initViews() {
        bCreate = findViewById(R.id.bCreate);
        bCreate.setOnClickListener(listener);
        bFromIterable = findViewById(R.id.bFromIterable);
        bFromIterable.setOnClickListener(listener);
        bRangeRepeat = findViewById(R.id.bRangeRepeat);
        bRangeRepeat.setOnClickListener(listener);
    }

}
