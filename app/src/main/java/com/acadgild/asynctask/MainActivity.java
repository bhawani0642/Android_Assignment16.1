package com.acadgild.asynctask;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private static final int WRITE_REQUEST_CODE = 50;
    EditText text;
    TextView content;
    Button add, delete;
    static String FILENAME = "test.txt";
    File file;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //UI components
        text = (EditText) findViewById(R.id.enter_data);
        content = (TextView) findViewById(R.id.show_data);
        add = (Button) findViewById(R.id.btn_add);
        delete = (Button) findViewById(R.id.btn_delete);


        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, WRITE_REQUEST_CODE);

        // File creation
        file = new File(Environment.getExternalStorageDirectory(), FILENAME);
        try {
            if (file.createNewFile()){
                Toast.makeText(getApplicationContext(), "File Created",
                        Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //update data to File
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string=text.getText().toString();
                text.setText("");

                ReadFile readFile = new ReadFile(file);
                readFile.execute(string);
            }
        });
        //deletion of  file
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file.delete();
            }
        });

    }
    private class ReadFile extends AsyncTask<String, Integer, String> {

        File fileRead;

        public ReadFile(File fileRead) {
            super();
            this.fileRead=fileRead;

        }
        //update data to file
        @Override
        protected String doInBackground(String... strings) {
            String enter="\n";
            FileWriter filewriter=null;
            try {
                filewriter=new FileWriter(fileRead,true);
                filewriter.append(strings[0].toString());
                filewriter.append(enter);
                filewriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    filewriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
        //read data from file
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String name = "";
            StringBuilder stringBuilder = new StringBuilder();
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(fileRead);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((name = bufferedReader.readLine()) != null) {
                    stringBuilder.append(name);
                    stringBuilder.append("\n");

                }
                bufferedReader.close();
                fileReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            content.setText(stringBuilder.toString());

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Granted.
                }
                else{
                    //Denied.
                }
                break;
        }
    }

}
