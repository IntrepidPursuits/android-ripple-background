package com.skyfishjy.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by fyu on 11/3/14.
 */

public class RippleBackground extends RelativeLayout {

    private static final int DEFAULT_RIPPLE_COUNT = 6;
    private static final int DEFAULT_DURATION_TIME = 3000;
    private static final float DEFAULT_SCALE = 6.0f;
    private static final int DEFAULT_FILL_TYPE = 0;

    private int rippleColor;
    private float rippleStrokeWidth;
    private float rippleRadius;
    private int rippleDurationTime;
    private int rippleAmount;
    int maxSizeImageView = 50;
    private int rippleDelay;
    private float rippleScale;
    private int rippleType;
    private Drawable rippleDrawable;
    private Paint paint;
    private boolean animationRunning = false;
    private boolean ovalView;
    int max = 2000, min = 0;
    private AnimatorSet animatorSet, animatorSetScale;
    private ArrayList<Animator> animatorList, animatorListScale;
    private LayoutParams rippleParams;
    private ArrayList<ImageView> rippleViewList = new ArrayList<>();

    public RippleBackground(Context context) {
        super(context);
    }

    public RippleBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RippleBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode())
            return;

        if (null == attrs) {
            throw new IllegalArgumentException("Attributes should be provided to this view,");
        }

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground);
        rippleColor = typedArray.getColor(R.styleable.RippleBackground_rb_color, getResources().getColor(R.color.rippelColor));
        rippleStrokeWidth = typedArray.getDimension(R.styleable.RippleBackground_rb_strokeWidth, getResources().getDimension(R.dimen.rippleStrokeWidth));
        rippleRadius = typedArray.getDimension(R.styleable.RippleBackground_rb_radius, getResources().getDimension(R.dimen.rippleRadius));
        rippleDurationTime = typedArray.getInt(R.styleable.RippleBackground_rb_duration, DEFAULT_DURATION_TIME);
        rippleAmount = typedArray.getInt(R.styleable.RippleBackground_rb_rippleAmount, DEFAULT_RIPPLE_COUNT);
        rippleScale = typedArray.getFloat(R.styleable.RippleBackground_rb_scale, DEFAULT_SCALE);
        rippleType = typedArray.getInt(R.styleable.RippleBackground_rb_type, DEFAULT_FILL_TYPE);
        rippleDrawable = typedArray.getDrawable(R.styleable.RippleBackground_rb_drawable);
        typedArray.recycle();

        rippleDelay = rippleDurationTime / rippleAmount;

        paint = new Paint();
        paint.setAntiAlias(true);
        if (rippleType == DEFAULT_FILL_TYPE) {
            rippleStrokeWidth = 0;
            paint.setStyle(Paint.Style.FILL);
        } else
            paint.setStyle(Paint.Style.STROKE);
        paint.setColor(rippleColor);

        rippleParams = new LayoutParams((int) (2 * (rippleRadius + rippleStrokeWidth)), (int) (2 * (rippleRadius + rippleStrokeWidth)));
        rippleParams.addRule(CENTER_IN_PARENT, TRUE);
        setupAnimations();

    }

    private void setupAnimations() {
        animatorSet = new AnimatorSet();
        animatorSetScale = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSetScale.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorList = new ArrayList<>();
        animatorListScale = new ArrayList<>();
        animatorSetScale.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                stopRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationRunning = false;
                        startRippleAnimation();
                    }
                }, 500);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        setupImageViews();
        animatorSet.playTogether(animatorList);
        animatorSetScale.playTogether(animatorListScale);
    }

    private void setupImageViews() {
        rippleViewList.clear();
        for (int i = 0; i < rippleAmount; i++) {
            final ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(rippleParams);
            imageView.setImageDrawable(rippleDrawable);
            imageView.setVisibility(GONE);
            imageView.setAlpha(0f);
            addView(imageView);
            rippleViewList.add(imageView);
            final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "ScaleX", 1.0f, rippleScale);
//            scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleXAnimator.setStartDelay(i * rippleDelay);
            scaleXAnimator.setDuration(rippleDurationTime);
            animatorListScale.add(scaleXAnimator);
            final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "ScaleY", 1.0f, rippleScale);
//            scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleYAnimator.setStartDelay(i * rippleDelay);
            scaleYAnimator.setDuration(rippleDurationTime);
            animatorListScale.add(scaleYAnimator);
            final ObjectAnimator alphaAnimatorStart = ObjectAnimator.ofFloat(imageView, "Alpha", 0f, 1.0f);
//            alphaAnimatorStart.setRepeatCount(ObjectAnimator.INFINITE);
//            alphaAnimatorStart.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimatorStart.setStartDelay(i * rippleDelay);
            alphaAnimatorStart.setDuration(rippleDurationTime);
            alphaAnimatorStart.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    removeView(imageView);
                    rippleViewList.remove(imageView);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorList.add(alphaAnimatorStart);
            final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(imageView, "Alpha", 1.0f, 0f);
//            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimator.setStartDelay(i * rippleDelay);
            alphaAnimator.setDuration(rippleDurationTime);
//            alphaAnimator.setInterpolator(0.8);

            animatorList.add(alphaAnimator);
        }
    }


    public void startRippleAnimation() {
        if (!isRippleAnimationRunning()) {
            setupAnimations();
            for (ImageView rippleView : rippleViewList) {
                rippleView.setVisibility(VISIBLE);
            }
            animatorSet.start();
            animatorSetScale.start();
            animationRunning = true;
        }
    }

    public void stopRippleAnimation() {
        if (isRippleAnimationRunning()) {
            animatorSet.end();
            animationRunning = false;
        }
    }

    public boolean isRippleAnimationRunning() {
        return animationRunning;
    }

    public float getRippleRadius() {
        return rippleRadius;
    }

    public void setRippleRadius(float rippleRadius) {
        this.rippleRadius = rippleRadius;
        invalidate();
    }

    public int getRippleAmount() {
        return rippleAmount;
    }

    public void setRippleAmount(int rippleAmount) {
        this.rippleAmount = rippleAmount;
        invalidate();
    }

    public int getRippleDurationTime() {
        return rippleDurationTime;
    }

    public void setRippleDurationTime(int rippleDurationTime) {
        this.rippleDurationTime = rippleDurationTime;
        invalidate();
    }

    public int getRippleDelay() {
        return rippleDelay;
    }

    public void setRippleDelay(int rippleDelay) {
        this.rippleDelay = rippleDelay;
        invalidate();
    }

    public float getRippleScale() {
        return rippleScale;
    }

    public void setRippleScale(float rippleScale) {
        this.rippleScale = rippleScale;
        invalidate();
    }

    public int getRippleType() {
        return rippleType;
    }

    public void setRippleType(int rippleType) {
        this.rippleType = rippleType;
        invalidate();
    }

    public Drawable getRippleDrawable() {
        return rippleDrawable;
    }

    public void setRippleDrawable(Drawable rippleDrawable) {
        this.rippleDrawable = rippleDrawable;
        invalidate();
    }

    public boolean isAnimationRunning() {
        return animationRunning;
    }

    public void setAnimationRunning(boolean animationRunning) {
        this.animationRunning = animationRunning;
        invalidate();
    }

    public int getRippleColor() {
        return rippleColor;
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        invalidate();
    }

    public int getMaxSizeImageView() {
        return maxSizeImageView;
    }

    public void setMaxSizeImageView(int maxSizeImageView) {
        this.maxSizeImageView = maxSizeImageView;
        invalidate();
    }
}
