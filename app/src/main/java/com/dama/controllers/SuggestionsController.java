package com.dama.controllers;

import android.content.Context;

import com.dama.generators.SuggestionsGenerator;
import com.dama.utils.BarStatus;
import com.dama.utils.Cell;

public abstract class SuggestionsController {
    public static final int N_SUG_FOR_BAR = 3;
    private SuggestionsGenerator suggestionsGenerator;
    private int[] suggestionsColPositions;
    private boolean shown;
    private BarStatus bar1;
    private BarStatus bar2;


    public SuggestionsController(Context context) {
        suggestionsGenerator = new SuggestionsGenerator(context);
        shown = false;
        bar1 = new BarStatus();
        bar2 = new BarStatus();
    }


    /***********************COLS********************/

    public void setSuggestionsColPositions(Cell clicked){
        suggestionsColPositions = generateSuggestionsColPositions(clicked);
    }

    private int[] generateSuggestionsColPositions(Cell cell){
        int position = cell.getCol();
        int[] indexes = new int[N_SUG_FOR_BAR];
        indexes[0] = position-1;
        indexes[1] = position;
        indexes[2] = position+1;
        return indexes;
    }

    public int[] getSuggestionsColPositions() {
        return suggestionsColPositions;
    }

    /***********************GETTERS********************/

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public BarStatus getBar1() {
        return bar1;
    }

    public BarStatus getBar2() {
        return bar2;
    }

    /***************GENERATE SUGGESTIONS*****************/
    public char[] getSuggestionsChars(String sequence){
        char[] allSuggestions = suggestionsGenerator.getSuggestionArray(sequence);
        return allSuggestions;
    }

    /*********************ABSTRACT********************/

    public abstract void setBarsRowPosition(Cell clicked);
}
