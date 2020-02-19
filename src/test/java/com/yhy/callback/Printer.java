package com.yhy.callback;

public class Printer {


    public void print(Callback callback) {
        System.out.println("打印中......请等待");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callback.printFinished("牛逼我打印出了");
    }
}
