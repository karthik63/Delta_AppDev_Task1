package com.example.samharris.task1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private RelativeLayout myLayout;

    private int count = 0;

    private int red;
    private int blue;
    private int green;

    TextView countView;
    Button incrButt;
    Button resetButt;

    private void resetCount(){
        count = 0;
    }
    //function to generate random color and set background to it
    private void colorSwitch() {

        Random rgbGen = new Random();

        red = rgbGen.nextInt(256);
        green = rgbGen.nextInt(256);
        blue = rgbGen.nextInt(256);

        changeFontColor();

        myLayout.setBackgroundColor(Color.rgb(red,green,blue));

    }

    //to ensure font visibility at all colors
    private void changeFontColor()
    {
        if (((red*299)+(green*587)+(blue*114))/1000>128)
            countView.setTextColor(Color.BLACK);
        else
            countView.setTextColor(Color.WHITE);
    }

    //function to increment count and change the TextView
    private void incrementCount() {
        countView = (TextView)findViewById(R.id.countView);
        count+=1;
        countView.setText(String.valueOf(count));
    }

    //function to destroy saved preferences and reset background colour to yellow and set font color to black
    private void destroySaves()
    {
        this.getSharedPreferences("colorCount", Context.MODE_PRIVATE).edit().clear().apply();
        red=255;
        green=247;
        blue=5;
        countView.setTextColor(Color.BLACK);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences shared = getSharedPreferences("colorCount", Context.MODE_PRIVATE);

        //set the colours saved on stop or destroy or set default colours if shared is null
        red = shared.getInt("red", 255);
        green = shared.getInt("green", 247);
        blue = shared.getInt("blue", 5);

        myLayout = (RelativeLayout) findViewById(R.id.startPage);

        myLayout.setBackgroundColor(Color.rgb(red,green,blue));

        //set count to saved count value or set it to 0 if no shared preferences
        count = shared.getInt("count", 0);

        countView = (TextView)findViewById(R.id.countView);

        incrButt = (Button) findViewById(R.id.incrButt);

        resetButt = (Button) findViewById(R.id.resetButt);

        countView.setText(String.valueOf(count));

        //change the font color according to brightness as soon as count is set
        changeFontColor();

        incrButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                colorSwitch();

                incrementCount();
            }
        });


        resetButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                resetCount();

                countView.setText("0");

                destroySaves();

                myLayout.setBackgroundColor(Color.rgb(red,green,blue));

            }
        });

    }

    //To display about page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.aboutMenu) {
            Intent settingsIntent = new Intent(this, AboutPage.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    //Save rgb values and count onStop
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences shared = getSharedPreferences("colorCount", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("count",count);
        editor.putInt("red",red);
        editor.putInt("blue",blue);
        editor.putInt("green",green);

        editor.apply();
    }

    //Save rgb values and count onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences shared = getSharedPreferences("colorCount", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("count",count);
        editor.putInt("red",red);
        editor.putInt("blue",blue);
        editor.putInt("green",green);

        editor.apply();

    }
}
