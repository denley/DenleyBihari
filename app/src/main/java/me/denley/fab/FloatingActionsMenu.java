package me.denley.fab;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;

import me.denley.denleybihari.R;

public class FloatingActionsMenu extends ViewGroup {
  public static final int EXPAND_UP = 0;
  public static final int EXPAND_DOWN = 1;
  public static final int EXPAND_LEFT = 2;
  public static final int EXPAND_RIGHT = 3;

  private static final int ANIMATION_DURATION = 300;
  private static final float COLLAPSED_PLUS_ROTATION = 0f;
  private static final float EXPANDED_PLUS_ROTATION = 90f + 45f;

  private int mAddButtonPlusColor;
  private int mAddButtonColorNormal;
  private int mAddButtonColorPressed;
  private int mExpandDirection;
  private int mAddButtonIcon = -1;

  private int mButtonSpacing;

  private boolean mExpanded;

  private AnimatorSet mExpandAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
  private AnimatorSet mCollapseAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
  private AddFloatingActionButton mAddButton;
  private RotatingDrawable mRotatingDrawable;

  public FloatingActionsMenu(Context context) {
    this(context, null);
  }

  public FloatingActionsMenu(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public FloatingActionsMenu(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attributeSet) {
    mAddButtonPlusColor = getColor(android.R.color.white);
    mAddButtonColorNormal = getColor(android.R.color.holo_blue_dark);
    mAddButtonColorPressed = getColor(android.R.color.holo_blue_light);
    mExpandDirection = EXPAND_UP;

    mButtonSpacing = (int) (getResources().getDimension(R.dimen.fab_actions_spacing) - getResources().getDimension(R.dimen.fab_shadow_radius) - getResources().getDimension(R.dimen.fab_shadow_offset));

    if (attributeSet != null) {
      TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.FloatingActionsMenu, 0, 0);
      if (attr != null) {
        try {
          mAddButtonIcon = attr.getResourceId(R.styleable.FloatingActionsMenu_fab_addButtonIconRes, -1);
          mAddButtonPlusColor = attr.getColor(R.styleable.FloatingActionsMenu_fab_addButtonPlusIconColor, getColor(android.R.color.white));
          mAddButtonColorNormal = attr.getColor(R.styleable.FloatingActionsMenu_fab_addButtonColorNormal, getColor(android.R.color.holo_blue_dark));
          mAddButtonColorPressed = attr.getColor(R.styleable.FloatingActionsMenu_fab_addButtonColorPressed, getColor(android.R.color.holo_blue_light));
          mExpandDirection = context.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE?EXPAND_LEFT:EXPAND_UP;
        } finally {
          attr.recycle();
        }
      }
    }

    createAddButton(context);
  }

  private static class RotatingDrawable extends LayerDrawable {
    public RotatingDrawable(Drawable drawable) {
      super(new Drawable[] { drawable });
    }

    private float mRotation;

    @SuppressWarnings("UnusedDeclaration")
    public float getRotation() {
      return mRotation;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setRotation(float rotation) {
      mRotation = rotation;
      invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
      canvas.save();
      canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
      super.draw(canvas);
      canvas.restore();
    }
  }

