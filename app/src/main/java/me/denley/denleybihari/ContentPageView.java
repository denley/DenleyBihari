package me.denley.denleybihari;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * A custom View representing the a page of the main content area.
 *
 * This class can be used as an abstract/generic framework for a page,
 * or as the page itself, if it only needs inflating a layout.
 *
 * @author Denley Bihari
 */
@SuppressLint("ViewConstructor")
public class ContentPageView extends FrameLayout implements View.OnClickListener {

    /**
     * An interface to the that allows this page to send next button click events
     * to the containing activity.
     */
    public interface NextPageCallback {
        /**
         * Called when the 'next' button is clicked.
         * The implementation should handle the event.
         */
        public void onNextClick();
    }


    private NextPageCallback nextPageCallback = null;

    private Button nextButton;

    public ContentPageView(Context context, int layoutResId) {
        super(context);

        // Inflate the wrapper
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.page_container, this);

        final ViewGroup pageContainer = (ViewGroup) findViewById(R.id.main_content);
        nextButton = (Button) findViewById(R.id.action_next);
        nextButton.setOnClickListener(this);

        // Inflate the content
        inflater.inflate(layoutResId, pageContainer);
    }

    @Override public void onClick(View v) {
        if(nextPageCallback!=null){
            nextPageCallback.onNextClick();
        }
    }

    /** Sets the callback to be invoked when the 'next' button is clicked */
    public void setNextPageCallback(NextPageCallback callback){
        nextPageCallback = callback;
    }

    /** Sets the text shown inside the 'next page' button */
    public void setNextButtonText(int textResId){
        final String text = getContext().getString(textResId);
        nextButton.setText(text + " ->");
    }

}
