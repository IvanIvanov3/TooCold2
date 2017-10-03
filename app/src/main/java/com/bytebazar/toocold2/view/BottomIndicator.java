package com.bytebazar.toocold2.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bytebazar.toocold2.R;

public final class BottomIndicator extends View {

    private final Paint circlePaint;
    private final Paint cursorPaint;

    private final Path cursorPath;

    private final RectF circleRect;
    private final float[] points;

    public BottomIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomIndicator, defStyleAttr, 0);
//        final int backgroundColor = ta.getColor(R.styleable.BottomIndicator_circleColor, Color.parseColor("#560641"));
//        setBackgroundColor(backgroundColor);
        final int circleColor = ta.getColor(R.styleable.BottomIndicator_circleColor, Color.parseColor("#5E7281"));
        final int cursorColor = ta.getColor(R.styleable.BottomIndicator_indicatorColor, Color.parseColor("#EEF5F5"));
        ta.recycle();

        circlePaint = new Paint();
        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

        circleRect = new RectF();

        cursorPaint = new Paint();
        cursorPaint.setColor(cursorColor);
        cursorPaint.setAntiAlias(true);
        cursorPaint.setStyle(Paint.Style.STROKE);
        cursorPaint.setStrokeWidth(2);

        cursorPath = new Path();

        points = new float[8];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(calculateValue(widthMeasureSpec), calculateValue(heightMeasureSpec));
    }

    private int calculateValue(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 300;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setPoints();
    }

    private void setPoints() {
        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;

        circleRect.set(centerX - 50, centerY - 50, centerX + 50, centerY + 50);

        cursorPath.moveTo(centerX - 25, centerY - 15);
        cursorPath.lineTo(centerX, centerY - 25);
        cursorPath.lineTo(centerX + 25, centerY - 15);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    private void drawIndicator(Canvas canvas) {
        canvas.save();

        canvas.drawArc(circleRect, 180, 180, true, circlePaint);
        canvas.drawOval(circleRect, circlePaint);

        canvas.drawPath(cursorPath, cursorPaint);
        canvas.translate(0, 35);
        canvas.drawPath(cursorPath, cursorPaint);
    }
}
