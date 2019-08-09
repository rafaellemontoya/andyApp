package com.myt.andyapp.View;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Rafa Pc on 04/04/2018.
 */

public class FontTypeChange{

    private Context c;

    public FontTypeChange(Context s) {
        // TODO Auto-generated constructor stub
        this.c=s;
    }

    public Typeface get_fontface(int n)
    {
        Typeface tf;
        if(n==1)
            tf=Typeface.createFromAsset(c.getAssets(), "font/courgette_regular.ttf");
        else if(n==2)
            tf= Typeface.createFromAsset(c.getAssets(), "font/courgette_regular.ttf");
        else if(n==3)
            tf=Typeface.createFromAsset(c.getAssets(), "font/courgette_regular.ttf");
        else
            tf=Typeface.createFromAsset(c.getAssets(), "font/courgette_regular.ttf");
        return tf;
    }

}