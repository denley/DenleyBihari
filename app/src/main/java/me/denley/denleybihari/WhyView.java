package me.denley.denleybihari;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A custom View representing the "Why" page.
 *
 * @author Denley Bihari
 */
public class WhyView extends ContentPageView {

    @InjectView(R.id.content_you) TextView youTextView;
    @InjectView(R.id.content_this_job) TextView thisJobTextView;
    @InjectView(R.id.content_a_perfect_fit) TextView perfectFitTextView;


    public WhyView(Context context) {
        super(context, R.layout.page_why);
        ButterKnife.inject(this);

        // Setup HTML parsing and clickable links
        youTextView.setText(Html.fromHtml(context.getString(R.string.content_you)));
        youTextView.setMovementMethod(LinkMovementMethod.getInstance());
        thisJobTextView.setText(Html.fromHtml(context.getString(R.string.content_this_job)));
        thisJobTextView.setMovementMethod(LinkMovementMethod.getInstance());
        perfectFitTextView.setText(Html.fromHtml(context.getString(R.string.content_a_perfect_fit)));
        perfectFitTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
