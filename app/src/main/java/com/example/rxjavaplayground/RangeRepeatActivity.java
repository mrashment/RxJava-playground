package com.example.rxjavaplayground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RangeRepeatActivity extends AppCompatActivity {

    private CompositeDisposable disposable;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        disposable = new CompositeDisposable();
        tvContent = findViewById(R.id.tvContent);
        tvContent.append("Looping 3 times");

        // creates an observable from specified range of numbers
        Observable<Integer> observable = Observable.range(0,5)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Throwable {
                        Thread.sleep(100);
                        return true;
                    }
                })
                .map(k -> 10 - k)
                .observeOn(AndroidSchedulers.mainThread());


        observable
                .repeat(3)  // repeats the observable 3 times (actually returns a new one I think).
                            // Can call with no arguments to repeat infinitely.
                .subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                tvContent.append("\n" + integer);
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
