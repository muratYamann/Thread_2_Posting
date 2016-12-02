package com.yamankod.thread_2_posting;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private ProgressBar myBar;
    private TextView lblTopCaption;
    private EditText txtBox1;
    private Button btnDoSomething;
    int accum = 0;
    long startingMills = System.currentTimeMillis();
    boolean isRunning = false;
    private String PATIENCE = "Some important data is been collected now. "
            + "\nPlease be patient...wait... ";
    Handler myHandler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lblTopCaption = (TextView) findViewById(R.id.lblTopCaption);
        myBar = (ProgressBar) findViewById(R.id.myBar);
        myBar.setMax(100);
        txtBox1 = (EditText) findViewById(R.id.txtBox1);
        txtBox1.setHint("Foreground distraction. Enter some data here");
        btnDoSomething = (Button) findViewById(R.id.btnDoSomething);
        btnDoSomething.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable txt = txtBox1.getText();
                Toast.makeText(getBaseContext(), "You said >> " + txt, 1).show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Thread myThread1 = new Thread(backgroundTask, "backAlias1");
        myThread1.start();
        myBar.incrementProgressBy(0);
    }
    private Runnable foregroundTask = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                int progressStep = 5;
                double totalTime = (System.currentTimeMillis() - startingMills) / 1000;
                lblTopCaption.setText(PATIENCE + totalTime);
                myBar.incrementProgressBy(progressStep);
                accum += progressStep;
                if (accum >= myBar.getMax()) {
                    lblTopCaption.setText("Background work is OVER!");
                    myBar.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() {
            try {
                for (int n = 0; n < 20; n++) {
                    Thread.sleep(1000);
                    myHandler.post(foregroundTask);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}