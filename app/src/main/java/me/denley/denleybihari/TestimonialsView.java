package me.denley.denleybihari;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

/**
 * A custom View representing the "Testimonials" page.
 *
 * @author Denley Bihari
 */
public class TestimonialsView extends FrameLayout {

    public TestimonialsView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.page_testimonials, this);
        ButterKnife.inject(this);
    }

}
