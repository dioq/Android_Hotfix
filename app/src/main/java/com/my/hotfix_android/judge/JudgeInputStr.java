package com.my.hotfix_android.judge;

public class JudgeInputStr {
    public String judgeSign(String param) {
        if (param.equals("success")) {
            return "Right from origin code";
        } else {
            return "Wrong from origin code";
        }
    }
}
