package com.andrewsosa.cosmiccompanion;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class Card extends Fragment {

    // Card data
    String cardType;
    Integer cardValue;

    public static Card newInstance(String type, int value) {

        Card c = new Card();

        c.setCardType(type);
        c.setCardValue(value);

        return c;

    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Integer getCardValue() {
        return cardValue;
    }

    public void setCardValue(Integer cardValue) {
        this.cardValue = cardValue;
    }



}
