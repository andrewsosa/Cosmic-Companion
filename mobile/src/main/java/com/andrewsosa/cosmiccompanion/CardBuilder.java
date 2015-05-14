package com.andrewsosa.cosmiccompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class CardBuilder extends Activity {
    
    // Buttons on the builder layout
    ArrayList<Button> btnNumList = new ArrayList<>();
    Button btnDel;
    Button btnInverse;

    Button btnDone;
    Button btnCancel;


    // Texts
    StringBuilder currentValue = new StringBuilder();
    String currentType;

    // Widgets
    //Toolbar toolbar;

    TextView value;
    TextView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cardbuilder);

        value = (TextView) findViewById(R.id.valueView);
        type = (TextView) findViewById(R.id.typeView);


        Intent i = getIntent();
        currentType = i.getStringExtra("type");
        type.setText(currentType);

        // Set up Listeners
        try {
            findViewById(R.id.btnCancel).setOnClickListener(new FinalButtonListener());
            findViewById(R.id.btnDone).setOnClickListener(new FinalButtonListener());
        } catch(NullPointerException e) {
            Log.e("Cosmic Companion", e.getMessage());
        }

        try {
            findViewById(R.id.btn0).setOnClickListener(new NumPadButtonListener());
            findViewById(R.id.btn1).setOnClickListener(new NumPadButtonListener());
            findViewById(R.id.btn2).setOnClickListener(new NumPadButtonListener());
            findViewById(R.id.btn3).setOnClickListener(new NumPadButtonListener());
            findViewById(R.id.btn4).setOnClickListener(new NumPadButtonListener());
            findViewById(R.id.btn5).setOnClickListener(new NumPadButtonListener());
            findViewById(R.id.btn6).setOnClickListener(new NumPadButtonListener());
            findViewById(R.id.btn7).setOnClickListener(new NumPadButtonListener());
            findViewById(R.id.btn8).setOnClickListener(new NumPadButtonListener());
            findViewById(R.id.btn9).setOnClickListener(new NumPadButtonListener());

            findViewById(R.id.btnDelete).setOnClickListener(new NumPadButtonListener());
            findViewById(R.id.btnMinus).setOnClickListener(new NumPadButtonListener());


        } catch (Exception e) {
            Log.e("Cosmic Companion", e.getMessage());
        }
        
    }

    private class NumPadButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn0: numberHandler(0); break;
                case R.id.btn1: numberHandler(1); break;
                case R.id.btn2: numberHandler(2); break;
                case R.id.btn3: numberHandler(3); break;
                case R.id.btn4: numberHandler(4); break;
                case R.id.btn5: numberHandler(5); break;
                case R.id.btn6: numberHandler(6); break;
                case R.id.btn7: numberHandler(7); break;
                case R.id.btn8: numberHandler(8); break;
                case R.id.btn9: numberHandler(9); break;

                case R.id.btnDelete: deleteHandler(); break;
                case R.id.btnMinus: inverseHandler(); break;

                default: break;

            }
        }

        public void numberHandler(int i) {

            if(currentValue.length() < 2) {
                currentValue.append(i);
                updateDisplay();
            }
            else if((currentValue.length() == 2) && (currentValue.charAt(0) == '-')) {
                currentValue.append(i);
                updateDisplay();
            }

        }

        public void deleteHandler() {

            // Regular delete last element
            if(currentValue.length() > 0) currentValue.deleteCharAt(currentValue.length() - 1);

            // Remove negative if only negative left
            else if((currentValue.length() == 1) && (currentValue.charAt(0) == '-'))
                currentValue.deleteCharAt(0);

            updateDisplay();
        }

        public void inverseHandler() {

            if((currentValue.length() > 0) && (currentValue.charAt(0) == '-')) {
                currentValue.deleteCharAt(0);
            } else {
                currentValue.insert(0, '-');
            }

            updateDisplay();

        }

        public void updateDisplay() {
            value.setText(currentValue.toString());
        }
    }

    private class FinalButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCancel: cancelHandler(); break;
                case R.id.btnDone: doneHandler(); break;
                default: break;
            }
        }

        public void doneHandler() {

            Card card;

            try {
                card = Card.newInstance(currentType, Integer.parseInt(currentValue.toString()));
            } catch (Exception e) {
                card = Card.newInstance(currentType, 0);
            }

            //CardList.addCard(card);
            // TODO HANDLE ADDING CARD

            Intent i = new Intent();
            i.putExtra("type", currentType);
            i.putExtra("value", Integer.parseInt(currentValue.toString()));
            setResult(Dashboard.SUCCESS, i);
            
            cancelHandler();
        }

        public void cancelHandler() {

            // Clear texts
            //DashboardActivity.updateCurrentType("");
            //DashboardActivity.updateCurrentValue("");

            // Clear Values
            currentValue = new StringBuilder();

            // Change views back
            //pager.setCurrentItem(0, true);
            
            
            finish();

        }

    }
}
