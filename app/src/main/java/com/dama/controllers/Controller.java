package com.dama.controllers;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.dama.customkeyboardbarsovertv.R;
import com.dama.utils.Cell;
import com.dama.utils.Key;
import com.dama.utils.Utils;

import java.util.ArrayList;

public abstract class Controller {
    public static final int COLS = 10 + 2;
    public static final int ROWS = 5 + 4;
    public static final int INVALID_KEY = -1;
    public static final int HIDDEN_KEY = -3;
    public static final int SPACE_KEY = 32;
    public static final int FAKE_KEY = -66;
    public static final int ENTER_KEY = 66;
    public static final int DEL_KEY = 24;
    public static final int[] barsIndexes = {1,3,5,7};
    private KeysController keysController;
    private ViewsController viewsController;
    private FocusController focusController;
    private TextController textController;
    private SuggestionsController suggestionsController;
    private boolean modeUpdateSuggestions;
    private boolean moveBar;

    public Controller(Context context, FrameLayout rootView) {
        keysController = new KeysController(new Keyboard(context, R.xml.querty));
        viewsController = new ViewsController(rootView);
        focusController = new FocusController();
        focusController.setCurrentFocus(new Cell(2,1));
        textController = new TextController();

        modeUpdateSuggestions = true;
        moveBar = false;
    }

    public void drawKeyboard(){
        Cell focus = focusController.getCurrentFocus();
        viewsController.drawKeyboard(keysController.getAllKeys(), focus);
    }

    /*************************FOCUS******************************/
    public boolean isNextFocusable(Cell newFocus){
        if(focusController.isFocusInRange(newFocus)
                && !(keysController.isInvalidKey(newFocus))
                && !(keysController.isHiddenKey(newFocus))){
            return true;
        }
        return false;
    }

    public Cell findNewFocus(int code){
        Cell curFocus = focusController.getCurrentFocus();
        Cell newFocus = focusController.calculateNewFocus(code);

        switch (code){
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(suggestionsController.isShown() && Utils.isIntPresent(barsIndexes, curFocus.getRow())){
                    int[] colPos = suggestionsController.getSuggestionsColPositions();
                    if(curFocus.getCol() == colPos[2]){
                        int col = focusController.getPreviousFocus().getCol()+1;
                        if(col>COLS-2)
                            col = 1;
                        newFocus.setRow(focusController.getPreviousFocus().getRow());
                        newFocus.setCol(col);
                    }
                }
            break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(suggestionsController.isShown() && Utils.isIntPresent(barsIndexes, curFocus.getRow())){
                    int[] colPos = suggestionsController.getSuggestionsColPositions();
                    if(curFocus.getCol() == colPos[0]){
                        int col = focusController.getPreviousFocus().getCol()-1;
                        if(col<1)
                            col = COLS-2;
                        newFocus.setRow(focusController.getPreviousFocus().getRow());
                        newFocus.setCol(col);
                    }
                }
            break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if(keysController.getKeysAtRow(newFocus.getRow())!= null && keysController.getKeysAtRow(newFocus.getRow()).isEmpty()){
                    newFocus.setRow(newFocus.getRow()-1);
                }

                //last row behaviour focus
                if((curFocus.getRow() == ROWS-1)){
                    switch (curFocus.getCol()){
                        case 5:
                            newFocus.setCol(8);
                            break;
                        case 6:
                            newFocus.setCol(9);
                            break;
                        case 7:
                            newFocus.setCol(10);
                            break;
                    }
                }

                //from bar 2 to prev focus
                if(suggestionsController.isShown() && Utils.isIntPresent(barsIndexes, curFocus.getRow())){
                    if(curFocus.getRow() == suggestionsController.getBar2().getRowIndex())
                        newFocus = focusController.getPreviousFocus();
                }

                //from bar 1 to same cell prev
                if(suggestionsController.isShown() && Utils.isIntPresent(barsIndexes, curFocus.getRow())){
                    if(curFocus.getRow() == suggestionsController.getBar1().getRowIndex())
                        newFocus.setCol(focusController.getPreviousFocus().getCol());
                }
            break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if(keysController.getKeysAtRow(newFocus.getRow())!= null && keysController.getKeysAtRow(newFocus.getRow()).isEmpty()){
                    newFocus.setRow(newFocus.getRow()+1);
                }

                //last row behaviour focus
                if(  (curFocus.getRow() == ROWS-2) || ((curFocus.getRow() == ROWS-3) && !suggestionsController.isShown())
                || ((curFocus.getRow() == ROWS-3) && suggestionsController.isShown() && suggestionsController.getBar2().getRowIndex()==-1) ){
                    switch (curFocus.getCol()){
                        case 5:
                        case 6:
                        case 7:
                            newFocus.setCol(4);
                            break;
                        case 8:
                            newFocus.setCol(5);
                            break;
                        case 9:
                            newFocus.setCol(6);
                            break;
                        case 10:
                            newFocus.setCol(7);
                            break;
                    }
                }

                //from bar 1 to prev focus
                if(suggestionsController.isShown() && Utils.isIntPresent(barsIndexes, curFocus.getRow())){
                    if(curFocus.getRow() == suggestionsController.getBar1().getRowIndex())
                        newFocus = focusController.getPreviousFocus();
                }
                //from bar 2 to same cell prev
                if(suggestionsController.isShown() && Utils.isIntPresent(barsIndexes, curFocus.getRow())){
                    if(curFocus.getRow() == suggestionsController.getBar2().getRowIndex()){
                        if(curFocus.getRow()!= ROWS-2){
                            newFocus.setCol(focusController.getPreviousFocus().getCol());
                        }else{
                            switch (focusController.getPreviousFocus().getCol()){
                                case 5:
                                case 6:
                                case 7:
                                    newFocus.setCol(4);
                                    break;
                                case 8:
                                    newFocus.setCol(5);
                                    break;
                                case 9:
                                    newFocus.setCol(6);
                                    break;
                                case 10:
                                    newFocus.setCol(7);
                                    break;
                            }
                        }

                    }
                }
                break;
        }
        return newFocus;
    }

