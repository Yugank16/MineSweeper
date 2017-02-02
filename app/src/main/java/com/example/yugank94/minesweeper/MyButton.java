package com.example.yugank94.minesweeper;

import android.content.Context;
import android.widget.Button;

/**
 * Created by yugank94 on 1/2/17.
 */

public class MyButton extends Button {

    public boolean isOpen;
    public boolean isMine;
    public boolean isFirst;
    int count;


    public MyButton(Context context) {
        super(context);
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
