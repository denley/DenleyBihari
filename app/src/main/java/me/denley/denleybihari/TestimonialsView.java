package me.denley.denleybihari;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A custom View representing the "Testimonials" page.
 *
 * @author Denley Bihari
 */
public class TestimonialsView extends ContentPageView {

    private static final String URL_MORE = "https://www.elance.com/s/denleybihari/job-history";


    public TestimonialsView(Context context) {
        super(context, R.layout.page_testimonials);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.action_more_testimonials)
    void onMoreClick(){
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(URL_MORE));

        try{
            getContext().startActivity(intent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(getContext(), R.string.error_cant_open, Toast.LENGTH_LONG).show();
        }
    }

}