    public void moveFocusOnKeyboard(Cell position){
        viewsController.moveCursorPosition(position);
        if(Utils.isIntPresent(barsIndexes, position.getRow())){
            viewsController.increaseZindex();
        }else{
            viewsController.decreaseZindex();
        }
    }

    public void doBehaviour(Cell newFocus){
        if(!suggestionsController.isShown()){
            moveFocusOnKeyboard(newFocus);
        }else{
            Cell prevFocus = focusController.getPreviousFocus();
            boolean isBar = Utils.isIntPresent(barsIndexes, newFocus.getRow());
            if(!isBar){
                if(newFocus.equals(prevFocus)){
                    moveFocusOnKeyboard(newFocus);
                }else {
                    hideBars();
                    moveFocusOnKeyboard(newFocus);
                }
            }else {
                moveFocusOnKeyboard(newFocus);
            }
        }
    }

    public void resetFocus(){
        Cell r = new Cell(2,1);
        focusController.setCurrentFocus(r);
        moveFocusOnKeyboard(r);
    }

    /*************************KEYS******************************/

    protected void modifyKeyContent(Cell position, int code, String label){
        keysController.modifyKeyAtPosition(position, code, label);
        viewsController.modifyKeyLabel(position, label);
    }

    /*******************GETTERS/SETTERS*********************/
    public FocusController getFocusController() {
        return focusController;
    }

    public KeysController getKeysController() {
        return keysController;
    }

    public TextController getTextController() {
        return textController;
    }

    public ViewsController getViewsController() {
        return viewsController;
    }

    public SuggestionsController getSuggestionsController() {
        return suggestionsController;
    }

    public void setSuggestionsController(SuggestionsController suggestionsController) {
        this.suggestionsController = suggestionsController;
    }

    public boolean isModeUpdateSuggestions() {
        return modeUpdateSuggestions;
    }

    public void setModeUpdateSuggestions(boolean modeUpdateSuggestions) {
        this.modeUpdateSuggestions = modeUpdateSuggestions;
    }

    public boolean isMoveBar() {
        return moveBar;
    }

    public void setMoveBar(boolean moveBar) {
        this.moveBar = moveBar;
    }

    /*****************SUGGESTIONS********************/
    public void showBars(){
        Cell curFocus = focusController.getCurrentFocus();

        //add highlight
        focusController.setPreviousFocus(curFocus);
        viewsController.showHighlight(focusController.getPreviousFocus());

        //set row index bars && col
        suggestionsController.setBarsRowPosition(focusController.getPreviousFocus());
        suggestionsController.setSuggestionsColPositions(focusController.getPreviousFocus());

        //generate suggestions
        String sequence = textController.getFourCharacters();
        char[] allSuggestions = suggestionsController.getSuggestionsChars(sequence);
        char [] checkedSuggestions = getCheckedSuggestions(allSuggestions);

        //add suggestions
        addSuggestionToBars(checkedSuggestions);

        //
        suggestionsController.setShown(true);
    }

    public void hideBars(){
        //remove highlight
        viewsController.hideHighlight();

        //clear bars
        clearAllBars();

        //
        suggestionsController.setShown(false);
    }

