package com.vgroyalchemist.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vgroyalchemist.R;


/**
 * View that provides quick feedback about an operation in a small popup at the base of the screen
 */
public class Snackbar extends SnackbarLayout {

    private static int PHONE_MIN_HEIGHT_DP = 150;
    private static int PHONE_MAX_HEIGHT_DP = 300;

    private static int TABLET_MIN_WIDTH_DP = 500;
    private static int TABLET_MAX_WIDTH_DP = 2000;
    private static int TABLET_HEIGHT_DP = 48;
    private static int TABLET_MARGIN_DP = 24;

    public enum SnackbarType {
        SINGLE_LINE(PHONE_MIN_HEIGHT_DP, 1), MULTI_LINE(PHONE_MAX_HEIGHT_DP, 10);

        private int height;
        private int maxLines;

        SnackbarType(int height, int maxLines) {
            this.height = height;
            this.maxLines = maxLines;
        }

        public int getHeight() {
            return height;
        }

        public int getMaxLines() {
            return maxLines;
        }
    }

    public enum SnackbarDuration {
        LENGTH_SHORT(1500), LENGTH_LONG(3500), CUSTOM_LENGTH(300);

        private long duration;

        SnackbarDuration(long duration) {
            this.duration = duration;
        }

        public long getDuration() {
            return duration;
        }
    }

    private SnackbarType mType = SnackbarType.MULTI_LINE;
    private SnackbarDuration mDuration = SnackbarDuration.LENGTH_SHORT;
    private CharSequence mText;
    private int mColor = getResources().getColor(R.color.parrot_green);
    private int mTextColor = Color.WHITE;
    private CharSequence mActionLabel;
    private int mActionColor = Color.GREEN;
    private boolean mAnimated = true;
    private long mCustomDuration = -1;
    private ActionClickListener mActionClickListener;
    private boolean mShouldDismiss = true;
    private EventListener mEventListener;
    private boolean mIsShowing = false;
    private boolean mCanSwipeToDismiss = true;

    private Snackbar(Context context) {
        super(context);
    }

    public static Snackbar with(Context context) {
        return new Snackbar(context);
    }


    public Snackbar type(SnackbarType type) {
        mType = type;
        return this;
    }


    public Snackbar text(CharSequence text) {
        mText = text;
        return this;
    }


    public Snackbar color(int color) {
        mColor = color;
        return this;
    }


    public Snackbar textColor(int textColor) {
        mTextColor = textColor;
        return this;
    }

    /**
     * Sets the action label to be displayed, if any. Note that if this is not set, the action
     * button will not be displayed
     *
     * @param actionButtonLabel
     * @return
     */
    public Snackbar actionLabel(CharSequence actionButtonLabel) {
        mActionLabel = actionButtonLabel;
        return this;
    }


    public Snackbar actionColor(int actionColor) {
        mActionColor = actionColor;
        return this;
    }

    public Snackbar dismissOnActionClicked(boolean shouldDismiss) {
        mShouldDismiss = shouldDismiss;
        return this;
    }


    public Snackbar actionListener(ActionClickListener listener) {
        mActionClickListener = listener;
        return this;
    }


    public Snackbar eventListener(EventListener listener) {
        mEventListener = listener;
        return this;
    }


    public Snackbar animation(boolean withAnimation) {
        mAnimated = withAnimation;
        return this;
    }

    /**
     *
     *
     * @param canSwipeToDismiss
     * @return
     */
    public Snackbar swipeToDismiss(boolean canSwipeToDismiss) {
        mCanSwipeToDismiss = canSwipeToDismiss;
        return this;
    }


    public Snackbar duration(SnackbarDuration duration) {
        mDuration = duration;
        return this;
    }


