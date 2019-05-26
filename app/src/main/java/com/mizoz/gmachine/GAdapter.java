package com.mizoz.gmachine;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mizoz.gmachine.processing.Instruction;

import java.math.BigInteger;
import java.util.HashMap;

public class GAdapter extends RecyclerView.Adapter<GAdapter.ViewHolder> {
    private HashMap<BigInteger, Instruction> instructions;
    public ArrayAdapter<CharSequence> arrayAdapter;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText label, args, comment;
        public TextView instructionID;
        public Spinner selectedInstruction;
        public EditTextListener l0, l1, l2;
        public SpinnerListener l3;


        public ViewHolder(ConstraintLayout v, ArrayAdapter<CharSequence> adapter, EditTextListener l0, EditTextListener l1, EditTextListener l2, SpinnerListener l3) {
            super(v);
            this.l0 = l0;
            this.l1 = l1;
            this.l2 = l2;
            this.l3 = l3;
            for (int i = 0; i < v.getChildCount(); i++) {
                View view = v.getChildAt(i);
                if (view.getId() == R.id.textView_instructionID) {
                    instructionID = (TextView) view;
                    continue;
                }
                if (view.getId() == R.id.editText_label) {
                    label = (EditText) view;
                    label.addTextChangedListener(this.l0);
                    continue;
                }
                if (view.getId() == R.id.spinner_selectedInstruction) {
                    selectedInstruction = (Spinner) view;
                    selectedInstruction.setAdapter(adapter);
                    selectedInstruction.setOnItemSelectedListener(l3);
                    continue;
                }
                if (view.getId() == R.id.editText_args) {
                    args = (EditText) view;
                    args.addTextChangedListener(this.l1);
                    continue;
                }
                if (view.getId() == R.id.editText_comment) {
                    comment = (EditText) view;
                    comment.addTextChangedListener(this.l2);
                    continue;
                }
            }
        }
    }

    public GAdapter(HashMap<BigInteger, Instruction> instructions, ArrayAdapter<CharSequence> adapter) {
        this.instructions = instructions;
        arrayAdapter = adapter;
    }

    @Override
    public GAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_row_view, parent, false);
        EditTextListener l0 = new EditTextListener() {
            @Override
            public void changed(CharSequence charSequence) {
                BigInteger integer = BigInteger.valueOf(this.getPosition());
                instructions.get(integer).setLabel(String.valueOf(charSequence));
            }
        };
        EditTextListener l1 = new EditTextListener() {
            @Override
            public void changed(CharSequence charSequence) {
                BigInteger integer = BigInteger.valueOf(this.getPosition());
                instructions.get(integer).setArgs(String.valueOf(charSequence));
            }
        };
        EditTextListener l2 = new EditTextListener() {
            @Override
            public void changed(CharSequence charSequence) {
                BigInteger integer = BigInteger.valueOf(this.getPosition());
                instructions.get(integer).setComment(String.valueOf(charSequence));
            }
        };
        SpinnerListener l3 = new SpinnerListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BigInteger integer = BigInteger.valueOf(this.getPosition());
                instructions.get(integer).setInstruction(adapterView.getItemAtPosition(i).toString());
            }
        };
        ViewHolder vh = new ViewHolder(v, arrayAdapter, l0, l1, l2, l3);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BigInteger bPosition = BigInteger.valueOf(position);
        holder.l0.updatePosition(position);
        holder.l1.updatePosition(position);
        holder.l2.updatePosition(position);
        holder.l3.updatePosition(position);
        holder.instructionID.setText(String.valueOf(position));
        if(position == GMachineActivity.getHighlightedPos())
            holder.instructionID.setTextColor(Color.RED);
        else
            holder.instructionID.setTextColor(Color.BLACK);
        holder.label.setText(instructions.get(bPosition).getLabel());
        holder.selectedInstruction.setSelection(arrayAdapter.getPosition(instructions.get(bPosition).getInstruction()));
        holder.args.setText(instructions.get(bPosition).getArgs());
        holder.comment.setText(instructions.get(bPosition).getComment());
        boolean enabled = GMachineActivity.getEnabledParam();
        holder.selectedInstruction.setEnabled(enabled);
        holder.args.setEnabled(enabled);
        holder.label.setEnabled(enabled);
        holder.comment.setEnabled(enabled);
    }



    @Override
    public int getItemCount() {
        return instructions.size();
    }


}
