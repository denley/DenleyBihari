package me.denley.denleybihari;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A custom View representing a list item in the navigation drawer.
 *
 * @author Denley Bihari
 */
public class DrawerListItem extends LinearLayout {

    @InjectView(R.id.icon) ImageView iconView;
    @InjectView(R.id.text) TextView textView;

    public DrawerListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setupBackground(context);

        // Setup child views
        LayoutInflater.from(context).inflate(R.layout.list_item_drawer, this);
        ButterKnife.inject(this);

        // Load custom attrs
        if(attrs!=null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.DrawerListItem, 0, 0);

            Drawable icon = a.getDrawable(R.styleable.DrawerListItem_dli_icon);
            String text = a.getString(R.styleable.DrawerListItem_dli_text);

            if(icon!=null) {
                iconView.setImageDrawable(icon);
            }
            if(text!=null) {
                textView.setText(text);
            }
        }

    }

    private void setupBackground(final Context context){
        final TypedArray backgroundArray = context.obtainStyledAttributes(
                new int[]{R.attr.selectableItemBackground});
        final Drawable normalPressedBackground = backgroundArray.getDrawable(0);
        backgroundArray.recycle();

        final Drawable selectedBackground = new ColorDrawable(
                context.getResources().getColor(R.color.selected_item_background));

        final StateListDrawable background = new StateListDrawable();
        background.addState(new int[]{android.R.attr.state_selected}, selectedBackground);
        background.addState(StateSet.WILD_CARD, normalPressedBackground);

        setBackgroundDrawable(background);
    }

}
