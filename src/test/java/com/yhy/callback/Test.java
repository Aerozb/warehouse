package com.yhy.callback;

public class Test {
    public static void main(String[] args) {
        Printer printer=new Printer();
        People people=new People(printer);
        people.startPrint();
    }
}
