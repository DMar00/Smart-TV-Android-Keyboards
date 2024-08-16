package com.dama.controllers;

import android.widget.FrameLayout;

import androidx.leanback.widget.Util;

import com.dama.customkeyboardbarsovertv.R;
import com.dama.utils.Cell;
import com.dama.utils.Key;
import com.dama.utils.Utils;
import com.dama.views.ContainerView;
import com.dama.views.KeyView;
import com.dama.views.KeyboardView;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewsController {
    private ContainerView containerView;
    private KeyboardView keyboardView;

    public ViewsController(FrameLayout rootView) {
        containerView = rootView.findViewById(R.id.container_view);
        keyboardView = containerView.findViewById(R.id.keyboard_view);
    }

    public void drawKeyboard(HashMap<Integer, ArrayList<Key>> keys, Cell focus){
        keyboardView.initKeyboardView(keys);

        KeyView keyView = keyboardView.getKeyViewAtCell(focus);
        keyView.post(()->{
            containerView.initContainerView(keyView);
        });
    }

    /*************************CURSOR******************************/

    public void moveCursorPosition(Cell focus){
        KeyView keyView ;
        if(!Utils.isIntPresent(Controller.barsIndexes,focus.getRow())){
            keyView = keyboardView.getKeyViewAtCell(focus);
        }else{
            keyView= containerView.getBar(focus.getRow()).getKeys().get(focus.getCol());
        }
        containerView.moveCursor(keyView);
    }

    public void increaseZindex(){
        containerView.increaseZindex();
    }

    public void decreaseZindex(){
        containerView.decreaseZindex();
    }

    /*************************HIGHLIGHT******************************/
    public void showHighlight(Cell clicked){
        KeyView keyView = keyboardView.getKeyViewAtCell(clicked);
        containerView.addHighLight(keyView);
    }

    public void hideHighlight(){
        containerView.removeHighlight();
    }

    /*************************ADD SUGGESTIONS******************************/
    public void removePrevBars(){
        for( int i : Controller.barsIndexes){
            int index = containerView.indexOfChild(containerView.getBar(i));
            boolean isChild = (index != -1);
            if(isChild)
                containerView.removeBar(i);
        }
    }

    public void fillKeyboardViewBar(int indexRow, ArrayList<Key> keys, int bar, Cell clicked){
        //empty keyView in bar
        containerView.getBar(indexRow).clearKeys();

        //add new keyView in bar
        containerView.getBar(indexRow).addKeys(keys);

        //set position
        KeyView  keyView = keyboardView.getKeyViewAtCell(clicked);
        if(bar==1){
            containerView.setPosBarUP(keyView, indexRow);
        } else if (bar==2) {
            containerView.setPosBarDOWN(keyView, indexRow);
        }

        //add bar in layout
        containerView.addBar(indexRow);
    }

    /*************************UPDATE SUGGESTIONS******************************/
    public void modifyKeyLabel(Cell position, String label){
        String finaLabel;
        if(label.equals(" "))
            finaLabel = "␣";
        else
            finaLabel = label;

        containerView.getBar(position.getRow()).getKeys().get(position.getCol()).changeLabel(finaLabel, "#FBFBFB");
    }
}
