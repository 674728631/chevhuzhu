package com.zccbh.util.base;

import java.util.UUID;

/**
 * UUIDCreator
 *
 * @author
 * @date 2016/7/12
 */
public class UUIDCreator {
    public static final String getUUID(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }

    public static void main(String[] args){
        System.out.println(getUUID());
    }
}
