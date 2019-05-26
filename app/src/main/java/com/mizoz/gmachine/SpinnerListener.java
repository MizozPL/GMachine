package com.mizoz.gmachine;

import android.view.View;
import android.widget.AdapterView;


public abstract class SpinnerListener implements AdapterView.OnItemSelectedListener {

    private int position;
    public void updatePosition(int position){
        this.position = position;
    }
    public int getPosition(){return position;}

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
