package me.denley.denleybihari;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A custom View representing one item on the portfolio page.
 *
 * @author Denley Bihari
 */
public class PortfolioCardView extends FrameLayout {

    @InjectView(R.id.name) TextView nameTextView;
    @InjectView(R.id.client) TextView clientTextView;
    @InjectView(R.id.icon) ImageView iconImageView;
    @InjectView(R.id.year) TextView yearTextView;

    /** The url to open when the portfolio item is clicked */
    String url = null;

    public PortfolioCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.card_portfolio, this);
        ButterKnife.inject(this);

        // Load custom attrs
        if(attrs!=null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.PortfolioCardView, 0, 0);

            url = a.getString(R.styleable.PortfolioCardView_pcv_url);
            String name = a.getString(R.styleable.PortfolioCardView_pcv_name);
            String client = a.getString(R.styleable.PortfolioCardView_pcv_client);
            String year = a.getString(R.styleable.PortfolioCardView_pcv_year);
            int icon = a.getResourceId(R.styleable.PortfolioCardView_pcv_icon, 0);

            if(name!=null) {
                nameTextView.setText(name);
            }
            if(client!=null && !client.isEmpty()) {
                clientTextView.setText(client);
            }else{
                clientTextView.setVisibility(View.GONE);
            }
            if(icon!=0){
                iconImageView.setImageResource(icon);
            }
            if(year!=null){
                yearTextView.setText(year);
            }
        }
    }

    @OnClick(R.id.download)
    void onDownloadClick(){
        if(url!=null) {
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            try {
                getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), R.string.error_cant_open, Toast.LENGTH_LONG).show();
            }
        }
    }

}
