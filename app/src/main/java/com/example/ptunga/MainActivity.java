package com.example.ptunga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button buy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buy=findViewById(R.id.buy);

        RecyclerView.LayoutManager layoutManager;



        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(MainActivity.this,RecyclerActivity.class);
                startActivity(mIntent);
            }
        });

    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addE:
                callDialog();
                break;
            case R.id.disE:
                startActivity(new Intent(this, RecyclerActivity.class));
                break;
            case R.id.login:
                startActivity(new Intent(this, Login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void callDialog() {
        MaterialAlertDialogBuilder thedialog = new MaterialAlertDialogBuilder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_body, null);
        TextInputEditText dialogName = view.findViewById(R.id.dialogName);
        TextInputEditText dialogDepartment = view.findViewById(R.id.dialogDepartment);
        TextInputEditText dialogGrade = view.findViewById(R.id.dialogGrade);
        TextInputEditText dialogAge = view.findViewById(R.id.dialogAge);
        TextInputEditText dialogLevel = view.findViewById(R.id.dialogLevel);
        thedialog.setView(view);
        thedialog.setTitle("Input Item Information Below");
        thedialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, ""+dialogName.getText().toString()+"\n"+dialogAge.getText().toString()+"\n"+dialogDepartment.getText().toString()+"\n"+dialogGrade.getText().toString()+"\n"+dialogLevel.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
        thedialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }

        );

        thedialog.setCancelable(false);
        thedialog.show();

    }

}