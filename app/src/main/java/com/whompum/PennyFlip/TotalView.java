package com.whompum.PennyFlip;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;



/**
 * Created by bryan on 12/28/2017.
 */

public class TotalView extends View {

    public static final int TOTAL_PERCENTAGE = 360;
    public static final String TOTAL_VALUE = "100%";


    public static final float DEF_TEXT_SCALE = 0.5F;
    public static final float BASE_TEXT_SIZE = 1F;

    private int viewSize;



    protected final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected final TextPaint valueTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    protected final Paint valuePathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected RectF valuePathBounds = new RectF();
    protected Path valuePath = new Path();




    protected int value = 0; //Out of 100; Will convert to a percentage
    private float valueTextScale = DEF_TEXT_SCALE;
    private float valueTextSize = BASE_TEXT_SIZE;
    private Layout valueTextLayout;


    private Rect textMeasureBounds = new Rect();
    private Rect internalTextOffsetBounds = new Rect();


    public TotalView(final Context context) {
        super(context);
    }

    public TotalView(final Context context, final AttributeSet set) {
        super(context, set);
        initDelegate(context, set);
    }

    public TotalView(final Context context, final AttributeSet set, final int st) {
        super(context, set, st);
        initDelegate(context, set);
    }


    private void initDelegate(final Context c, final AttributeSet set) {

        final TypedArray array = c.obtainStyledAttributes(set, R.styleable.TotalView);

        for (int i = 0; i < array.getIndexCount(); i++) {

            final int index = array.getIndex(i);

            if (index == R.styleable.TotalView_totalViewSize)
                this.viewSize = array.getDimensionPixelSize(index, 50);

            if (index == R.styleable.TotalView_totalViewTextScale)
                setTextScale(array.getFloat(index, 0.5F));

            if (index == R.styleable.TotalView_totalViewValue)
                setValue(array.getInt(index, 0));


        }

        setValueTextColor(getColor(R.color.light_blue));
        setBackgroundViewColor(getColor(R.color.milk_white));
        setValuePathColor(getColor(R.color.color_light_orange));

        backgroundPaint.setStyle(Paint.Style.FILL);
        valueTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        valuePathPaint.setStyle(getValuePathPaintStyle());

        array.recycle();
    }


    protected final void setValueTextColor(final int color){
        valueTextPaint.setColor(color);
    }
    protected final void setBackgroundViewColor(final int color){
        backgroundPaint.setColor(color);
    }
    protected final void setValuePathColor(final int color){
        valuePathPaint.setColor(color);
    }

    protected Paint.Style getValuePathPaintStyle(){
        return Paint.Style.FILL_AND_STROKE;
    }

    /**
     *
     * @return degree value of the value
     */
    protected final int getValueSweepAngle() {
        final float valuePercentage = (value / 100F);
        return (int) (TOTAL_PERCENTAGE * valuePercentage);
    }

    protected int getColor(@ColorRes final int colorRes) {

        if (Build.VERSION.SDK_INT >= 23)
            return getColorForApi23(colorRes);
        else
            return getColorForUnder23(colorRes);

    }

    protected void generateValuePath(final int width, final int height){

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(viewSize, viewSize);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        fetchTextSize();
        makeLayout();
    }

    @CallSuper
    @Override
    protected void onDraw(Canvas canvas) {
        final int halfSize = getSize() / 2;

        generateValuePath(viewSize, viewSize);


        canvas.drawCircle(halfSize, halfSize, halfSize, backgroundPaint);
        canvas.drawPath(valuePath, valuePathPaint);


        final int translateCenter = canvas.save();
        canvas.translate(halfSize, halfSize);

        final int textLayoutWidth = valueTextLayout.getWidth();
        final int textLayoutHeight = valueTextLayout.getHeight();

        canvas.save();

        canvas.translate(-textLayoutWidth / 2, -textLayoutHeight / 2);
        valueTextLayout.draw(canvas);

        canvas.restoreToCount(translateCenter);

    }



    public int getValue() {
        return value;
    }
    public String getValueAsString() {
        return getValueAsText();
    }
    public void setValue(@IntRange(from = 0, to = 100) final int value) {
        this.value = value;
        makeLayout();
        invalidate();
    }
    public void setTextScale(@FloatRange(from = 0.0F, to = 1.0F) final float textScale) {
        this.valueTextScale = textScale;
        requestLayout();
    }
    protected int getSize() {
        return Math.max(getWidth(), getHeight());
    }
    private String getValueAsText() {
        return String.valueOf(value) + "%";
    }




    private void fetchTextSize() {

        internalTextOffsetBounds.set(0, 0, (int) (getSize() * valueTextScale), (int) (getSize() * valueTextScale));

        final Paint measurePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        float newTextSize = BASE_TEXT_SIZE;

        while (true) {

            measurePaint.setTextSize(newTextSize += BASE_TEXT_SIZE);
            measurePaint.getTextBounds(TOTAL_VALUE, 0, TOTAL_VALUE.length(), textMeasureBounds);

            if (textMeasureBounds.width() > internalTextOffsetBounds.width() |
                    textMeasureBounds.height() > internalTextOffsetBounds.height())
                break;

        }

        this.valueTextSize = measurePaint.getTextSize();
    }
    private void makeLayout() {

        valueTextPaint.setTextSize(valueTextSize);


        final String text = getValueAsString();
        final TextPaint paint = valueTextPaint;
        final int width = (int) Layout.getDesiredWidth(text, valueTextPaint);
        final Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
        final float spacingMult = 1F;
        final float spacingAdd = 0F;
        final boolean includePadding = false;


        this.valueTextLayout = new StaticLayout(text, paint, width, alignment, spacingMult, spacingAdd, includePadding);

    }



    @TargetApi(23)
    private int getColorForApi23(@ColorRes final int colorRes) {
        return getContext().getColor(colorRes);
    }
    private int getColorForUnder23(@ColorRes final int colorRes) {
        return getContext().getResources().getColor(colorRes);
    }

}



