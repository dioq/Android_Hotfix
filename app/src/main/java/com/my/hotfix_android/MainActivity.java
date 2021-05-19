package com.my.hotfix_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.my.hotfix_android.hotfix.Hotfix;
import com.my.hotfix_android.judge.JudgeInputStr;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
    }

    public void submit_func(View view) {
        String input_str = editText.getText().toString();
        JudgeInputStr judgeInputStr = new JudgeInputStr();
        String result = judgeInputStr.judgeSign(input_str);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

}
