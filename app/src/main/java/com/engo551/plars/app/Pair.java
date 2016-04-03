package com.engo551.plars.app;

/**
 * Created by Xueyang Zou on 2016-04-01.
 */
public class Pair implements Comparable<Pair> {
    public int sort;
    public double index;

    public Pair (int sort, double index){
        this.sort = sort;
        this.index = index;
    }


    @Override
    public int compareTo(Pair another) {
        return -1*Double.valueOf(this.index).compareTo(another.index);
    }


}
