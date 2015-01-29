package me.denley.denleybihari;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A custom View representing the "About Me" page.
 *
 * @author Denley Bihari
 */
public class AboutView extends ContentPageView {

    @InjectView(R.id.content_heart) TextView heartContent;
    @InjectView(R.id.content_trade) TextView tradeContent;


    public AboutView(Context context) {
        super(context, R.layout.page_about);
        ButterKnife.inject(this);

        // Setup TextViews with clickable links
        heartContent.setText(Html.fromHtml(context.getString(R.string.content_heart)));
        heartContent.setMovementMethod(LinkMovementMethod.getInstance());
        tradeContent.setText(Html.fromHtml(context.getString(R.string.content_trade)));
        tradeContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
