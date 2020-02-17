package com.yhy.warehouse;

import org.junit.jupiter.api.Test;

import java.io.Serializable;

public class IntegerIsImplSerializable {

    @Test
    public void tets(){
        System.out.println( Integer.class instanceof Serializable);
    }
}
