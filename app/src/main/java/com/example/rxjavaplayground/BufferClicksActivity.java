package com.example.rxjavaplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import kotlin.Unit;

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

        // registers clicks on a view to a new observable. This one buffers, meaning it aggregates several events before emitting them in a single object
        RxView.clicks(bClick)
                .map(u -> 1)
                .buffer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        tvMessage.setText("You clicked " + integers.size() + " times in 1 second.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