  private void createAddButton(Context context) {
    mAddButton = new AddFloatingActionButton(context, mAddButtonIcon) {
      @Override
      void updateBackground() {
        mPlusColor = mAddButtonPlusColor;
        mColorNormal = mAddButtonColorNormal;
        mColorPressed = mAddButtonColorPressed;
        super.updateBackground();
      }

      @Override
      Drawable getIconDrawable() {
        final RotatingDrawable rotatingDrawable = new RotatingDrawable(super.getIconDrawable());
        mRotatingDrawable = rotatingDrawable;

        final OvershootInterpolator interpolator = new OvershootInterpolator();

        final ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(
                rotatingDrawable, "rotation",
                mAddButtonIcon==-1?EXPANDED_PLUS_ROTATION:360f,
                COLLAPSED_PLUS_ROTATION);
        final ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(
                rotatingDrawable, "rotation",
                COLLAPSED_PLUS_ROTATION,
                mAddButtonIcon==-1?EXPANDED_PLUS_ROTATION:360f);

        collapseAnimator.setInterpolator(interpolator);
        expandAnimator.setInterpolator(interpolator);

        mExpandAnimation.play(expandAnimator);
        mCollapseAnimation.play(collapseAnimator);

        return rotatingDrawable;
      }
    };

    mAddButton.setId(R.id.fab_expand_menu_button);
    mAddButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        toggle();
      }
    });

    addView(mAddButton, super.generateDefaultLayoutParams());
  }

  private int getColor(@ColorRes int id) {
    return getResources().getColor(id);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    measureChildren(widthMeasureSpec, heightMeasureSpec);

    int width = 0;
    int height = 0;

    for (int i = 0; i < getChildCount(); i++) {
      View child = getChildAt(i);

      switch (mExpandDirection) {
        case EXPAND_UP:
        case EXPAND_DOWN:
          width = Math.max(width, child.getMeasuredWidth());
          height += child.getMeasuredHeight();
          break;
        case EXPAND_LEFT:
        case EXPAND_RIGHT:
          width += child.getMeasuredWidth();
          height = Math.max(height, child.getMeasuredHeight());
      }
    }

    switch (mExpandDirection) {
      case EXPAND_UP:
      case EXPAND_DOWN:
        height += mButtonSpacing * (getChildCount() - 1);
        height = height * 12 / 10; // for overshoot
        break;
      case EXPAND_LEFT:
      case EXPAND_RIGHT:
        width += mButtonSpacing * (getChildCount() - 1);
        width = width * 12 / 10; // for overshoot
    }

    setMeasuredDimension(width, height);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    switch (mExpandDirection) {
      case EXPAND_UP:
      case EXPAND_DOWN:
        boolean expandUp = mExpandDirection == EXPAND_UP;

        int addButtonY = expandUp ? b - t - mAddButton.getMeasuredHeight() : 0;
        mAddButton.layout(0, addButtonY, mAddButton.getMeasuredWidth(), addButtonY + mAddButton.getMeasuredHeight());

        int nextY = expandUp ?
            addButtonY - mButtonSpacing :
            addButtonY + mAddButton.getMeasuredHeight() + mButtonSpacing;

        for (int i = getChildCount() - 1; i >= 0; i--) {
          final View child = getChildAt(i);

          if (child == mAddButton) continue;

          int childX = (mAddButton.getMeasuredWidth() - child.getMeasuredWidth()) / 2;
          int childY = expandUp ? nextY - child.getMeasuredHeight() : nextY;
          child.layout(childX, childY, childX + child.getMeasuredWidth(), childY + child.getMeasuredHeight());

          float collapsedTranslation = addButtonY - childY;
          float expandedTranslation = 0f;

          child.setTranslationY(mExpanded ? expandedTranslation : collapsedTranslation);
          child.setAlpha(mExpanded ? 1f : 0f);

          LayoutParams params = (LayoutParams) child.getLayoutParams();
          params.mCollapseDir.setFloatValues(expandedTranslation, collapsedTranslation);
          params.mExpandDir.setFloatValues(collapsedTranslation, expandedTranslation);
          params.setAnimationsTarget(child);

          nextY = expandUp ?
              childY - mButtonSpacing :
              childY + child.getMeasuredHeight() + mButtonSpacing;
        }
        break;

      case EXPAND_LEFT:
      case EXPAND_RIGHT:
        boolean expandLeft = mExpandDirection == EXPAND_LEFT;

        int addButtonX = expandLeft ? r - l - mAddButton.getMeasuredWidth() : 0;
        mAddButton.layout(addButtonX, 0, addButtonX + mAddButton.getMeasuredWidth(), mAddButton.getMeasuredHeight());

        int nextX = expandLeft ?
            addButtonX - mButtonSpacing :
            addButtonX + mAddButton.getMeasuredWidth() + mButtonSpacing;

        for (int i = getChildCount() - 1; i >= 0; i--) {
          final View child = getChildAt(i);

          if (child == mAddButton) continue;

          int childX = expandLeft ? nextX - child.getMeasuredWidth() : nextX;
          int childY = (mAddButton.getMeasuredHeight() - child.getMeasuredHeight()) / 2;
          child.layout(childX, childY, childX + child.getMeasuredWidth(), childY + child.getMeasuredHeight());

          float collapsedTranslation = addButtonX - childX;
          float expandedTranslation = 0f;

          child.setTranslationX(mExpanded ? expandedTranslation : collapsedTranslation);
          child.setAlpha(mExpanded ? 1f : 0f);

          LayoutParams params = (LayoutParams) child.getLayoutParams();
          params.mCollapseDir.setFloatValues(expandedTranslation, collapsedTranslation);
          params.mExpandDir.setFloatValues(collapsedTranslation, expandedTranslation);
          params.setAnimationsTarget(child);

          nextX = expandLeft ?
              childX - mButtonSpacing :
              childX + child.getMeasuredWidth() + mButtonSpacing;
        }
    }
  }

  @Override
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(super.generateDefaultLayoutParams());
  }

  @Override
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new LayoutParams(super.generateLayoutParams(attrs));
  }

  @Override
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
    return new LayoutParams(super.generateLayoutParams(p));
  }

  @Override
  protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
    return super.checkLayoutParams(p);
  }

  private static Interpolator sExpandInterpolator = new OvershootInterpolator();
  private static Interpolator sCollapseInterpolator = new DecelerateInterpolator(3f);
  private static Interpolator sAlphaExpandInterpolator = new DecelerateInterpolator();

  private class LayoutParams extends ViewGroup.LayoutParams {

    private ObjectAnimator mExpandDir = new ObjectAnimator();
    private ObjectAnimator mExpandAlpha = new ObjectAnimator();
    private ObjectAnimator mCollapseDir = new ObjectAnimator();
    private ObjectAnimator mCollapseAlpha = new ObjectAnimator();

    public LayoutParams(ViewGroup.LayoutParams source) {
      super(source);

      mExpandDir.setInterpolator(sExpandInterpolator);
      mExpandAlpha.setInterpolator(sAlphaExpandInterpolator);
      mCollapseDir.setInterpolator(sCollapseInterpolator);
      mCollapseAlpha.setInterpolator(sCollapseInterpolator);

      mCollapseAlpha.setProperty(View.ALPHA);
      mCollapseAlpha.setFloatValues(1f, 0f);

      mExpandAlpha.setProperty(View.ALPHA);
      mExpandAlpha.setFloatValues(0f, 1f);

      switch (mExpandDirection) {
        case EXPAND_UP:
        case EXPAND_DOWN:
          mCollapseDir.setProperty(View.TRANSLATION_Y);
          mExpandDir.setProperty(View.TRANSLATION_Y);
          break;
        case EXPAND_LEFT:
        case EXPAND_RIGHT:
          mCollapseDir.setProperty(View.TRANSLATION_X);
          mExpandDir.setProperty(View.TRANSLATION_X);
      }

      mExpandAnimation.play(mExpandAlpha);
      mExpandAnimation.play(mExpandDir);

      mCollapseAnimation.play(mCollapseAlpha);
      mCollapseAnimation.play(mCollapseDir);
    }

    public void setAnimationsTarget(View view) {
      mCollapseAlpha.setTarget(view);
      mCollapseDir.setTarget(view);
      mExpandAlpha.setTarget(view);
      mExpandDir.setTarget(view);
    }
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    bringChildToFront(mAddButton);
  }

  public void collapse() {
    if (mExpanded) {
      mExpanded = false;
      mCollapseAnimation.start();
      mExpandAnimation.cancel();
    }
  }

  public void toggle() {
    if (mExpanded) {
      collapse();
    } else {
      expand();
    }
  }

  public void expand() {
    if (!mExpanded) {
      mExpanded = true;
      mCollapseAnimation.cancel();
      mExpandAnimation.start();
    }
  }

  @Override
  public Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    SavedState savedState = new SavedState(superState);
    savedState.mExpanded = mExpanded;

    return savedState;
  }

  @Override
  public void onRestoreInstanceState(Parcelable state) {
    if (state instanceof SavedState) {
      SavedState savedState = (SavedState) state;
      mExpanded = savedState.mExpanded;

      if (mRotatingDrawable != null) {
        mRotatingDrawable.setRotation(mExpanded
                ? (mAddButtonIcon==-1?EXPANDED_PLUS_ROTATION:360f)
                : COLLAPSED_PLUS_ROTATION);
      }

      super.onRestoreInstanceState(savedState.getSuperState());
    } else {
      super.onRestoreInstanceState(state);
    }
  }

  public static class SavedState extends BaseSavedState {
    public boolean mExpanded;

    public SavedState(Parcelable parcel) {
      super(parcel);
    }

    private SavedState(Parcel in) {
      super(in);
      mExpanded = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(@NonNull Parcel out, int flags) {
      super.writeToParcel(out, flags);
      out.writeInt(mExpanded ? 1 : 0);
    }

    public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

      @Override
      public SavedState createFromParcel(Parcel in) {
        return new SavedState(in);
      }

      @Override
      public SavedState[] newArray(int size) {
        return new SavedState[size];
      }
    };
  }



    //
    // Show/Hide behaviour
    //

    private static final int TRANSLATE_DURATION_MILLIS = 200;

    boolean mVisible = true;
    boolean mFabVisible = true;
    protected AbsListView mListView;
    protected RecyclerView mRecyclerView;
    private FabOnScrollListener mOnScrollListener;
    private FabRecyclerOnViewScrollListener mRecyclerViewOnScrollListener;
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public void setFabVisible(final boolean visible, final boolean animate){
        mFabVisible = visible;
        if(!visible) hide(animate);
        else show(animate);
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        if(mFabVisible) {
            toggle(true, animate, false);
        }
    }

    public void hide(boolean animate) {
        toggle(false, animate, false);
        collapse();
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height + getMarginBottom();
            if (animate) {
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                setTranslationY(translationY);
            }
        }
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    /**
     * If need to use custom {@link android.widget.AbsListView.OnScrollListener},
     * pass it to {@link #attachToListView(android.widget.AbsListView, FabOnScrollListener)}
     */
    public void attachToListView(@NonNull AbsListView listView) {
        attachToListView(listView, new FabOnScrollListener());
    }

    /**
     * If need to use custom {@link android.widget.AbsListView.OnScrollListener},
     * pass it to {@link #attachToListView(android.widget.AbsListView, FabOnScrollListener)}
     */
    public void attachToRecyclerView(@NonNull RecyclerView recyclerView) {
        attachToRecyclerView(recyclerView, new FabRecyclerOnViewScrollListener());
    }

    public void attachToListView(@NonNull AbsListView listView, @NonNull FabOnScrollListener onScrollListener) {
        mListView = listView;
        mOnScrollListener = onScrollListener;
        onScrollListener.setFloatingActionButton(this);
        onScrollListener.setListView(listView);
        mListView.setOnScrollListener(onScrollListener);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView, @NonNull FabRecyclerOnViewScrollListener onScrollListener) {
        mRecyclerView = recyclerView;
        mRecyclerViewOnScrollListener = onScrollListener;
        onScrollListener.setFloatingActionButton(this);
        onScrollListener.setRecyclerView(recyclerView);
        mRecyclerView.setOnScrollListener(onScrollListener);
    }

    /**
     * Shows/hides the FAB when the attached {@link AbsListView} scrolling events occur.
     * Extend this class and override {@link FabOnScrollListener#onScrollDown()}/{@link FabOnScrollListener#onScrollUp()}
     * if you need custom code to be executed on these events.
     */
    public static class FabOnScrollListener extends ScrollDirectionDetector implements ScrollDirectionListener {
        private FloatingActionsMenu mFloatingActionButton;

        public FabOnScrollListener() {
            setScrollDirectionListener(this);
        }

        private void setFloatingActionButton(@NonNull FloatingActionsMenu floatingActionButton) {
            mFloatingActionButton = floatingActionButton;
        }

        /**
         * Called when the attached {@link AbsListView} is scrolled down.
         * <br />
         * <br />
         * <i>Derived classes should call the super class's implementation of this method.
         * If they do not, the FAB will not react to AbsListView's scrolling events.</i>
         */
        @Override
        public void onScrollDown() {
            mFloatingActionButton.show();
        }

        /**
         * Called when the attached {@link AbsListView} is scrolled up.
         * <br />
         * <br />
         * <i>Derived classes should call the super class's implementation of this method.
         * If they do not, the FAB will not react to AbsListView's scrolling events.</i>
         */
        @Override
        public void onScrollUp() {
            mFloatingActionButton.hide();
        }
    }

    /**
     * Shows/hides the FAB when the attached {@link RecyclerView} scrolling events occur.
     * Extend this class and override {@link FabOnScrollListener#onScrollDown()}/{@link FabOnScrollListener#onScrollUp()}
     * if you need custom code to be executed on these events.
     */
    public static class FabRecyclerOnViewScrollListener extends ScrollDirectionRecyclerViewDetector implements ScrollDirectionListener {
        private FloatingActionsMenu mFloatingActionButton;

        public FabRecyclerOnViewScrollListener() {
            setScrollDirectionListener(this);
        }

        private void setFloatingActionButton(@NonNull FloatingActionsMenu floatingActionButton) {
            mFloatingActionButton = floatingActionButton;
        }

        /**
         * Called when the attached {@link RecyclerView} is scrolled down.
         * <br />
         * <br />
         * <i>Derived classes should call the super class's implementation of this method.
         * If they do not, the FAB will not react to RecyclerView's scrolling events.</i>
         */
        @Override
        public void onScrollDown() {
            mFloatingActionButton.show();
        }

        /**
         * Called when the attached {@link RecyclerView} is scrolled up.
         * <br />
         * <br />
         * <i>Derived classes should call the super class's implementation of this method.
         * If they do not, the FAB will not react to RecyclerView's scrolling events.</i>
         */
        @Override
        public void onScrollUp() {
            mFloatingActionButton.hide();
        }
    }

    public static interface ScrollDirectionListener {
        void onScrollDown();

        void onScrollUp();
    }

    /**
     * Detects which direction list view was scrolled.
     * <p/>
     * Set {@link ScrollDirectionListener} to get callbacks
     * {@link ScrollDirectionListener#onScrollDown()} or
     * {@link ScrollDirectionListener#onScrollUp()}
     *
     * @author Aidan Follestad
     */
    public static abstract class ScrollDirectionRecyclerViewDetector extends RecyclerView.OnScrollListener {
        private ScrollDirectionListener mScrollDirectionListener;
        private int mPreviousScrollY;
        private int mPreviousFirstVisibleItem;
        public int mLastChangeY;
        private RecyclerView mRecyclerView;
        private int mMinSignificantScroll;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            mMinSignificantScroll = recyclerView.getContext().getResources().getDimensionPixelOffset(R.dimen.fab_min_significant_scroll);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int newScrollY = estimateScrollY();
            if (mScrollDirectionListener != null && isSameRow(getFirstVisibleItem()) && isSignificantDelta(newScrollY)) {
                if (isScrollUp(newScrollY)) {
                    mScrollDirectionListener.onScrollUp();
                } else {
                    mScrollDirectionListener.onScrollDown();
                }
            }
        }

        public ScrollDirectionListener getScrollDirectionListener() {
            return mScrollDirectionListener;
        }

        public void setScrollDirectionListener(ScrollDirectionListener mScrollDirectionListener) {
            this.mScrollDirectionListener = mScrollDirectionListener;
        }

        /**
         * @return true if scrolled up or false otherwise
         * @see #isSignificantDelta(int) which ensures, that events are not fired it there was no scrolling
         */
        private boolean isScrollUp(int newScrollY) {
            boolean scrollUp = newScrollY > mPreviousScrollY;
            mPreviousScrollY = newScrollY;
            return scrollUp;
        }

        /**
         * Make sure wrong direction method is not called when stopping scrolling
         * and finger moved a little to opposite direction.
         *
         * @see #isScrollUp(int)
         */
        private boolean isSignificantDelta(int newScrollY) {
            boolean isSignificantDelta = Math.abs(mLastChangeY - newScrollY) > mMinSignificantScroll;
            if (isSignificantDelta)
                mLastChangeY = newScrollY;
            return isSignificantDelta;
        }

        /**
         * <code>newScrollY</code> position might not be correct if:
         * <ul>
         * <li><code>firstVisibleItem</code> is different than <code>mPreviousFirstVisibleItem</code></li>
         * <li>list has rows of different height</li>
         * </ul>
         * <p/>
         * It's necessary to track if row did not change, so events
         * {@link ScrollDirectionListener#onScrollUp()} or {@link ScrollDirectionListener#onScrollDown()} could be fired with confidence
         *
         * @see #estimateScrollY()
         */
        private boolean isSameRow(int firstVisibleItem) {
            boolean rowsChanged = firstVisibleItem == mPreviousFirstVisibleItem;
            mPreviousFirstVisibleItem = firstVisibleItem;
            return rowsChanged;
        }

        /**
         * Will be incorrect if rows has changed and if list has rows of different heights
         * <p/>
         * So when measuring scroll direction, it's necessary to ignore this value
         * if first visible row is different than previously calculated.
         *
         * @deprecated because it should be used with caution
         */
        private int estimateScrollY() {
            if (mRecyclerView == null || mRecyclerView.getChildAt(0) == null) return 0;
            View topChild = mRecyclerView.getChildAt(0);
            return getFirstVisibleItem() * topChild.getHeight() - topChild.getTop();
        }

        private int getFirstVisibleItem() {
            RecyclerView.LayoutManager mLayoutManager = mRecyclerView.getLayoutManager();
            if (mLayoutManager == null)
                throw new IllegalStateException("Your RecyclerView does not have a LayoutManager.");
            if (mLayoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            } else {
                throw new RuntimeException("Currently only LinearLayoutManager is supported for the RecyclerView.");
            }
        }

        public void setRecyclerView(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }
    }

    /**
     * Detects which direction list view was scrolled.
     * <p/>
     * Set {@link ScrollDirectionListener} to get callbacks
     * {@link ScrollDirectionListener#onScrollDown()} or
     * {@link ScrollDirectionListener#onScrollUp()}
     *
     * @author Vilius Kraujutis
     */
    public static abstract class ScrollDirectionDetector implements AbsListView.OnScrollListener {
        private ScrollDirectionListener mScrollDirectionListener;
        private int mPreviousScrollY;
        private int mPreviousFirstVisibleItem;
        public int mLastChangeY;
        private AbsListView mListView;
        private int mMinSignificantScroll;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mMinSignificantScroll = view.getContext().getResources().getDimensionPixelOffset(R.dimen.fab_min_significant_scroll);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int newScrollY = estimateScrollY();
            if (mScrollDirectionListener != null && isSameRow(firstVisibleItem) && isSignificantDelta(newScrollY)) {
                if (isScrollUp(newScrollY)) {
                    mScrollDirectionListener.onScrollUp();
                } else {
                    mScrollDirectionListener.onScrollDown();
                }
            }
        }

        public ScrollDirectionListener getScrollDirectionListener() {
            return mScrollDirectionListener;
        }

        public void setScrollDirectionListener(ScrollDirectionListener mScrollDirectionListener) {
            this.mScrollDirectionListener = mScrollDirectionListener;
        }

        /**
         * @return true if scrolled up or false otherwise
         * @see #isSignificantDelta(int) which ensures, that events are not fired it there was no scrolling
         */
        private boolean isScrollUp(int newScrollY) {
            boolean scrollUp = newScrollY > mPreviousScrollY;
            mPreviousScrollY = newScrollY;
            return scrollUp;
        }

        /**
         * Make sure wrong direction method is not called when stopping scrolling
         * and finger moved a little to opposite direction.
         *
         * @see #isScrollUp(int)
         */
        private boolean isSignificantDelta(int newScrollY) {
            boolean isSignificantDelta = Math.abs(mLastChangeY - newScrollY) > mMinSignificantScroll;
            if (isSignificantDelta)
                mLastChangeY = newScrollY;
            return isSignificantDelta;
        }

        /**
         * <code>newScrollY</code> position might not be correct if:
         * <ul>
         * <li><code>firstVisibleItem</code> is different than <code>mPreviousFirstVisibleItem</code></li>
         * <li>list has rows of different height</li>
         * </ul>
         * <p/>
         * It's necessary to track if row did not change, so events
         * {@link ScrollDirectionListener#onScrollUp()} or {@link ScrollDirectionListener#onScrollDown()} could be fired with confidence
         *
         * @see #estimateScrollY()
         */
        private boolean isSameRow(int firstVisibleItem) {
            boolean rowsChanged = firstVisibleItem == mPreviousFirstVisibleItem;
            mPreviousFirstVisibleItem = firstVisibleItem;
            return rowsChanged;
        }

        /**
         * Will be incorrect if rows has changed and if list has rows of different heights
         * <p/>
         * So when measuring scroll direction, it's necessary to ignore this value
         * if first visible row is different than previously calculated.
         *
         * @deprecated because it should be used with caution
         */
        private int estimateScrollY() {
            if (mListView == null || mListView.getChildAt(0) == null) return 0;
            View topChild = mListView.getChildAt(0);
            return mListView.getFirstVisiblePosition() * topChild.getHeight() - topChild.getTop();
        }

        public void setListView(AbsListView listView) {
            mListView = listView;
        }
    }

}
