package com.my.hotfix_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.my.hotfix_android.hotfix.Hotfix;
import com.my.hotfix_android.judge.JudgeInputStr;
import com.my.hotfix_android.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        new Hotfix().dynamicUpdate(this);
    }

    public void submit_func(View view) {
        String input_str = editText.getText().toString();
        JudgeInputStr judgeInputStr = new JudgeInputStr();
        String result = judgeInputStr.judgeSign(input_str);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

}