    public Snackbar duration(long duration) {
        mCustomDuration = duration;
        return this;
    }
    private FrameLayout.LayoutParams initOnResume(Activity parent, int bottomMargin){

            SnackbarLayout layout = (SnackbarLayout) LayoutInflater.from(parent)
                    .inflate(R.layout.snackbar, this, true);

            float scale = getResources().getDisplayMetrics().density;

            FrameLayout.LayoutParams params;
            if (getResources().getConfiguration().smallestScreenWidthDp < 600) {
                // Phone
                //layout.setMinimumHeight(dpToPx(PHONE_MIN_HEIGHT_DP, scale));
                //layout.setMaxHeight(dpToPx(mType.getHeight(), scale));
//                layout.setBackgroundResource(R.color.blue_color);
                layout.setBackgroundResource(R.drawable.snackbar);
                params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            } else {
                // Tablet/desktop
//                layout.setMinimumWidth(dpToPx(TABLET_MIN_WIDTH_DP, scale));
//                layout.setMaxWidth(dpToPx(TABLET_MAX_WIDTH_DP, scale));
//                layout.setBackgroundResource(R.color.blue_color);
                layout.setBackgroundResource(R.drawable.snackbar);
                GradientDrawable bg = (GradientDrawable) layout.getBackground();
                bg.setColor(mColor);

//                params = new FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.MATCH_PARENT, dpToPx(TABLET_HEIGHT_DP, scale));
//                int margin = dpToPx(TABLET_MARGIN_DP, scale);
//                params.leftMargin = margin;

                params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            }
            params.gravity = Gravity.BOTTOM;
            params.bottomMargin = bottomMargin;
            /*Resources resources = parent.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height_landscape", "dimen", "android");
            if (resourceId > 0) {
                Utility.debugger(" Resouce ID >>>>>>>>>>>>>>> "+resourceId);
                params.bottomMargin = resources.getDimensionPixelSize(resourceId)/2;
            }*/

            TextView snackbarText = layout.findViewById(R.id.snackbar_text);
            snackbarText.setText(mText);
            snackbarText.setTextColor(mTextColor);
            snackbarText.setMaxLines(mType.getMaxLines());

            TextView snackbarAction = layout.findViewById(R.id.snackbar_action);
            if (!TextUtils.isEmpty(mActionLabel)) {
                snackbarAction.setText(mActionLabel);
                snackbarAction.setTextColor(mActionColor);
                snackbarAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mActionClickListener != null) {
                            mActionClickListener.onActionClicked();
                        }
                        if (mShouldDismiss) {
                            dismiss();
                        }
                    }
                });
                snackbarAction.setMaxLines(mType.getMaxLines());
            } else {
                snackbarAction.setVisibility(GONE);
            }

            setClickable(true);

            if (mCanSwipeToDismiss) {
                setOnTouchListener(new SwipeDismissTouchListener(this, null,
                        new SwipeDismissTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(Object token) {
                                return true;
                            }

                            @Override
                            public void onDismiss(View view, Object token) {
                                if (view != null) {
                                    dismiss();
                                }
                            }
                        }));
            }

            return params;

        }
    private FrameLayout.LayoutParams init(Activity parent) {
        SnackbarLayout layout = (SnackbarLayout) LayoutInflater.from(parent)
                .inflate(R.layout.snackbar, this, true);

        float scale = getResources().getDisplayMetrics().density;

        FrameLayout.LayoutParams params;
        if (getResources().getConfiguration().smallestScreenWidthDp < 600) {
            // Phone
            //layout.setMinimumHeight(dpToPx(PHONE_MIN_HEIGHT_DP, scale));
            //layout.setMaxHeight(dpToPx(mType.getHeight(), scale));
//        	 layout.setBackgroundResource(R.color.blue_color);
            layout.setBackgroundResource(R.drawable.snackbar);
            params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        } else {
            // Tablet/desktop
            layout.setMinimumWidth(dpToPx(TABLET_MIN_WIDTH_DP, scale));
            layout.setMaxWidth(dpToPx(TABLET_MAX_WIDTH_DP, scale));
//            layout.setBackgroundResource(R.color.blue_color);
            layout.setBackgroundResource(R.drawable.snackbar);
            GradientDrawable bg = (GradientDrawable) layout.getBackground();
            bg.setColor(mColor);

//            params = new FrameLayout.LayoutParams(
//                    FrameLayout.LayoutParams.MATCH_PARENT, dpToPx(TABLET_HEIGHT_DP, scale));
//            int margin = dpToPx(TABLET_MARGIN_DP, scale);
//            params.leftMargin = margin;
//            params.bottomMargin = margin;
            params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        }
        params.gravity = Gravity.BOTTOM;


        TextView snackbarText = layout.findViewById(R.id.snackbar_text);
        snackbarText.setText(mText);
        snackbarText.setTextColor(mTextColor);
        snackbarText.setMaxLines(mType.getMaxLines());

        TextView snackbarAction = layout.findViewById(R.id.snackbar_action);
        if (!TextUtils.isEmpty(mActionLabel)) {
            snackbarAction.setText(mActionLabel);
            snackbarAction.setTextColor(mActionColor);
            snackbarAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionClickListener != null) {
                        mActionClickListener.onActionClicked();
                    }
                    if (mShouldDismiss) {
                        dismiss();
                    }
                }
            });
            snackbarAction.setMaxLines(mType.getMaxLines());
        } else {
            snackbarAction.setVisibility(GONE);
        }

        setClickable(true);

        if (mCanSwipeToDismiss) {
            setOnTouchListener(new SwipeDismissTouchListener(this, null,
                    new SwipeDismissTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(Object token) {
                            return true;
                        }

                        @Override
                        public void onDismiss(View view, Object token) {
                            if (view != null) {
                                dismiss();
                            }
                        }
                    }));
        }

        return params;
    }

    private static int dpToPx(int dp, float scale) {
        return (int) (dp * scale + 0.5f);
    }
    public void showOnResume(Activity targetActivity, int margin){
        FrameLayout.LayoutParams params = initOnResume(targetActivity, margin);
        ViewGroup root = targetActivity.findViewById(android.R.id.content);
        root.addView(this, params);

        mIsShowing = true;

        if (mEventListener != null) {
            mEventListener.onShow(mType.getHeight());
        }

        if (mAnimated) {
            startSnackbarAnimation();
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, getDuration());
        }
    }



    public void show(Activity targetActivity) {
        FrameLayout.LayoutParams params = init(targetActivity);
        ViewGroup root = targetActivity.findViewById(android.R.id.content);
        root.addView(this, params);

        mIsShowing = true;

        if (mEventListener != null) {
            mEventListener.onShow(mType.getHeight());
        }

        if (mAnimated) {
            startSnackbarAnimation();
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, getDuration());
        }
    }

    private void startSnackbarAnimation() {
        final Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.snackbar_out);
        fadeOut.setStartOffset(getDuration());
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Animation slideIn = AnimationUtils.loadAnimation(getContext(), R.anim.snackbar_in);
        slideIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        startAnimation(fadeOut);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(slideIn);
    }

    public void dismiss() {
        clearAnimation();
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
        if (mEventListener != null && mIsShowing) {
            mEventListener.onDismiss(mType.getHeight());
        }
        mIsShowing = false;
    }

    public int getActionColor() {
        return mActionColor;
    }

    public CharSequence getActionLabel() {
        return mActionLabel;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getColor() {
        return mColor;
    }

    public CharSequence getText() {
        return mText;
    }

    public long getDuration() {
        return mCustomDuration == -1 ? mDuration.getDuration() : mCustomDuration;
    }

    public SnackbarType getType() {
        return mType;
    }

    public boolean isAnimated() {
        return mAnimated;
    }

    public boolean shouldDismissOnActionClicked() {
        return mShouldDismiss;
    }


    public boolean isShowing() {
        return mIsShowing;
    }


    public boolean isDismissed() {
        return !mIsShowing;
    }

    public interface ActionClickListener {
        void onActionClicked();
    }


    public interface EventListener {

        void onShow(int height);


        void onDismiss(int height);
    }
}
