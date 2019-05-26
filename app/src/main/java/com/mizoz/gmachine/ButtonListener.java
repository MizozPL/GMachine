package com.mizoz.gmachine;

import android.view.View;

public abstract class ButtonListener implements View.OnClickListener {

    private int position;
    public void updatePosition(int position){
        this.position = position;
    }

    public int getPosition(){
        return position;
    }

    @Override
    public void onClick(View view) {

    }
}
