package com.artoymdmitriev.bsacschedule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.artoymdmitriev.bsacschedule.R;
import com.artoymdmitriev.bsacschedule.logic.CustomScheduleInfo;


public class SelectGroupActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {

    private EditText editText;
    private Spinner spinner;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group2);
        setUpEditText();
        setUpSpinnerParams();
        setUpSubmitButton();
        radioGroup = (RadioGroup) findViewById(R.id.radioSub);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            editText.getText().clear();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getBaseContext(), DownloadActivity.class);
        CustomScheduleInfo customScheduleInfo = new CustomScheduleInfo();
        setCustomScheduleInfo(customScheduleInfo);
        intent.putExtra("CustomScheduleInfo", customScheduleInfo);
        startActivity(intent);
    }

    private void setCustomScheduleInfo(CustomScheduleInfo customScheduleInfo) {
        customScheduleInfo.setGroupName(editText.getText().toString());
        int subGroup;
        if("подгруппа 1".equals(((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString()))
            subGroup = 1;
        else
            subGroup = 0;
        customScheduleInfo.setSubGroup(subGroup);
        customScheduleInfo.setCurrentWeek(Integer.parseInt(spinner.getSelectedItem().toString()));
    }

    private void setUpEditText() {
        editText = (EditText) findViewById(R.id.editText);
        editText.setOnFocusChangeListener(this);
    }

    private void setUpSpinnerParams() {
        spinner = (Spinner) findViewById(R.id.weeks_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.weeks_array, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setUpSubmitButton() {
        Button button = (Button) findViewById(R.id.submitButton);
        button.setOnClickListener(this);
    }
}
