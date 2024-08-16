package com.dama.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.dama.controllers.Controller;
import com.dama.customkeyboardbarsovertv.R;

import java.util.HashMap;


public class ContainerView extends FrameLayout {
    private KeyView cursor;
    private KeyView highLight;
    private HashMap<Integer, BarView> bars;
    private float currX, currY;

    public ContainerView(@NonNull Context context) {
        super(context);
    }

    public ContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initContainerView(KeyView focusedView){
        //create cursor
        Drawable cs = ContextCompat.getDrawable(getContext(), R.drawable.cursor);
        cursor = new KeyView(getContext(), cs, null, "#FBFBFB");

        //set initial position cursor
        initCursorPosition(focusedView);

        //create highLight
        Drawable hl = ContextCompat.getDrawable(getContext(), R.drawable.highlight);
        highLight = new KeyView(getContext(), hl, null, "#FBFBFB");

        //init bars
        bars = new HashMap<>();
        for(int i : Controller.barsIndexes){
            BarView barView =  new BarView(getContext());
            barView.initBarView();
            bars.put(i,barView);
        }
    }

    public Rect rectCoordinate(KeyView keyView){
        Rect offsetViewBounds = new Rect();
        keyView.getDrawingRect(offsetViewBounds);
        offsetDescendantRectToMyCoords(keyView, offsetViewBounds);
        return offsetViewBounds;
    }

    /*************************CURSOR******************************/

    public void initCursorPosition(KeyView keyView){
        keyView.post(() -> {
            //coordinate keyView
            Rect keyViewCoordinates = rectCoordinate(keyView);
            int x = keyViewCoordinates.left;
            int y = keyViewCoordinates.top;

            //remove cursor
            removeView(cursor);

            //add cursor
            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            );
            layoutParams.leftMargin = x; //set x
            layoutParams.topMargin = y; //set y
            cursor.setLayoutParams(layoutParams);
            cursor.changeDimension(keyView.getKeyHeight(), keyView.getKeyWidth(), 0);

            //cursor.requestLayout();
            addView(cursor);
            //cursor.requestLayout();

            currX = cursor.getX();
            currY = cursor.getY();
        });
    }

    public void moveCursor(KeyView keyView){
        keyView.post(() -> {
            //keyView coordinate
            Rect keyViewCoordinates = rectCoordinate(keyView);
            int x = keyViewCoordinates.left;
            int y = keyViewCoordinates.top;

            //distances
            float newX = x - cursor.getX();
            float newY = y - cursor.getY();


            //create animation
            TranslateAnimation animation = new TranslateAnimation(currX,newX,currY,newY);
            animation.setFillAfter(true);
            animation.setFillBefore(true);
            animation.setDuration(100);
            //animation.setInterpolator(new AccelerateInterpolator());

            //start animation on view
            cursor.startAnimation(animation);
            cursor.changeDimension(keyView.getKeyHeight(), keyView.getKeyWidth(), 0);

            //update
            this.currX = newX;
            this.currY = newY;
        });
    }

    public void increaseZindex(){
        cursor.setElevation(getResources().getDimensionPixelSize(R.dimen.elevationCursorMAX));
    }

    public void decreaseZindex(){
        cursor.setElevation(getResources().getDimensionPixelSize(R.dimen.elevationCursorMIN));
    }

    /*************************HIGHLIGHT******************************/

    public void addHighLight(KeyView clickedView){
        clickedView.post(() -> {
            //coordinate keyView
            Rect keyViewCoordinates = rectCoordinate(clickedView);
            int x = keyViewCoordinates.left;
            int y = keyViewCoordinates.top;

            //remove cursor
            removeView(highLight);

            //add cursor
            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            );
            layoutParams.leftMargin = x; //set x
            layoutParams.topMargin = y; //set y
            highLight.setLayoutParams(layoutParams);
            highLight.changeDimension(clickedView.getKeyHeight(), clickedView.getKeyWidth(), 0);
            addView(highLight);
        });
    }

    public void removeHighlight(){
        removeView(highLight);
    }

    /******************************BARS*********************************/
    public BarView getBar(int indexRow) {
        return bars.get(indexRow);
    }

    public void addBar(int indexRow){
        addView(getBar(indexRow));
    }

    public void removeBar(int indexRow){
        removeView(getBar(indexRow));
    }

    public void setPosBarUP(KeyView focusedKeyView, int indexRow){
        focusedKeyView.post(() -> {
            BarView barView = bars.get(indexRow);
            //update popupBar position
            //int lenPopupBar = barView.getWidth();
            int lenPopupBar = 140;
            //Log.d("setPosBarUP","lenPopupBar: "+lenPopupBar);

            int lenKey = focusedKeyView.getWidth();
            int heightKey = focusedKeyView.getKeyHeight();

            Rect keyViewCoordinates = rectCoordinate(focusedKeyView);
            int x = keyViewCoordinates.left;
            int y = keyViewCoordinates.top;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.leftMargin = x - (lenPopupBar/2) + (lenKey/2); //set x
            layoutParams.topMargin = y - 25; //set y
            barView.setLayoutParams(layoutParams);
        });
    }

    public void setPosBarDOWN(KeyView focusedKeyView, int indexRow){
        focusedKeyView.post(() -> {
            BarView barView = bars.get(indexRow);
            //update popupBar position
            //int lenPopupBar = barView.getWidth();
            int lenPopupBar = 140;
            //Log.d("setPosBarDOWN","lenPopupBar: "+lenPopupBar);

            int lenKey = focusedKeyView.getWidth();
            int heightKey = focusedKeyView.getKeyHeight();

            Rect keyViewCoordinates = rectCoordinate(focusedKeyView);
            int x = keyViewCoordinates.left;
            int y = keyViewCoordinates.top;

            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams2.leftMargin = x - (lenPopupBar/2) + (lenKey/2); //set x
            layoutParams2.topMargin = y + heightKey - 10; //set y
            barView.setLayoutParams(layoutParams2);
        });
    }
}
