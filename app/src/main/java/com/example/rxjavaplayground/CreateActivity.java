package com.example.rxjavaplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateActivity";
    private TextView tvContent;
    private Random rnd;

    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        rnd = new Random();
        tvContent = findViewById(R.id.tvContent);

        // the disposable prevents memory leaks, clear it when done with your tasks
        disposable = new CompositeDisposable();

        // creates a basic observable which triggers its observers 10 times
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                if (!emitter.isDisposed()) {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(500);
                        emitter.onNext(rnd.nextInt());
                    }
                    emitter.onComplete();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                tvContent.append("\nNext random number is " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                tvContent.append("\nCreate Finished.");
            }
        });

        tvContent.append("\n\nAlso testing 'just' operator here");

        // an easy way to create an observable from a few objects
        Observable<Integer> justObservable = Observable.just(1,2,3,4,5,6)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Throwable {
                        Thread.sleep(rnd.nextInt(1000));
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        justObservable.subscribe(new Observer<Integer>() {
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
                tvContent.append("\nJust Finished");
            }
        });

        // Flowables have backpressure (or handle backpressure?), meaning they supposedly handle overflowing or
        // abnormally terminating events better. Or something.
        Flowable.just(15,20,25,30).subscribe(System.out::println);
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
