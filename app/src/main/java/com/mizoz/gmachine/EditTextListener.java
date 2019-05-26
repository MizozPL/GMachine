package com.mizoz.gmachine;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class EditTextListener implements TextWatcher {

    private int position;
    public void updatePosition(int position){
        this.position = position;
    }
    public int getPosition(){return position;}
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            changed(charSequence);
    }

    public abstract void changed(CharSequence charSequence);

    @Override
    public void afterTextChanged(Editable editable) {

    }
}