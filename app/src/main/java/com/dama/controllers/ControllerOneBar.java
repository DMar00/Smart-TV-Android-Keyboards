package com.dama.controllers;

import android.content.Context;
import android.widget.FrameLayout;

import com.dama.utils.Cell;
import com.dama.utils.Key;
import com.dama.utils.Utils;

import java.util.Arrays;

public class ControllerOneBar extends Controller{

    public ControllerOneBar(Context context, FrameLayout rootView) {
        super(context, rootView);
        setSuggestionsController(new SuggestionsControllerOneBar(context));
    }

    @Override
    protected int getNewRow(int oldRow) {
        int indexBar1 = getSuggestionsController().getBar1().getRowIndex();
        if(indexBar1==3){
            switch (oldRow) {
                case 2:
                    return 0;
                case 3:
                    return 1;
                case 4:
                    return 2;
                case 6:
                    return 3;
                case 8:
                    return 4;
                default:
                    return -1;
            }
        }else if(indexBar1==5){
            switch (oldRow) {
                case 2:
                    return 0;
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 8:
                    return 4;
                default:
                    return -1;
            }
        }
        return -1;
    }

    @Override
    protected void addSuggestionToBars(char[] checkedSuggestions) {
        //each bar has 3 suggestions
        char[] sug1 = new char[3];

        //put first suggestion up or down clicked letter
        sug1[1] = checkedSuggestions[0];

        //fill others
        sug1[0] = checkedSuggestions[1];    //2
        sug1[2] = checkedSuggestions[2];    //3

        //put them in keysController
        int index1 = getSuggestionsController().getBar1().getRowIndex();
        getViewsController().removePrevBars();
        fillBar(index1, sug1, 1);
    }

    @Override
    protected void updateSuggestionsBars(char[] checkedSuggestions) {
        Cell curFocus = getFocusController().getCurrentFocus();

        int[] colIndexes = getSuggestionsController().getSuggestionsColPositions();
        char[] sug = Arrays.copyOfRange(checkedSuggestions, 0, (SuggestionsController.N_SUG_FOR_BAR)-1); //0 to 2

        //modify key in focused Bar
        int[] cIndex = Utils.removeIntFromArray(colIndexes,curFocus.getCol()); //colIndexes - curFocus.getCol()
        Key k1 = getKeysController().getCharToKey(sug[0]);
        Key k2 = getKeysController().getCharToKey(sug[1]);
        modifyKeyContent(new Cell(curFocus.getRow(),cIndex[0]), k1.getCode(), k1.getLabel());
        modifyKeyContent(new Cell(curFocus.getRow(),cIndex[1]), k2.getCode(), k2.getLabel());
    }
}
