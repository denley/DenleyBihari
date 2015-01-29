package me.denley.denleybihari;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.denley.fab.FloatingActionsMenu;

/**
 * The primary Activity for this app.
 *
 * This class handles the navigation drawer setup, and the floating action menu behaviour.
 *
 * @author Denley Bihari
 */
public class MainActivity extends ActionBarActivity implements NavigationDrawerView.PageLoader{

    // Contact URLs for contact buttons
    private static final String URL_PHONE = "tel:+61431940243";
    private static final String URL_EMAIL = "mailto:denleybihari@gmail.com";
    private static final String URL_TWITTER = "https://twitter.com/denleybihari";
    private static final String URL_LOCATION
            = "geo:0,0?q=4/8+Eurilpa+Avenue,+Everard+Park,+South+Australia)";


    @InjectView(R.id.systemBarBackground) View systemBarBackground;
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.main_content) ScrollView mainContentView;
    @InjectView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @InjectView(R.id.navigation_drawer) NavigationDrawerView navigationDrawer;
    @InjectView(R.id.action_contact) FloatingActionsMenu contactButton;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSystemBarHeight();

        // Set up navigation drawer
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close){
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                contactButton.hide();
            }
            @Override public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if(newState== DrawerLayout.STATE_IDLE && !drawerLayout.isDrawerOpen(Gravity.START)){
                    contactButton.show();
                }
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        navigationDrawer.setPageLoader(this);
        drawerToggle.syncState();


        contactButton.attachToScrollView(mainContentView);
    }

    @Override public void loadPage(View page, int title) {
        mainContentView.scrollTo(0, 0);
        mainContentView.removeAllViews();
        mainContentView.addView(page);
        toolbar.setTitle(title);
        drawerLayout.closeDrawer(Gravity.START);
    }

    @Override public void closeDrawer(){
        drawerLayout.closeDrawer(Gravity.START);
    }

    @OnClick(R.id.action_contact_phone)
    void contactPhone(){
        contactButton.collapse();

        try {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(URL_PHONE)));
        }catch(ActivityNotFoundException e){
            Toast.makeText(this, R.string.error_cant_dial, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.action_contact_email)
    void contactEmail(){
        contactButton.collapse();

        try{
            startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(URL_EMAIL)));
        }catch(ActivityNotFoundException e){
            Toast.makeText(this, R.string.error_cant_email, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.action_contact_twitter)
    void contactTwitter(){
        contactButton.collapse();

        try{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_TWITTER)));
        }catch(ActivityNotFoundException e){
            Toast.makeText(this, R.string.error_cant_open, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.action_contact_location)
    void contactLocation(){
        contactButton.collapse();

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_LOCATION)));
        }catch(ActivityNotFoundException e){
            Toast.makeText(this, R.string.error_cant_open_location, Toast.LENGTH_LONG).show();
        }
    }

    @Override public void onBackPressed() {
        if(!navigationDrawer.navigateBack()){
            super.onBackPressed();
        }
    }

    /** Measures the status bar height, and sets the height of the overlay view accordingly */
    private void setSystemBarHeight() {
        int height = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getResources().getDimensionPixelSize(resourceId);
        }

        final ViewGroup.LayoutParams params = systemBarBackground.getLayoutParams();
        params.height = height;
        systemBarBackground.setLayoutParams(params);
    }

}
