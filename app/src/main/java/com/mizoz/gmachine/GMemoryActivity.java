package com.mizoz.gmachine;

import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import java.math.BigInteger;
import java.util.HashMap;

public class GMemoryActivity extends AppCompatActivity {

    private EditText address, value;

    private static final String ADDRESS_TAG = "address";
    private static final String VALUE_TAG = "value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmemory);

        address = (EditText) findViewById(R.id.edittext_memory_address);
        value = (EditText) findViewById(R.id.edittext_memory_value);

        if(savedInstanceState != null){
            address.setText(savedInstanceState.getString(ADDRESS_TAG));
            value.setText(savedInstanceState.getString(VALUE_TAG));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ADDRESS_TAG, address.getText().toString());
        outState.putString(VALUE_TAG, value.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void writeMemory(View v){
        BigInteger mAddress, mValue;
        try {
            mAddress = new BigInteger(address.getText().toString());
            mValue = new BigInteger(value.getText().toString());
        } catch (NumberFormatException ex) {
            Snackbar.make(findViewById(R.id.layout_memory_constraint), "Invalid input!", Snackbar.LENGTH_SHORT).setAction("OK", null).show();
            return;
        }

        GMachineActivity.memoryHashMap.put(mAddress, mValue);
    }

    public void readMemory(View v){
        BigInteger mAddress, mValue;
        try {
            mAddress = new BigInteger(address.getText().toString());
        } catch (NumberFormatException ex) {
            Snackbar.make(findViewById(R.id.layout_memory_constraint), "Invalid input!", Snackbar.LENGTH_SHORT).setAction("OK", null).show();
            return;
        }

        mValue = GMachineActivity.memoryHashMap.get(mAddress);
        if(mValue != null)
            value.setText(mValue.toString());
        else
            value.setText("0");
    }
}
