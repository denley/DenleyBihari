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

    /** Page titles in order */
    private static final int[] TITLES = {
            R.string.app_name,
            R.string.title_why,
            R.string.title_portfolio,
            R.string.title_testimonials,
            R.string.title_experience
    };

    /** Action labels for pages (in order) */
    private static final int[] ACTION_NAMES = {
            R.string.action_about,
            R.string.action_why,
            R.string.action_portfolio,
            R.string.action_testimonials,
            R.string.action_experience
    };

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
                final int pageIndex = navigationButtons.indexOf(view);
                pageLoader.loadPage(
                        createPageForIndex(pageIndex),
                        TITLES[pageIndex]
                );
                currentPage = pageIndex;
            }
        }
    }

    /** Creates the View object for the page at the given position */
    public View createPageForIndex(final int pageIndex){

        ContentPageView newPage;
        switch (pageIndex) {
            case 0:
                newPage = new AboutView(getContext());
                break;
            case 1:
                newPage = new WhyView(getContext());
                break;
            case 2:
                newPage = new ContentPageView(getContext(), R.layout.page_portfolio);
                break;
            case 3:
                newPage = new TestimonialsView(getContext());
                break;
            case 4:
                return new ExperienceView(getContext());
            default:
                throw new IndexOutOfBoundsException("Page index out of bounds");
        }

        // Setup next button
        newPage.setNextPageCallback(new ContentPageView.NextPageCallback() {
            @Override public void onNextClick() {
                onNavItemClick(navigationButtons.get(pageIndex + 1));
            }
        });
        newPage.setNextButtonText(ACTION_NAMES[pageIndex + 1]);

        return newPage;
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
