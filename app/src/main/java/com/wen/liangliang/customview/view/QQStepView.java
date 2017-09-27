package com.wen.liangliang.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wen.liangliang.customview.R;

/**
 * Created by Admin on 2017/9/27.
 */

public class QQStepView extends View {

    private int mOuterColor = Color.BLUE;
    private int mInnerColor = Color.RED;
    private int mBorderWidth = 20;
    private int mStepTextSize = 22;
    private int mStepTextColor = Color.RED;
    private Paint mOuterPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;

    private int mStepMax;
    private int mCurrentStep;



    public QQStepView(Context context) {
        this(context,null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性集
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.QQStepView);
        //获取各个属性
        mOuterColor = typedArray.getColor(R.styleable.QQStepView_outerColor,mOuterColor);
        mInnerColor = typedArray.getColor(R.styleable.QQStepView_innerColor,mInnerColor);
        mBorderWidth = (int) typedArray.getDimension(R.styleable.QQStepView_borderWidth,mBorderWidth);
        mStepTextSize = typedArray.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize,mStepTextSize);
        mStepTextColor = typedArray.getColor(R.styleable.QQStepView_stepTextColor,mStepTextColor);
        typedArray.recycle();



        //设置画笔
        mOuterPaint = new Paint();
        //抗锯齿
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStyle(Paint.Style.STROKE);

        //设置画笔
        mInnerPaint = new Paint();
        //抗锯齿
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        //设置画笔
        mTextPaint = new Paint();
        //抗锯齿
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
    }

    //onMeasure

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量的宽高,宽高如果不一致,取最小值
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width > height? height : width,width > height? height : width);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //onDraw

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1 画外弧圆
        int center = getWidth()/2;
        int radius = getWidth()/2 - mBorderWidth/2;

        RectF rectF = new RectF(center-radius,center-radius,center+radius,center+radius);
        canvas.drawArc(rectF,135,270,false, mOuterPaint);
        //2 画内弧圆
        if(mStepMax == 0)
            return ;
        float sweepAngle = (float)mCurrentStep/mStepMax;
        canvas.drawArc(rectF,135,sweepAngle * 270,false, mInnerPaint);
        //3 画文字
        String stepText = mCurrentStep + "";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(stepText,0,stepText.length(),textBounds);
        int dx = getWidth()/2 - textBounds.width()/2;
        //基线
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy =  fontMetricsInt.bottom - (fontMetricsInt.bottom - fontMetricsInt.top)/2;
        int baseLine = getWidth()/2 + dy;
        canvas.drawText(stepText,dx,baseLine,mTextPaint);
    }

    public synchronized void setStepMax(int stepMax){
        this.mStepMax = stepMax;
    }

    public synchronized  void setCurrentStep(int currentStep){

        this.mCurrentStep = currentStep;
        //刚开始忘记调用这个方法,导致只绘制一次之后就不再绘制..
        invalidate();
    }
}
