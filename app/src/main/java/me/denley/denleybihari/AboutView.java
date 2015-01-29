package me.denley.denleybihari;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A custom View representing the "About Me" page.
 *
 * @author Denley Bihari
 */
public class AboutView extends FrameLayout {

    @InjectView(R.id.content_heart) TextView heartContent;
    @InjectView(R.id.content_trade) TextView tradeContent;
    @InjectView(R.id.profile_image) ImageView profileImage;


    public AboutView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.page_about, this);
        ButterKnife.inject(this);

        // Setup TextViews with clickable links
        heartContent.setText(Html.fromHtml(context.getString(R.string.content_heart)));
        heartContent.setMovementMethod(LinkMovementMethod.getInstance());
        tradeContent.setText(Html.fromHtml(context.getString(R.string.content_trade)));
        tradeContent.setMovementMethod(LinkMovementMethod.getInstance());

        // Load circular version of the profile image
        if(!isInEditMode()) {
            Picasso.with(getContext())
                    .load(R.drawable.profile_image)
                    .transform(new CircleTransformation())
                    .into(profileImage);
        }
    }

}
