package net.bohush.geometricprogressview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class GeometricProgressView extends View {

    private static final int DEFAULT_NUMBER_OF_ANGLES = 6;
    private static final int DEFAULT_SIZE = 64;
    private static final int DEFAULT_DURATION = 1500;
    private static final int DEFAULT_FIGURE_PADDING = 2;
    private static final String DEFAULT_COLOR = "#00897b";
    private static final TYPE DEFAULT_TYPE = TYPE.KITE;

    private int color;
    private int width, height;
    private int desiredWidth;
    private int desiredHeight;
    private int figurePadding;
    private int duration;
    private PointF center;
    private int numberOfAngles;
    private List<Figure> figures;
    private List<ValueAnimator> animators;
    private TYPE type;

    public GeometricProgressView(Context context) {
        this(context, null);
    }

    public GeometricProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeometricProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        desiredWidth = dpToPx(DEFAULT_SIZE);
        desiredHeight = dpToPx(DEFAULT_SIZE);
        center = new PointF(0, 0);
        if (attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GeometricProgressView);
            figurePadding = array.getDimensionPixelSize(R.styleable.GeometricProgressView_gp_figure_padding, DEFAULT_FIGURE_PADDING);
            numberOfAngles = array.getInteger(R.styleable.GeometricProgressView_gp_number_of_angles, DEFAULT_NUMBER_OF_ANGLES);
            setColor(array.getColor(R.styleable.GeometricProgressView_gp_color, Color.parseColor(DEFAULT_COLOR)));
            duration = array.getInteger(R.styleable.GeometricProgressView_gp_duration, DEFAULT_DURATION);
            int typeInt = array.getInt(R.styleable.GeometricProgressView_gp_type, 0);
            switch (typeInt) {
                case 0:
                    type = TYPE.KITE;
                    break;
                case 1:
                    type = TYPE.TRIANGLE;
                    break;
            }
            array.recycle();
        } else {
            figurePadding = dpToPx(DEFAULT_FIGURE_PADDING);
            numberOfAngles = DEFAULT_NUMBER_OF_ANGLES;
            setColor(Color.parseColor(DEFAULT_COLOR));
            duration = DEFAULT_DURATION;
            type = DEFAULT_TYPE;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int measuredWidth = resolveSize(desiredWidth, widthMeasureSpec);
        final int measuredHeight = resolveSize(desiredHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.width = getWidth();
        this.height = getHeight();
        this.center.set(width / 2.0f, height / 2.0f);
        initializeFigures();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Figure figure : figures) {
            figure.draw(canvas);
        }
    }

    public void setNumberOfAngles(int numberOfAngles) {
        this.numberOfAngles = numberOfAngles;
        initializeFigures();
    }

    public void setType(TYPE type) {
        this.type = type;
        initializeFigures();
    }

    public void setFigurePadding(int figurePadding) {
        this.figurePadding = figurePadding;
        initializeFigures();
    }

    public void setFigurePaddingInDp(int figurePadding) {
        setFigurePadding(dpToPx(figurePadding));
    }

    public void setColor(int color) {
        this.color = color;
        if (figures != null) {
            for (Figure figure : figures) {
                figure.setColor(color);
            }
        }
        invalidate();
    }

    public void setDuration(int duration) {
        this.duration = duration;
        if (figures != null) {
            setupAnimation();
        }
    }

    private void initializeFigures() {
        if (!isInEditMode()) cancelAnimation();
        int size = Math.min(width, height);

        double circumference = numberOfAngles * figurePadding;
        double distanceFromCenter = circumference / (Math.PI * 2);
        int radius = size / 2 - (int) (distanceFromCenter);
        double startAngle = 90 + (360.0 / numberOfAngles) / 2;
        List<PointF> angles = new ArrayList<>();
        for (int i = 0; i < numberOfAngles; i++) {
            double angle = startAngle + i * (360.0 / numberOfAngles);
            angles.add(new PointF(
                    (float) (center.x + radius * Math.cos(Math.toRadians(angle))),
                    (float) (center.y + radius * Math.sin(Math.toRadians(angle))))
            );
        }

        figures = new ArrayList<>();
        if (TYPE.KITE.equals(type)) {
            buildFiguresUsingKites(angles, startAngle, distanceFromCenter);
        } else {
            buildFiguresUsingTriangles(angles, startAngle, distanceFromCenter);
        }
        setupAnimation();
    }

    private void buildFiguresUsingKites(List<PointF> angles, double startAngle, double distanceFromCenter) {
        for (int i = 0; i < angles.size(); i++) {

            double angle = startAngle + i * (360.0 / numberOfAngles);
            float newCenterX = (float) (distanceFromCenter * Math.cos(Math.toRadians(angle)));
            float newCenterY = (float) (distanceFromCenter * Math.sin(Math.toRadians(angle)));


            Path path = new Path();

            path.moveTo(newCenterX + center.x, newCenterY + center.y);

            PointF point2;
            if (i <= 0) {
                point2 = getPointBetweenPoints(angles.get(i), angles.get(angles.size() - 1));
            } else {
                point2 = getPointBetweenPoints(angles.get(i), angles.get(i - 1));
            }
            path.lineTo(newCenterX + point2.x, newCenterY + point2.y);

            path.lineTo(newCenterX + angles.get(i).x, newCenterY + angles.get(i).y);

            PointF point3;
            if (i >= (angles.size() - 1)) {
                point3 = getPointBetweenPoints(angles.get(i), angles.get(0));
            } else {
                point3 = getPointBetweenPoints(angles.get(i), angles.get(i + 1));

            }
            path.lineTo(newCenterX + point3.x, newCenterY + point3.y);
            path.lineTo(newCenterX + center.x, newCenterY + center.y);
            path.close();

            figures.add(new Figure(path, color, isInEditMode() ? (int) (25.0 + i * (230.0 / numberOfAngles)) : 0));
        }
    }

    private void buildFiguresUsingTriangles(List<PointF> angles, double startAngle, double distanceFromCenter) {
        for (int i = 0; i < angles.size(); i++) {

            double angle1 = startAngle + i * (360.0 / numberOfAngles);
            double angle2 = startAngle + (i + 1) * (360.0 / numberOfAngles);
            double angle = (angle1 + angle2) / 2;
            float newCenterX = (float) (distanceFromCenter * Math.cos(Math.toRadians(angle)));
            float newCenterY = (float) (distanceFromCenter * Math.sin(Math.toRadians(angle)));

            Path path = new Path();
            path.moveTo(newCenterX + center.x, newCenterY + center.y);
            path.lineTo(newCenterX + angles.get(i).x, newCenterY + angles.get(i).y);
            if (i >= (angles.size() - 1)) {
                path.lineTo(newCenterX + angles.get(0).x, newCenterY + angles.get(0).y);
            } else {
                path.lineTo(newCenterX + angles.get(i + 1).x, newCenterY + angles.get(i + 1).y);
            }
            path.lineTo(newCenterX + center.x, newCenterY + center.y);
            path.close();

            figures.add(new Figure(path, color, isInEditMode() ? (int) (25.0 + i * (230.0 / numberOfAngles)) : 0));
        }
    }

    private PointF getPointBetweenPoints(PointF point1, PointF point2) {
        float x = (point1.x + point2.x) / 2;
        float y = (point1.y + point2.y) / 2;
        return new PointF(x, y);
    }

    private void setupAnimation() {
        if (isInEditMode()) return;
        cancelAnimation();
        animators = new ArrayList<>();
        for (int i = 0; i < figures.size(); i++) {
            final Figure figure = figures.get(i);
            if (i != 0) {
                ValueAnimator startingFadeAnimator = ValueAnimator.ofInt((int) (i * (255.0 / numberOfAngles)), 0);
                startingFadeAnimator.setRepeatCount(1);
                startingFadeAnimator.setDuration((int) (i * (((double) duration) / numberOfAngles)));
                startingFadeAnimator.setInterpolator(new LinearInterpolator());
                startingFadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        figure.setAlpha((int) animation.getAnimatedValue());
                        invalidate();
                    }
                });
                startingFadeAnimator.start();
                animators.add(startingFadeAnimator);
            }

            ValueAnimator fadeAnimator = ValueAnimator.ofInt(255, 0);
            fadeAnimator.setRepeatCount(ValueAnimator.INFINITE);
            fadeAnimator.setDuration(duration);
            fadeAnimator.setInterpolator(new LinearInterpolator());
            fadeAnimator.setStartDelay((int) (i * (((double) duration) / numberOfAngles)));
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    figure.setAlpha((int) animation.getAnimatedValue());
                    invalidate();
                }
            });
            fadeAnimator.start();

            animators.add(fadeAnimator);
        }
    }

    private void cancelAnimation() {
        if (isInEditMode()) return;
        if (animators != null) {
            for (ValueAnimator valueAnimator : animators) {
                valueAnimator.cancel();
            }
            animators.clear();
            animators = null;
        }
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }
}
