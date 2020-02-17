package com.yhy.callback;

public class People implements Callback {

    private Printer printer;

    public People(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void printFinished(String msg) {
        System.out.println("打印出的内容:" + msg);
    }

    public void startPrint() {
        //异步回调
        new Thread(new Runnable() {
            public void run() {
                printer.print(People.this);
            }
        }).start();

        /*
        同步回调
        printer.print(People.this);
        */
    }


}

