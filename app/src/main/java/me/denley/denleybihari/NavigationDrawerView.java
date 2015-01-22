package me.denley.denleybihari;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * Created by Denley on 22/01/2015.
 */
public class NavigationDrawerView extends LinearLayout {

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ButterKnife.inject(this);
    }

    public void onDrawerClosed() {

    }

    public void onDrawerOpened() {

    }

    public void onDrawerSlide(float slideOffset) {

    }
}
