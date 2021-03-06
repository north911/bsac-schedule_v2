package com.adanilenka.bsacschedule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adanilenka.bsacschedule.Entity.WeekSchedule;
import com.adanilenka.bsacschedule.R;
import com.adanilenka.bsacschedule.logic.AsyncParser;
import com.adanilenka.bsacschedule.logic.CustomScheduleInfo;
import com.adanilenka.bsacschedule.logic.DBHelper;
import com.adanilenka.bsacschedule.logic.ResultsListener;

import java.util.ArrayList;

public class DownloadActivity extends AppCompatActivity implements ResultsListener {
    TextView textView;
    ProgressBar progressBar;
    ImageView imageView;
    RelativeLayout relativeLayout;
    CustomScheduleInfo customScheduleInfo;
    ArrayList<WeekSchedule> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        Intent i = getIntent();
        customScheduleInfo = (CustomScheduleInfo) i.getSerializableExtra("CustomScheduleInfo");

        textView = (TextView) findViewById(R.id.textView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        parseNewItems();
    }

    @Override
    public void onResultSucceeded(Object obj) {
        relativeLayout.removeView(progressBar);
        arrayList = (ArrayList) obj;

        if (arrayList != null) {
            DBHelper dbHelper = new DBHelper(this);
            dbHelper.addScheduleInfo(customScheduleInfo);

            for (WeekSchedule weekSchedule : arrayList) {
                dbHelper.setScheduleToDb(weekSchedule);
            }
        } else {
            showAlert();
        }
        textView.setText("Расписание загружено!");
        Handler handler = new Handler();
        imageView = (ImageView) findViewById(R.id.imageView);
        handler.postDelayed(() -> imageView.setImageResource(R.drawable.success), 3000);
    }

    private void showAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Ошибка!")
                .setMessage("Похоже что группа которую вы ввели не существует, " +
                        "либо расписание для нее отсутствует на сайте")
                .setPositiveButton("ввести другую группу", (dialog, which) -> System.exit(0))
                .setIcon(R.drawable.error)
                .show();
    }

    private void parseNewItems() {
        AsyncParser parser = new AsyncParser();
        parser.setResultsListener(this);
        parser.execute(customScheduleInfo);
    }

    public void showSchedule(View v) {
        Intent intent = new Intent(getBaseContext(), FullScheduleActivity.class);
        intent.putExtra("Schedule", arrayList);
        System.out.println(customScheduleInfo.toString());
        startActivity(intent);
    }
}
