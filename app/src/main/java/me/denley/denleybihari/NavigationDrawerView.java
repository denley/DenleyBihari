package me.denley.denleybihari;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * The root View for the navigation drawer.
 *
 * @author Denley Bihari
 */
public class NavigationDrawerView extends LinearLayout {

    @InjectView(R.id.profile_image) ImageView profileImage;

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        // Load circular version of the profile image
        if(!isInEditMode()) {
            Picasso.with(getContext())
                    .load(R.drawable.profile_image)
                    .transform(new CircleTransformation())
                    .into(profileImage);
        }
    }

    public void onDrawerClosed() {

    }

    public void onDrawerOpened() {

    }

    public void onDrawerSlide(float slideOffset) {

    }

}