    public void updateBars(){
        //generate suggestions
        String sequence = textController.getFourCharacters();
        char[] allSuggestions = suggestionsController.getSuggestionsChars(sequence);
        char [] checkedSuggestions = getCheckedSuggestions(allSuggestions);
        //
        for(char c: checkedSuggestions)
            Log.d("ERRORE",c+"");
        updateSuggestionsBars(checkedSuggestions);
    }

    protected void clearAllBars(){
        for(int i : Controller.barsIndexes){
            keysController.getKeysAtRow(i).clear();
        }

        viewsController.removePrevBars();
    }

    protected void fillBar(int index, char[] suggestions, int bar){
        ArrayList<Key> keys = new ArrayList<>();
        for(char s : suggestions){
            //Log.d("ERROR","s: "+s);
            Key k = keysController.getCharToKey(s).clone();
            //Log.d("ERROR","k: "+k.getLabel());
            k.setSuggestion(true);
            keys.add(k);
        }

        fillKeyboardBar(index, keys);
        viewsController.fillKeyboardViewBar(index, keysController.getKeysAtRow(index), bar, focusController.getPreviousFocus());
    }

    protected void fillKeyboardBar(int indexRow, ArrayList<Key> keys){
        //clear bar
        keysController.getKeysAtRow(indexRow).clear();

        //get col indexes suggestions
        int[] colIndexes = suggestionsController.getSuggestionsColPositions();

        //add to keysController
        for(int i=0, j=0; i<COLS; i++){
            if(Utils.isIntPresent(colIndexes,i)){
                keysController.getKeysAtRow(indexRow).add(keys.get(j));
                j++;
            }else {
                keysController.getKeysAtRow(indexRow).add(new Key(HIDDEN_KEY,"", null));
            }
        }
    }

    private char[] getCheckedSuggestions(char[] allSuggestions){ //allSuggestions has size = 12
        int z = 0;
        for(char v : allSuggestions){
            Log.d("ALL SUGG","allsugg["+z+"]: "+v);
            z++;
        }


        ArrayList<Character> charsToDelete = new ArrayList<>();

        //delete from suggestions clicked highlight letter
        Cell prevFocus = focusController.getPreviousFocus();
        char d1 = keysController.getKeyAtPosition(prevFocus).getLabel().charAt(0);
        charsToDelete.add(d1);

        //delete from suggestions focused letter [in bar for example]
        Cell curFocus = focusController.getCurrentFocus();
        char d2 = keysController.getKeyAtPosition(curFocus).getLabel().charAt(0);
        charsToDelete.add(d2);

        //
        int dim = allSuggestions.length-charsToDelete.size();
        char[] checkedSuggestions = new char[dim];
        int i = 0;
        for(char c: allSuggestions){
            Cell sugCell = keysController.getCharPosition(c);
            int clicks = getClicksNumber(curFocus, sugCell);
            //Log.d("distance","curFocus: "+keysController.getKeyAtPosition(curFocus).getLabel()+" - letter: "+c+" - ["+clicks+"]");
            if(!charsToDelete.contains(c) && i<dim && clicks>2){
                checkedSuggestions[i] = c;
                i++;
            }
        }

        /*for(char v : checkedSuggestions)
            Log.d("CHECKED","char: "+v);*/
        return checkedSuggestions;
    }

    private int getClicksNumber(Cell selected, Cell other){
        Cell selectedCell = new Cell(getNewRow(selected.getRow()), selected.getCol());
        Cell otherCell = new Cell(getNewRow(other.getRow()), other.getCol());

        int difRow = Math.abs(selectedCell.getRow()-otherCell.getRow());
        int difCol = Math.abs(selectedCell.getCol()-otherCell.getCol());
        int difCol2 = (Controller.COLS) - difCol;
        int clicks =  difRow + Math.min(difCol, difCol2);

        return clicks;
    }

    protected abstract int getNewRow(int oldRow);

    protected abstract void addSuggestionToBars(char[] checkedSuggestions);
    protected  abstract void updateSuggestionsBars(char[] checkedSuggestions);

    /**********************TEMA IME************************/
    public int whichBar(){
        int row1 = suggestionsController.getBar1().getRowIndex();
        int row2 = suggestionsController.getBar2().getRowIndex();
        if(focusController.getCurrentFocus().getRow() == row1)
            return 1;
        else if(focusController.getCurrentFocus().getRow() == row2)
            return 2;
        else
            return -1;
    }

    public int getPosInBar(){
        int pos[] = suggestionsController.getSuggestionsColPositions();
        if(pos!=null){
            if(focusController.getCurrentFocus().getCol() == pos[0])
                return 1;
            else if(focusController.getCurrentFocus().getCol() == pos[1]){
                return 2;
            }else if(focusController.getCurrentFocus().getCol() == pos[2]){
                return 3;
            }else return -1;
        }else return -1;
    }
}
