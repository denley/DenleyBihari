package me.denley.denleybihari;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A custom View representing one testimonial on the testimonials page.
 *
 * @author Denley Bihari
 */
public class TestimonialCardView extends FrameLayout {

    @InjectView(R.id.quote) TextView quoteTextView;
    @InjectView(R.id.author) TextView authorTextView;

    public TestimonialCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.card_testimonial, this);
        ButterKnife.inject(this);

        // Load custom attrs
        if(attrs!=null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.TestimonialCardView, 0, 0);

            String quote = a.getString(R.styleable.TestimonialCardView_tcv_quote);
            String author = a.getString(R.styleable.TestimonialCardView_tcv_author);

            if(quote!=null) {
                quoteTextView.setText(quote);
            }
            if(author!=null) {
                authorTextView.setText("- "+author);
            }
        }
    }

}
