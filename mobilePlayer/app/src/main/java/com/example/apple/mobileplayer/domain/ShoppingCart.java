package com.example.apple.mobileplayer.domain;

import java.io.Serializable;

/**
 * Created by apple on 17/3/17.
 */

public class ShoppingCart  extends ShoppingItemData implements Serializable{

    /**
     * 购买的数量
     */
    private int count = 1;

    /**
     * 是否勾选
     */
    private boolean isCheck = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "count=" + count +
                ", isCheck=" + isCheck +
                '}';
    }
}
