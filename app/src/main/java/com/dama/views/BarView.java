package com.dama.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.dama.controllers.Controller;
import com.dama.customkeyboardbarsovertv.R;
import com.dama.utils.Key;
import com.dama.utils.Utils;
import java.util.ArrayList;


public class BarView extends LinearLayout {
    private ArrayList<KeyView> keys;

    public BarView(Context context) {
        super(context);
    }

    public BarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initBarView(){
        setLayout();
        keys = new ArrayList<>();
    }

    private void setLayout(){
        setOrientation(HORIZONTAL);
        setBackgroundResource(R.drawable.popup_bar_background);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setElevation(getResources().getDimensionPixelSize(R.dimen.elevation));
        setPadding(getResources().getDimensionPixelSize(R.dimen.paddingVerticalBar), getResources().getDimensionPixelSize(R.dimen.paddingHorizontalBar), 0, 0);
    }

    public void addKeys(ArrayList<Key> allKeys){
        Drawable hiddenDr = ContextCompat.getDrawable(getContext(), R.drawable.key_hidden_background);
        Drawable keyDrawable = ContextCompat.getDrawable(getContext(), R.drawable.key_sug_background);
        String colorLabel = Utils.colorToString(ContextCompat.getColor(getContext(), R.color.label));

        int i=0;
        for(Key k: allKeys){
            KeyView keyView = null;
            if(k.getCode()== Controller.HIDDEN_KEY){
                keyView = new KeyView(getContext(),hiddenDr, "", colorLabel);
            }else{
                String s;
                if(k.getLabel().equals(" "))
                    s = "␣";
                else s = k.getLabel();

                keyView = new KeyView(getContext(),keyDrawable, s, colorLabel);
                //set dims
                int height = (int) getResources().getDimension(R.dimen.sug_key_height);
                int width = (int) getResources().getDimension(R.dimen.sug_key_width);
                int textSize = (int) getResources().getDimension(R.dimen.sug_key_text_size);
                keyView.changeDimension(height,width,textSize);
                //
                if(i==1){
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int margin = getResources().getDimensionPixelSize(R.dimen.marginCenterSuggestion);
                    layoutParams.setMargins(margin, 0, margin, 0);
                    keyView.setLayoutParams(layoutParams);
                }
                addView(keyView);
                i++;
            }
            keys.add(keyView);
        }
    }

    public void clearKeys(){
        removeAllViews();
        keys.clear();
    }

    public ArrayList<KeyView> getKeys() {
        return keys;
    }
}
