package me.denley.denleybihari;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A custom View representing one item on the portfolio page.
 *
 * @author Denley Bihari
 */
public class PortfolioCardView extends FrameLayout {

    @InjectView(R.id.splash) ImageView splashImageView;
    @InjectView(R.id.name) TextView nameTextView;
    @InjectView(R.id.client) TextView clientTextView;
    @InjectView(R.id.icon) ImageView iconImageView;

    String url;

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
            int splashImage = a.getResourceId(R.styleable.PortfolioCardView_pcv_splash_image, 0);
            int icon = a.getResourceId(R.styleable.PortfolioCardView_pcv_icon, 0);

            if(name!=null) {
                nameTextView.setText(name);
            }
            if(client!=null) {
                clientTextView.setText(client);
            }
            if(splashImage!=0){
                Picasso.with(context)
                        .load(splashImage)
                        .into(splashImageView);
            }
            if(icon!=0){
                Picasso.with(context)
                        .load(icon)
                        .into(iconImageView);
            }
        }
    }

    @OnClick(R.id.download)
    void onDownloadClick(){
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        try {
            getContext().startActivity(intent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(getContext(), R.string.error_cant_open, Toast.LENGTH_LONG).show();
        }
    }

}
