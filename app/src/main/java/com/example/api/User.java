package com.example.api;

public class User {
    private String prize_name;
    private String prize_num;
    private String prize_amount;
    private String prize_require;

    @Override
    public String toString() {
        return
                 "本期"+prize_name +" "+
                " 中奖人数:" + prize_num  + "人 "+
                " 中奖金额:" + prize_amount +"元 "+
                " 中奖要求:" + prize_require+" " ;
    }
}
