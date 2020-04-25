package com.example.rxjavaplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FromIterableActivity extends AppCompatActivity {

    private static final String TAG = "FromIterableActivity";

    private TextView tvContent;
    private Random rnd;

    private CompositeDisposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        this.tvContent = findViewById(R.id.tvContent);

        List<String> list = Arrays.asList("Apple","Banana","Carrot","Donut","Egg");

        disposable = new CompositeDisposable();

        Observable<String> observable = Observable.fromIterable(list)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Throwable {
                        Thread.sleep(1000);
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "onNext: " + s);
                FromIterableActivity.this.tvContent.append("\n" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                tvContent.append("\nFinished");
            }
        });

    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
