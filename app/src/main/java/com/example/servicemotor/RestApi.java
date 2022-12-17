package com.example.servicemotor;

import java.util.ArrayList;

public class RestApi {
    private String next;
    private Object previous;
    private int count;
    public ArrayList<ResultItem> results;
    public String getNext(){
        return next;
    }
    public Object getPrevious(){
        return previous;
    }
    public int getCount(){
        return count;
    }
    public ArrayList<ResultItem> getResult(){
        return results;
    }
}

