package com.mizoz.gmachine;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class GMainActivity extends AppCompatActivity {

    private static final String message = "Uruchamiając tę aplikację bierzesz na siebie pełną odpowiedzialność za jej niestabilne działanie i ponosisz wszelkie konsekwencje związane z jej użytkowaniem:\nSoftware brick,\nHardware brick,\nSpalenie się telefonu,\nKamil może nie być głupi*,\nitd.\n\n*No dobra bez przesady, aż tak niedopracowana nie jest.\n\nMiłego Użytkowania!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmain);
    }

    public void startGMachineActivity(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GMainActivity.this);
        builder.setMessage(message).setTitle("Grenade Machine - Wiadomość do Użytkownika");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(GMainActivity.this, GMachineActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
