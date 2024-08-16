package com.dama.controllers;

import android.content.Context;
import com.dama.utils.Cell;

public class SuggestionsControllerOneBar extends SuggestionsController{
    public SuggestionsControllerOneBar(Context context) {
        super(context);
    }

    @Override
    public void setBarsRowPosition(Cell clicked) {
        getBar1().setRowIndex(clicked.getRow()-1);
    }
}
