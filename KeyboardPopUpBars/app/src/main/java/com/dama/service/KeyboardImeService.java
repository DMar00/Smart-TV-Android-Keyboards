package com.dama.service;

import static java.lang.Character.isLetter;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;

import com.dama.controllers.Controller;
import com.dama.controllers.ControllerOneBar;
import com.dama.controllers.ControllerTwoBars;
import com.dama.customkeyboardbarsovertv.R;
import com.dama.log.TemaImeLogger;
import com.dama.utils.Cell;
import com.dama.utils.Key;

public class KeyboardImeService extends InputMethodService {
    private Controller controller;
    private boolean keyboardShown;
    private InputConnection ic;
    private FrameLayout rootView;
    private TemaImeLogger temaImeLogger;

    @Override
    public View onCreateInputView() {
        rootView = (FrameLayout) this.getLayoutInflater().inflate(R.layout.keyboard_layout, null);
        controller = new ControllerTwoBars(getApplicationContext(), rootView);
        //controller = new ControllerOneBar(getApplicationContext(), rootView);

        controller.drawKeyboard();

        temaImeLogger = new TemaImeLogger(getApplicationContext());

        return rootView;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        keyboardShown = true;
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        keyboardShown = false;
        controller.hideBars();
        controller.getTextController().reset();
        controller.resetFocus();
    }

    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyboardShown){
            Log.d("KeyboardImeService", "onKeyDown code: "+ keyCode);
            ic = getCurrentInputConnection();
            switch (keyCode){
                case KeyEvent.KEYCODE_1:
                    Log.d("MOD 1 BAR","selected");
                    controller.hideBars();
                    controller = new ControllerOneBar(getApplicationContext(), rootView);
                    controller.moveFocusOnKeyboard(controller.getFocusController().getCurrentFocus());
                    break;
                case KeyEvent.KEYCODE_2:
                    Log.d("MOD 2 BARS","selected");
                    controller.hideBars();
                    controller = new ControllerTwoBars(getApplicationContext(), rootView);
                    controller.moveFocusOnKeyboard(controller.getFocusController().getCurrentFocus());
                    break;
                case KeyEvent.KEYCODE_3:
                    Log.d("UPDATE BARS","selected");
                    controller.setModeUpdateSuggestions(true);
                    break;
                case KeyEvent.KEYCODE_4:
                    Log.d("NO UPDATE BAR","selected");
                    controller.setModeUpdateSuggestions(false);
                    break;
                case KeyEvent.KEYCODE_5:
                    Log.d("MOVE BAR","selected");
                    controller.setMoveBar(true);
                    break;
                case KeyEvent.KEYCODE_6:
                    Log.d("NO MOVE BAR","selected");
                    controller.setMoveBar(false);
                    break;
                case KeyEvent.KEYCODE_BACK:
                    hideKeyboard();
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    temaImeLogger.writeToLog("LEFT",false);
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    temaImeLogger.writeToLog("RIGHT",false);
                case KeyEvent.KEYCODE_DPAD_UP:
                    temaImeLogger.writeToLog("UP",false);
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                        temaImeLogger.writeToLog("DOWN",false);
                    }

                    Cell newCell = controller.findNewFocus(keyCode);
                    if (controller.isNextFocusable(newCell)){
                        //update focus
                        controller.getFocusController().setCurrentFocus(newCell);
                        controller.doBehaviour(newCell);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_0:    //todo remove - just for emulator test OK
                    Cell focus = controller.getFocusController().getCurrentFocus();
                    Key key = controller.getKeysController().getKeyAtPosition(focus);


                    int code = key.getCode();
                    if(code!=Controller.FAKE_KEY){
                        handleText(code, ic);
                        char character = (char) code;
                        if(code!=Controller.ENTER_KEY && code!=Controller.DEL_KEY){
                            controller.getTextController().addCharacterWritten(character);
                            //TODO character==' '
                            if(isLetter(character) || character==' '){
                                if(!key.isSuggestion() /*&& character!=' '*/){  //todo 0307
                                    controller.showBars();
                                }else {
                                    if(controller.isModeUpdateSuggestions() && character!=' ')
                                        controller.updateBars();

                                    if(controller.isMoveBar()){
                                        controller.setModeUpdateSuggestions(false);
                                        controller.hideBars();
                                        Cell letterCell = controller.getKeysController().getCharPosition(key.getLabel().charAt(0));
                                        controller.getFocusController().setCurrentFocus(letterCell);
                                        //controller.getFocusController().setPreviousFocus(letterCell);
                                        controller.moveFocusOnKeyboard(letterCell);

                                        controller.showBars();
                                    }


                                }
                            }
                        }
                    }


                    break;
            }
            return true;
        }
        return false;
    }

    private void handleText(int code, InputConnection ic){
        switch (code){
            case Controller.DEL_KEY:
                ic.deleteSurroundingText(1, 0);
                CharSequence beforeCursor = ic.getTextBeforeCursor(20, 0); //Get text 20 chars before cursor
                if(beforeCursor!=null){
                    controller.getTextController().deleteCharacterWritten(beforeCursor.toString());
                }
                break;
            case Controller.ENTER_KEY:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                break;
            default: //user press letter, number, symbol
                char c = (char) code;
                ic.commitText(String.valueOf(c),1);
        }
    }

    private void hideKeyboard(){
        requestHideSelf(0);
        //calls onFinishInputView
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        controller.getTextController().reset();
    }
}
