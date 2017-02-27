package net.bohush.geometricprogressview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private GeometricProgressView.TYPE type;

    public GeometricProgressView(Context context) {
        super(context);
        initialize();
    }

    public GeometricProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public GeometricProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        desiredWidth = dpToPx(DEFAULT_SIZE);
        desiredHeight = dpToPx(DEFAULT_SIZE);
        figurePadding = dpToPx(DEFAULT_FIGURE_PADDING);
        numberOfAngles = DEFAULT_NUMBER_OF_ANGLES;
        type = DEFAULT_TYPE;
        duration = DEFAULT_DURATION;
        center = new PointF(0, 0);
        setColor(Color.parseColor(DEFAULT_COLOR));
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
        setupAnimation();
    }

    private void initializeFigures() {
        cancelAnimation();
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
                    (float) (center.y + radius * Math.sin(Math.toRadians(angle)))));
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
            Path path = new Path();

            path.moveTo(center.x, center.y);

            PointF point2;
            if (i <= 0) {
                point2 = getPointBetweenPoints(angles.get(i), angles.get(angles.size() - 1));
            } else {
                point2 = getPointBetweenPoints(angles.get(i), angles.get(i - 1));
            }
            path.lineTo(point2.x, point2.y);

            path.lineTo(angles.get(i).x, angles.get(i).y);

            PointF point3;
            if (i >= (angles.size() - 1)) {
                point3 = getPointBetweenPoints(angles.get(i), angles.get(0));
            } else {
                point3 = getPointBetweenPoints(angles.get(i), angles.get(i + 1));

            }
            path.lineTo(point3.x, point3.y);
            path.lineTo(center.x, center.y);

            double angle = startAngle + i * (360.0 / numberOfAngles);
            float newCenterX = (float) (distanceFromCenter * Math.cos(Math.toRadians(angle)));
            float newCenterY = (float) (distanceFromCenter * Math.sin(Math.toRadians(angle)));

            path.offset(newCenterX, newCenterY);

            Figure figure = new Figure();
            figure.setColor(color);
            figure.setAlpha(0);
            figure.setPath(path);
            figures.add(figure);
        }
    }

    private void buildFiguresUsingTriangles(List<PointF> angles, double startAngle, double distanceFromCenter) {
        for (int i = 0; i < angles.size(); i++) {
            Path path = new Path();

            path.moveTo(center.x, center.y);
            path.lineTo(angles.get(i).x, angles.get(i).y);
            if (i >= (angles.size() - 1)) {
                path.lineTo(angles.get(0).x, angles.get(0).y);
            } else {
                path.lineTo(angles.get(i + 1).x, angles.get(i + 1).y);

            }
            path.lineTo(center.x, center.y);

            double angle1 = startAngle + i * (360.0 / numberOfAngles);
            double angle2 = startAngle + (i + 1) * (360.0 / numberOfAngles);
            double angle = (angle1 + angle2) / 2;


            float newCenterX = (float) (distanceFromCenter * Math.cos(Math.toRadians(angle)));
            float newCenterY = (float) (distanceFromCenter * Math.sin(Math.toRadians(angle)));

            path.offset(newCenterX, newCenterY);

            Figure figure = new Figure();
            figure.setColor(color);
            figure.setAlpha(0);
            figure.setPath(path);
            figures.add(figure);
        }
    }

    private PointF getPointBetweenPoints(PointF point1, PointF point2) {
        float x = (point1.x + point2.x) / 2;
        float y = (point1.y + point2.y) / 2;
        return new PointF(x, y);
    }

    private void setupAnimation() {
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

    private static class Figure {

        private Path path;
        private Paint paint;

        Figure() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(0);
        }

        void setColor(int color) {
            paint.setColor(color);
        }

        void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        void setPath(Path path) {
            this.path = path;
        }

        void draw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }
    }


    public enum TYPE {
        TRIANGLE, KITE
    }
}
