package com.lab1kobiela.bmi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final float BMI_VALUE_BAD_BOTTOM = 16;
    private static final float BMI_VALUE_BAD_TOP = 35;
    private static final float BMI_VALUE_GOOD_BOTTOM = 18.5f;
    private static final float BMI_VALUE_GOOD_TOP = 25;
    private static final float LB_TO_KG = 0.454f;
    private static final float IN_TO_M = 0.0254f;


    @BindView(R.id.swUnitChoice)
    Switch sw_Unit;
    @BindView(R.id.etMass)
    EditText et_Mass;
    @BindView(R.id.etHeight)
    EditText et_Height;
    @BindView(R.id.tvBMIResult)
    TextView tv_Result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        readSavedValues();
    }

    private void readSavedValues() {
        sharedPreferences = getSharedPreferences("com.lab1kobiela.bmi", Context.MODE_PRIVATE);

        float m = sharedPreferences.getFloat("Mass", 0f);
        float h = sharedPreferences.getFloat("Height", 0f);
        if (m > 0 && h > 0) {
            et_Mass.setText(String.valueOf(m));
            et_Height.setText(String.valueOf(h));
            sw_Unit.setChecked(sharedPreferences.getBoolean("UnitLbIn", false));
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            float fSavedValue = savedInstanceState.getFloat("BMIvalue");
            if (fSavedValue != 0f) {
                if (fSavedValue == -1.0f) printWrongData();
                else printResult(fSavedValue);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!tv_Result.getText().toString().equals("")) {
            String value = tv_Result.getText().toString();
            value = value.replace(",", ".");
            if (value.equals(getString(R.string.wrong_data)))
                value = "-1.0";
            outState.putFloat("BMIvalue", Float.valueOf(value));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //enabling save button
        try {
            countBMI();
            menu.getItem(0).setEnabled(true);
        } catch (Exception e) {
            menu.getItem(0).setEnabled(false);
        }

        //enabling share button
        if (tv_Result.getText().toString().equals("") || tv_Result.getText().toString().equals(getString(R.string.wrong_data)))
            menu.getItem(1).setEnabled(false);
        else menu.getItem(1).setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                saveMassAndHeight();
                break;
            case R.id.action_share:
                shareResult();
                break;
            case R.id.action_author:
                showAuthor();
                break;
        }
        return true;
    }


    @OnClick(R.id.bCount)
    public void countBMIOnClick() {
        hideSoftKeyboard();
        try {
            printResult(countBMI());
        } catch (Exception e) {
            printWrongData();
        }
    }


    private void saveMassAndHeight() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("Mass", Float.valueOf(et_Mass.getText().toString()));
        editor.putFloat("Height", Float.valueOf(et_Height.getText().toString()));
        editor.putBoolean("UnitLbIn", sw_Unit.isChecked());
        editor.apply();
    }

    private void shareResult() {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        myIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_BMI_title));
        myIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_BMI_text) + " " + tv_Result.getText().toString());
        startActivity(Intent.createChooser(myIntent, getString(R.string.share_using_title)));
    }

    private void showAuthor() {
        Intent intentAuthor = new Intent(MainActivity.this, AuthorActivity.class);
        startActivity(intentAuthor);
    }

    private float countBMI() {
        CountBMIForKgM counter = new CountBMIForKgM();
        float mass = Float.valueOf(et_Mass.getText().toString());
        float height = Float.valueOf(et_Height.getText().toString());
        if (sw_Unit.isChecked()) {
            mass = mass * LB_TO_KG;
            height = height * IN_TO_M;
        }
        return counter.countBMI(mass, height);
    }

    @SuppressLint("DefaultLocale")
    private void printResult(float BMIvalue) {
        changeOutputColor(BMIvalue);
        tv_Result.setText(String.format("%.2f", BMIvalue));
    }


    private void printWrongData() {
        tv_Result.setTextColor(ContextCompat.getColor(this, R.color.colorVeryBadBMIOrWrongData));
        tv_Result.setText(getString(R.string.wrong_data));
    }


    private void changeOutputColor(float BMIvalue) {
        if (BMIvalue < BMI_VALUE_BAD_BOTTOM || BMIvalue >= BMI_VALUE_BAD_TOP)
            tv_Result.setTextColor(ContextCompat.getColor(this, R.color.colorVeryBadBMIOrWrongData));
        else if (BMIvalue < BMI_VALUE_GOOD_BOTTOM || BMIvalue >= BMI_VALUE_GOOD_TOP)
            tv_Result.setTextColor(ContextCompat.getColor(this, R.color.colorBadBMI));
        else tv_Result.setTextColor(ContextCompat.getColor(this, R.color.colorGoodBMI));
    }

    private void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }
    }


}
