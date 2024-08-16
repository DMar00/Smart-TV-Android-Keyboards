package com.dama.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import com.dama.utils.Cell;
import com.dama.utils.Key;
import com.dama.utils.Utils;

import java.util.Arrays;

public class ControllerTwoBars extends Controller{
    public ControllerTwoBars(Context context, FrameLayout rootView) {
        super(context, rootView);
        setSuggestionsController(new SuggestionsControllerTwoBars(context));
    }

    @Override
    protected int getNewRow(int oldRow) {
        int indexBar1 = getSuggestionsController().getBar1().getRowIndex();
        int indexBar2 = getSuggestionsController().getBar2().getRowIndex();
        if(indexBar1==1 && indexBar2==3){
            switch (oldRow){
                case 1:
                    return 0;
                case 2:
                    return 1;
                case 3:
                    return 2;
                case 4:
                    return 3;
                case 6:
                    return 4;
                default:
                    return -1;
            }
        }else if(indexBar1==3 && indexBar2==5){
            switch (oldRow) {
                case 2:
                    return 0;
                case 3:
                    return 1;
                case 4:
                    return 2;
                case 5:
                    return 3;
                case 6:
                    return 4;
                default:
                    return -1;
            }
        }else if(indexBar1==5 && indexBar2==7){
            switch (oldRow) {
                case 2:
                    return 0;
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
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
        char[] sug2 = new char[3];

        //put first 2 suggestions up and down clicked letter
        sug1[1] = checkedSuggestions[0];
        sug2[1] = checkedSuggestions[1];

        //fill others
        sug1[0] = checkedSuggestions[2];
        sug1[2] = checkedSuggestions[3];
        sug2[0] = checkedSuggestions[4];
        sug2[2] = checkedSuggestions[5];

        //put them in keysController
        int index1 = getSuggestionsController().getBar1().getRowIndex();
        int index2 = getSuggestionsController().getBar2().getRowIndex();
        getViewsController().removePrevBars();

        Log.d("SIZE","size: "+checkedSuggestions.length);
        int i=0;
        for(char c: checkedSuggestions){
            Log.d("CHKSUG","checked["+i+"]: "+c);
            i++;
        }

        i=0;
        for(char c: sug1){
            Log.d("SUG1","sug1["+i+"]: "+c);
            i++;
        }


        fillBar(index1, sug1, 1);

        if(index2<ROWS)
            fillBar(index2, sug2, 2);
    }

    @Override
    protected void updateSuggestionsBars(char[] checkedSuggestions) {
        int indexRow1 = getSuggestionsController().getBar1().getRowIndex();
        int indexRow2 = getSuggestionsController().getBar2().getRowIndex();

        Cell curFocus = getFocusController().getCurrentFocus();

        int focusRowIndex = -1, noFocusRowIndex = -1;
        if(curFocus.getRow()==indexRow1){
            focusRowIndex = indexRow1;
            noFocusRowIndex = indexRow2;
        }else if(curFocus.getRow()==indexRow2){
            focusRowIndex = indexRow2;
            noFocusRowIndex = indexRow1;
        }

        int[] colIndexes = getSuggestionsController().getSuggestionsColPositions();
        char[] sug = Arrays.copyOfRange(checkedSuggestions, 0, (SuggestionsController.N_SUG_FOR_BAR*2)-1); //0 to 5

        //modify key in focused Bar
        int[] cIndex = Utils.removeIntFromArray(colIndexes,curFocus.getCol()); //colIndexes - curFocus.getCol()
        Key k1 = getKeysController().getCharToKey(sug[0]);
        Key k2 = getKeysController().getCharToKey(sug[1]);
        modifyKeyContent(new Cell(focusRowIndex,cIndex[0]), k1.getCode(), k1.getLabel());
        modifyKeyContent(new Cell(focusRowIndex,cIndex[1]), k2.getCode(), k2.getLabel());

        //modify key in noFocused Bar
        Key k3 = getKeysController().getCharToKey(sug[2]);
        Key k4 = getKeysController().getCharToKey(sug[3]);
        Key k5 = getKeysController().getCharToKey(sug[4]);

        //todo 0307 only if not bar
        if(getKeysController().getKeyAtPosition(getFocusController().getPreviousFocus()).getCode()!=SPACE_KEY){
            modifyKeyContent(new Cell(noFocusRowIndex,colIndexes[0]), k3.getCode(), k3.getLabel());
            modifyKeyContent(new Cell(noFocusRowIndex,colIndexes[1]), k4.getCode(), k4.getLabel());
            modifyKeyContent(new Cell(noFocusRowIndex,colIndexes[2]), k5.getCode(), k5.getLabel());
        }
    }
}
