package com.example.rxjavaplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BufferClicksActivity extends AppCompatActivity {

    private Button bClick;
    private TextView tvMessage;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer_clicks);

        bClick = findViewById(R.id.bClick);
        tvMessage = findViewById(R.id.tvMessage);
        disposable = new CompositeDisposable();


    }
}
