package me.denley.denleybihari;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A custom View representing one item on the experience page.
 *
 * @author Denley Bihari
 */
public class ExperienceCardView extends FrameLayout {

    @InjectView(R.id.role) TextView roleTextView;
    @InjectView(R.id.institution) TextView institutionTextView;
    @InjectView(R.id.timeline) TextView timelineTextView;
    @InjectView(R.id.icon)
    ImageView iconImageView;

    public ExperienceCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.card_experience, this);
        ButterKnife.inject(this);

        // Load custom attrs
        if(attrs!=null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ExperienceCardView, 0, 0);

            String role = a.getString(R.styleable.ExperienceCardView_ecv_role);
            String institution = a.getString(R.styleable.ExperienceCardView_ecv_institution);
            String timeline = a.getString(R.styleable.ExperienceCardView_ecv_timeline);
            int type = a.getInt(R.styleable.ExperienceCardView_ecv_type, -1);

            if(role!=null) {
                roleTextView.setText(role);
            }
            if(institution!=null) {
                institutionTextView.setText(institution);
            }
            if(timeline!=null) {
                timelineTextView.setText(timeline);
            }
            if(type!=-1){
                iconImageView.setImageResource(
                        type==0
                            ?R.drawable.ic_school_grey600_48dp
                            :R.drawable.ic_work_grey600_48dp
                );
            }
        }
    }

}
