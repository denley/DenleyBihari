package me.denley.denleybihari;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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

    @InjectViews({
            R.id.action_about,
            R.id.action_why,
            R.id.action_portfolio,
            R.id.action_testimonials,
            R.id.action_experience
    })
    List<DrawerListItem> navigationButtons;

    /** The index of the currently selected page */
    private int currentPage = 0;

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

    @Override public Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("currentPage", currentPage);
        return bundle;
    }

    @Override public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            currentPage = bundle.getInt("currentPage");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Select "About" by default
        onNavItemClick(navigationButtons.get(currentPage));
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
                        currentPage = 0;
                        pageLoader.loadPage(
                                new AboutView(getContext()),
                                R.string.app_name
                        );
                        break;
                    case R.id.action_why:
                        currentPage = 1;
                        pageLoader.loadPage(
                                new View(getContext()),
                                R.string.action_why
                        );
                        break;
                    case R.id.action_portfolio:
                        currentPage = 2;
                        pageLoader.loadPage(
                                new View(getContext()),
                                R.string.action_portfolio
                        );
                        break;
                    case R.id.action_testimonials:
                        currentPage = 3;
                        pageLoader.loadPage(
                                new View(getContext()),
                                R.string.action_testimonials
                        );
                        break;
                    case R.id.action_experience:
                        currentPage = 4;
                        pageLoader.loadPage(
                                new View(getContext()),
                                R.string.action_experience
                        );
                        break;
                }
            }
        }
    }

    /**
     * Attempts to navigate backwards through the page history.
     *
     * @return  true, if the navigation was successful.
     *          false, if the root element is already showing.
     */
    public boolean navigateBack(){
        final DrawerListItem rootItem = navigationButtons.get(0);
        if(rootItem.isSelected()){
            return false;
        }else{
            onNavItemClick(rootItem);
            return true;
        }
    }

}
