package com.example.rxjavaplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class ThrottleActivity extends AppCompatActivity {

    private Button bClick;
    private TextView tvMessage;
    private CompositeDisposable disposable;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer_clicks);

        currentTime = System.currentTimeMillis();

        bClick = findViewById(R.id.bClick);
        tvMessage = findViewById(R.id.tvMessage);
        disposable = new CompositeDisposable();

        RxView.clicks(bClick)
                .throttleFirst(4, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        tvMessage.setText("Last object emitted " + (System.currentTimeMillis() - currentTime)/1000.0 + " seconds ago.");
                        currentTime = System.currentTimeMillis();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete(){
                    }
                });

    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
