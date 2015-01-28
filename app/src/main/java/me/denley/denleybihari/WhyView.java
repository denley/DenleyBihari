package me.denley.denleybihari;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

/**
 * A custom View representing the "Why" page.
 *
 * @author Denley Bihari
 */
public class WhyView extends FrameLayout {

    public WhyView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.page_why, this);
        ButterKnife.inject(this);
    }

}
