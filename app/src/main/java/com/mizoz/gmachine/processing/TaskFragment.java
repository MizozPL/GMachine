package com.mizoz.gmachine.processing;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mizoz.gmachine.GMachineActivity;

import java.math.BigInteger;

public class TaskFragment extends Fragment {
    private GMachineActivity gMachineActivity;
    private Task task;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        gMachineActivity = (GMachineActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        task = new Task();
        task.execute();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        gMachineActivity = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
    }

    private class Task extends AsyncTask<Void, String, Void> {

        private GExecutionException exception = null;

        @Override
        protected void onPreExecute() {
            //handled in activity
        }

        @Override
        protected Void doInBackground(Void... ignore) {
            GMachine gMachine = new GMachine(GMachineActivity.inputArray, GMachineActivity.memoryHashMap, GMachineActivity.instructionHashMap);
            try {
                gMachine.init();
            } catch (GExecutionException e) {
                exception = e;
                return null;
            }

            boolean completed = false;
            while(!completed && !isCancelled()) {
                BigInteger integer = gMachine.getInstructionPointer();

                try {
                    completed = gMachine.process();
                } catch (GExecutionException e) {
                    exception = e;
                    return null;
                }


                if(integer.compareTo(BigInteger.valueOf(Integer.MIN_VALUE))!=-1 && integer.compareTo(BigInteger.valueOf(Integer.MAX_VALUE))!=1) {
                    int i = Integer.valueOf(integer.toString());
                    GMachineActivity.highlightedPos = i;
                }
                else {
                    exception = new GExecutionException("Instruction Pointer Value: " + integer.toString());
                    return null;
                }

                publishProgress(gMachine.getLastOutput());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //Do nothing just exit
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... s) {
            if(gMachineActivity == null)
                return;
            if(!s[0].equals(""))
                gMachineActivity.appendToOutput(s[0]);

            gMachineActivity.notifyDataSet();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            gMachineActivity.gRunPostExecution(exception);
        }
    }


}