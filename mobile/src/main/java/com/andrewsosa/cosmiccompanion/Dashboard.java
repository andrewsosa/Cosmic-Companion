package com.andrewsosa.cosmiccompanion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;


public class Dashboard extends Activity implements Toolbar.OnMenuItemClickListener{

    // Sum in header bar
    int sumValue = 0;

    private CardListAdapter cardListAdapter;
    private Toolbar t;

    // Request codes
    final static int ADD_CARD = 0;
    final static int SUCCESS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("0 Total");
        t.setTitleTextAppearance(this, R.style.titleText);
        t.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        t.setOnMenuItemClickListener(this);
        t.inflateMenu(R.menu.menu_main);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primaryDark));

        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        ArrayList<Card> cards = new ArrayList<>();
        GridView list = (GridView) findViewById(R.id.cardList);

        cardListAdapter = new CardListAdapter(this, cards);
        list.setAdapter(cardListAdapter);


        initFAB();

        updateSumValue(getCardSum());

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.action_clear:
                cardListAdapter.cards.clear();
                cardListAdapter.notifyDataSetChanged();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void launchCardBuilder(View v) {
        String type = ((FloatingActionButton) v).getTitle();
        Intent i = new Intent(Dashboard.this, CardBuilder.class);
        i.putExtra("type", type);
        startActivityForResult(i, ADD_CARD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_CARD && resultCode == SUCCESS) {
            int value = data.getIntExtra("value", 0);
            String type = data.getStringExtra("type");
            this.addCard(Card.newInstance(type, value));
        }
    }

    private void initFAB(){

        FABListener l = new FABListener();
        findViewById(R.id.ships).setOnClickListener(l);
        findViewById(R.id.attack).setOnClickListener(l);
        findViewById(R.id.kicker).setOnClickListener(l);
        findViewById(R.id.reinforcements).setOnClickListener(l);
        findViewById(R.id.bonus).setOnClickListener(l);

    }

    private class FABListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.button);
            launchCardBuilder(v);
            menu.collapse();

        }
    }

    public int getCardSum() {
        return cardListAdapter.sumElements();
    }

    public void addCard(Card c) {
        cardListAdapter.add(c);
        updateSumValue(getCardSum());
    }


    public void updateSumValue(int newValue) {
        sumValue = newValue;
        //sumText.setText(String.valueOf(sumValue));
        t.setTitle(String.valueOf(sumValue) + " Total");
    }


    
    //
    // Adapter for Main List view
    //
    public class CardListAdapter extends ArrayAdapter<Card> {

        private final Context c;
        public final ArrayList<Card> cards;


        public CardListAdapter(Context c, ArrayList<Card> cards) {
            super(c, R.layout.fragment_card, cards);

            this.c = c;
            this.cards = cards;

        }

        public Integer sumElements() {
            Integer sum = 0;
            Integer encounter = 0;
            Integer kicker = 1;

            for(Card c : cards) {
                if (c != null) {

                    if(c.getCardType().equals("Kicker")) {
                        kicker = c.getCardValue();
                    }
                    else if(c.getCardType().equals("Attack")) {
                        encounter = encounter + c.getCardValue();
                    }
                    else sum = sum + c.getCardValue();
                }

            }

            return sum + (encounter * kicker);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Init stuff
            LayoutInflater inflater = (LayoutInflater) c
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.fragment_card, parent, false);

            // Get view elements
            TextView cardType = (TextView) v.findViewById(R.id.cardType);
            TextView cardValue = (TextView) v.findViewById(R.id.cardValue);
            View cardLayout = v.findViewById(R.id.cardLayout);

            // Assign view elements
            Card card = cards.get(position);

            if (card.getCardType() != null) cardType.setText(card.getCardType());

            // Data manipulation before display
            String tempValue = String.valueOf(card.getCardValue());
            String type = card.getCardType();
            if (type.equals("Kicker")) tempValue = "x" + tempValue;
            if (type.equals("Reinforcements")) tempValue = "+" + tempValue;
            if (card.getCardValue() != null) cardValue.setText(tempValue);

            // Assign color
            int bg = getResources().getColor(R.color.default_card);
            if (type.equals("Ships")) bg = getResources().getColor(R.color.ship_card);
            if (type.equals("Attack")) bg = getResources().getColor(R.color.attack_card);
            if (type.equals("Kicker")) bg = getResources().getColor(R.color.kicker_card);
            if (type.equals("Reinforcements")) bg = getResources().getColor(R.color.reinforcements_card);
            if (type.equals("Bonus")) bg = getResources().getColor(R.color.bonus_card);
            cardLayout.setBackgroundColor(bg);


            ImageButton btnRemove = (ImageButton) v.findViewById(R.id.btnRemove);
            btnRemove.setOnClickListener(new ImageButton.OnClickListener(){
                public void onClick(View v) {

                    cards.remove(position);
                    notifyDataSetChanged();
                }
            });

            return v;

        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            Dashboard.this.updateSumValue(sumElements());
        }
    }

}
