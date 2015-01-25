package me.denley.denleybihari;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

/**
 * A custom View representing the "Portfolio" page.
 *
 * @author Denley Bihari
 */
public class PortfolioView extends FrameLayout {

    public PortfolioView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.page_portfolio, this);
        ButterKnife.inject(this);
    }

}
