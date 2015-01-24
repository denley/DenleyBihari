package me.denley.denleybihari;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * The root View for the navigation drawer.
 *
 * @author Denley Bihari
 */
public class NavigationDrawerView extends LinearLayout {

    private static final ButterKnife.Action<DrawerListItem> ACTION_DESELECT
            = new ButterKnife.Action<DrawerListItem>() {
        @Override public void apply(DrawerListItem view, int index) {
            view.setSelected(false);
        }
    };

    public interface PageLoader {
        public void loadPage(View page, int title);
    }



    /** The loader to handle navigation page loading */
    PageLoader pageLoader = null;

    @InjectView(R.id.profile_image) ImageView profileImage;
    @InjectView(R.id.action_about) DrawerListItem aboutButton;

    @InjectViews({
            R.id.action_about,
            R.id.action_why,
            R.id.action_portfolio,
            R.id.action_testimonials,
            R.id.action_experience
    })
    List<DrawerListItem> navigationButtons;

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

        // Select "About" by default
        onNavItemClick(aboutButton);
    }

    /** Sets the page loader to handle navigation selection callbacks */
    public void setPageLoader(PageLoader l){
        pageLoader = l;
    }

    @OnClick({
            R.id.action_about,
            R.id.action_why,
            R.id.action_portfolio,
            R.id.action_testimonials,
            R.id.action_experience
    })
    void onNavItemClick(DrawerListItem view){
        if(!view.isSelected()) {
            // Apply selected state to view
            ButterKnife.apply(navigationButtons, ACTION_DESELECT);
            view.setSelected(true);

            // Load page
            if (pageLoader != null) {
                switch (view.getId()) {
                    case R.id.action_about:
                        pageLoader.loadPage(new View(getContext()), R.string.action_about);
                        break;
                    case R.id.action_why:
                        pageLoader.loadPage(new View(getContext()), R.string.action_why);
                        break;
                    case R.id.action_portfolio:
                        pageLoader.loadPage(new View(getContext()), R.string.action_portfolio);
                        break;
                    case R.id.action_testimonials:
                        pageLoader.loadPage(new View(getContext()), R.string.action_testimonials);
                        break;
                    case R.id.action_experience:
                        pageLoader.loadPage(new View(getContext()), R.string.action_experience);
                        break;
                }
            }
        }
    }

}
