package me.denley.denleybihari;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * The root View for the navigation drawer.
 *
 * @author Denley Bihari
 */
public class NavigationDrawerView extends LinearLayout {

    /** A custom action that deselects every view given */
    private static final ButterKnife.Action<DrawerListItem> ACTION_DESELECT
            = new ButterKnife.Action<DrawerListItem>() {
        @Override public void apply(DrawerListItem view, int index) {
            view.setSelected(false);
        }
    };

    /**
     * An interface to the navigation drawer that allows it to
     * send callbacks to the containing Activity.
     */
    public interface PageLoader {
        /**
         * Requests that the given view be loaded into the
         * main content area, with the given title
         */
        public void loadPage(View page, int title);

        /** Requests that this navigation drawer be closed */
        public void closeDrawer();
    }



    /** The loader to handle navigation page loading */
    PageLoader pageLoader = null;

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
        if(view.isSelected()) {
            // Selected page is already loaded, just close the nav drawer
            pageLoader.closeDrawer();
        }else{
            // Apply selected state to view
            ButterKnife.apply(navigationButtons, ACTION_DESELECT);
            view.setSelected(true);

            // Load page
            if (pageLoader != null) {
                switch (view.getId()) {
                    case R.id.action_about: {
                        final ContentPageView newPage = new AboutView(getContext());
                        pageLoader.loadPage(newPage, R.string.app_name);
                        currentPage = 0;
                        newPage.setNextPageCallback(new ContentPageView.NextPageCallback() {
                            @Override public void onNextClick() {
                                onNavItemClick(navigationButtons.get(currentPage + 1));
                            }
                        });
                        break;
                    }case R.id.action_why: {
                        final ContentPageView newPage = new WhyView(getContext());
                        pageLoader.loadPage(newPage, R.string.title_why);
                        currentPage = 1;
                        newPage.setNextPageCallback(new ContentPageView.NextPageCallback() {
                            @Override public void onNextClick() {
                                onNavItemClick(navigationButtons.get(currentPage + 1));
                            }
                        });
                        break;
                    }case R.id.action_portfolio: {
                        final ContentPageView newPage = new ContentPageView(
                                getContext(),
                                R.layout.page_portfolio);

                        pageLoader.loadPage(newPage, R.string.title_portfolio);
                        currentPage = 2;
                        newPage.setNextPageCallback(new ContentPageView.NextPageCallback() {
                            @Override public void onNextClick() {
                                onNavItemClick(navigationButtons.get(currentPage + 1));
                            }
                        });
                        break;
                    }case R.id.action_testimonials: {
                        final ContentPageView newPage = new TestimonialsView(getContext());
                        pageLoader.loadPage(newPage, R.string.title_testimonials);
                        currentPage = 3;
                        newPage.setNextPageCallback(new ContentPageView.NextPageCallback() {
                            @Override public void onNextClick() {
                                onNavItemClick(navigationButtons.get(currentPage + 1));
                            }
                        });
                        break;
                    }case R.id.action_experience: {
                        pageLoader.loadPage(
                                new ExperienceView(getContext()),
                                R.string.title_experience
                        );
                        currentPage = 4;
                        break;
                    }
                }
            }
        }
    }

    /** Returns the currently selected page index */
    public int getCurrentPageIndex(){
        return currentPage;
    }

    /** Navigates to the given page index, calling the PageLoader's callback. */
    public void setCurrentPageIndex(final int index){
        currentPage = index;

        ButterKnife.apply(navigationButtons, ACTION_DESELECT);
        navigationButtons.get(currentPage).setSelected(true);
    }

}
