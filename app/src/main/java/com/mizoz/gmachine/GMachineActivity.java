package com.mizoz.gmachine;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.mizoz.gmachine.processing.GExecutionException;
import com.mizoz.gmachine.processing.GMachine;
import com.mizoz.gmachine.processing.Instruction;
import com.mizoz.gmachine.processing.TaskFragment;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class GMachineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText outputText;
    private EditText inputText;
    private TaskFragment taskFragment;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;

    public static HashMap<BigInteger, Instruction> instructionHashMap = new HashMap<>();
    public static HashMap<BigInteger, BigInteger> memoryHashMap = new HashMap<>();
    public static ArrayList<String> inputArray = new ArrayList<>();

    private static final String TAG_TASK_FRAGMENT = "GMachine_Task";

    private static boolean debug = false;
    private static boolean run = false;
    public static int highlightedPos = 0;

    public static int getHighlightedPos() {
        return highlightedPos;
    }

    private static GMachine gMachine = new GMachine(inputArray, memoryHashMap, instructionHashMap);


    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmachine);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layout_coordinator);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_instruction);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.instruction_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter = new GAdapter(instructionHashMap, arrayAdapter);
        recyclerView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.floating_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigInteger integer = BigInteger.valueOf(adapter.getItemCount());
                Instruction instruction = new Instruction("", "-", "", "");
                instructionHashMap.put(integer, instruction);
                adapter.notifyDataSetChanged();
            }
        });

        outputText = (EditText) findViewById(R.id.edittext_output);
        inputText = (EditText) findViewById(R.id.edittext_input);
        taskFragment = (TaskFragment) getFragmentManager().findFragmentByTag(TAG_TASK_FRAGMENT);
        if (taskFragment == null) {
            taskFragment = new TaskFragment();
        }

        addDeleteAction();
        updateEnabledParam();
    }

    private void addDeleteAction() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (viewHolder == target || run || debug)
                    return false;

                int a, b;
                a = viewHolder.getAdapterPosition();
                b = target.getAdapterPosition();

                BigInteger ba = BigInteger.valueOf(a);
                BigInteger bb = BigInteger.valueOf(b);

                Instruction temp = instructionHashMap.get(ba);
                instructionHashMap.put(ba, instructionHashMap.get(bb));
                instructionHashMap.put(bb, temp);

                ((GAdapter.ViewHolder) viewHolder).instructionID.setText(String.valueOf(b));
                ((GAdapter.ViewHolder) target).instructionID.setText(String.valueOf(a));

                ((GAdapter.ViewHolder) viewHolder).instructionID.setTextColor(b == 0 ? Color.RED : Color.BLACK);
                ((GAdapter.ViewHolder) target).instructionID.setTextColor(a == 0 ? Color.RED : Color.BLACK);

                adapter.notifyItemMoved(a, b);

                return true;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (!(run || debug)) {

                    BigInteger removeIndex = BigInteger.valueOf(position);
                    BigInteger helper = removeIndex;
                    instructionHashMap.remove(removeIndex);

                    BigInteger size = BigInteger.valueOf(instructionHashMap.size());
                    for (; removeIndex.compareTo(size) == -1; removeIndex = helper) {
                        helper = helper.add(BigInteger.ONE);
                        instructionHashMap.put(removeIndex, instructionHashMap.get(helper));
                    }
                    instructionHashMap.remove(helper);
                }

                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_clearmemory:
                gClearMemory();
                return (true);
            case R.id.menuitem_memory:
                gMemory();
                return (true);
            case R.id.menuitem_run:
                gRun();
                return (true);
            case R.id.menuitem_step:
                gDebug();
                return (true);
            case R.id.menuitem_stop:
                gReset();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    private void gClearMemory(){
        if (debug | run) {
            snackMessage("Running! Reset first!\nMemory is not cleared.");
            return;
        }
        snackMessage("Clean!");
        memoryHashMap.clear();
    }

    private void gMemory() {
        if (debug | run) {
            snackMessage("Running! Reset first!\nMemory is not cleared.");
            return;
        }
        Intent intent = new Intent(this, GMemoryActivity.class);
        startActivity(intent);
    }

    private void gRun() {
        if (debug) {
            snackMessage("Debug Running! Reset first!");
            return;
        }
        gReset();
        run = true;
        snackMessage("Run!");
        updateEnabledParam();
        getFragmentManager().beginTransaction().add(taskFragment, TAG_TASK_FRAGMENT).commit();
    }

    private void gReset() {
        getFragmentManager().executePendingTransactions();
        if (taskFragment != null && taskFragment.isAdded()) {
            getFragmentManager().beginTransaction().remove(taskFragment).commit();
        }
        outputText.setText("");
        highlightedPos = 0;
        debug = false;
        run = false;
        updateEnabledParam();
        adapter.notifyDataSetChanged();
        inputArray.clear();

        for (String s : inputText.getText().toString().trim().split(",")) {
            inputArray.add(s.trim());
        }
    }

    private void gDebug() {
        if (run) {
            snackMessage("Machine Running! Reset first!");
            return;
        }
        if (!debug) {
            try {
                gReset();
                gMachine.init();
                debug = true;
                snackMessage("Debug initialized!");
                updateEnabledParam();
                return;
            } catch (GExecutionException e) {
                createErrorDialog("Machine Initialization Failed! Resetting!", e);
                gReset();
                return;
            }
        }

        snackMessage("Step");
        try {
            BigInteger integer = gMachine.getInstructionPointer();
            debug = !gMachine.process();
            int i;
            if (integer.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) != -1 && integer.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) != 1)
                i = Integer.valueOf(integer.toString());
            else
                throw new GExecutionException("Instruction Pointer Value: " + integer.toString());
            highlightedPos = i;
            if (!gMachine.getLastOutput().equals(""))
                appendToOutput(gMachine.getLastOutput());
            adapter.notifyDataSetChanged();
        } catch (GExecutionException e) {
            createErrorDialog("Step failed!", e);
        }

        if (!debug) {
            snackMessage("Debug End!");
            updateEnabledParam();
            highlightedPos = 0;
            adapter.notifyDataSetChanged();
        }
    }

    public void appendToOutput(String text) {
        if (outputText.getText().toString().length() > 0)
            text = ", " + text;
        outputText.append(text);
    }

    public void notifyDataSet() {
        adapter.notifyDataSetChanged();
    }

    public void gRunPostExecution(GExecutionException exception) {
        run = false;
        if (exception == null) {
            snackMessage("Run Completed!");
        } else {
            createErrorDialog("Run Failed", exception);
        }
        updateEnabledParam();
        highlightedPos = 0;
        adapter.notifyDataSetChanged();
    }

    public void createErrorDialog(final String title, final Exception ex) {
        Snackbar.make(coordinatorLayout, title, Snackbar.LENGTH_SHORT).setAction("Show", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GMachineActivity.this);
                builder.setMessage(ex.getMessage()).setTitle(title);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }).show();
        ex.printStackTrace();
    }

    private void snackMessage(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).setAction("OK", null).show();
    }

    private void updateEnabledParam() {
        boolean enabled = !(run || debug);
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            GAdapter.ViewHolder holder = (GAdapter.ViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            holder.selectedInstruction.setEnabled(enabled);
            holder.args.setEnabled(enabled);
            holder.label.setEnabled(enabled);
            holder.comment.setEnabled(enabled);
        }
        if (enabled)
            fab.show();
        else
            fab.hide();
        inputText.setEnabled(enabled);
    }

    public static boolean getEnabledParam() {
        return !(run || debug);
    }

}
