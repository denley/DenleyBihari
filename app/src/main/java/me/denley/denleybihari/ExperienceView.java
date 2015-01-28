package me.denley.denleybihari;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * A custom View representing the "Experience" page.
 *
 * @author Denley Bihari
 */
public class ExperienceView extends FrameLayout {

    public ExperienceView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.page_experience, this);
    }

}
