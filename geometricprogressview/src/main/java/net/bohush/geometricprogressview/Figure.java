package net.bohush.geometricprogressview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

class Figure {

    private Path mPath;
    private Paint mPaint;

    Figure(Path path, int color, int alpha) {
        mPath = path;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        mPaint.setAlpha(alpha);
        mPaint.setStrokeWidth(0);
    }

    void setColor(int color) {
        mPaint.setColor(color);
    }

    void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }
}