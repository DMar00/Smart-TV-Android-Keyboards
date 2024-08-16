package com.dama.controllers;

import android.content.Context;

import com.dama.utils.Cell;

public class SuggestionsControllerTwoBars extends SuggestionsController{
    public SuggestionsControllerTwoBars(Context context) {
        super(context);
    }

    @Override
    public void setBarsRowPosition(Cell clicked) {
        int rowIndexBar1 = clicked.getRow()-1;
        int rowIndexBar2 = clicked.getRow()+1;
        getBar1().setRowIndex(rowIndexBar1);
        getBar2().setRowIndex(rowIndexBar2);
    }
}
