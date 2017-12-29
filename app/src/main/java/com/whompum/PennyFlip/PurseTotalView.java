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

public class PurseTotalView extends View {

    private static final int TOTAL_PERCENTAGE = 360;

    private static final String TOTAL_VALUE = "100%";
    public static final String NA = "N/A";

    private static final int STROKE_FACTOR = 15;

    private static final float DEF_TEXT_SCALE = 0.5F;

    private static final float BASE_TEXT_SIZE = 1F;

    private int viewSize;
    private boolean NAEnabled = false;


    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private final Paint purseTotalStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint baseStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF strokeBounds = new RectF();
    private Path valueStrokePath = new Path();
    private Path baseStrokePath = new Path();

    private int strokeWidth = 0;



    private int value = 0; //Out of 100; Will convert to a percentage
    private final TextPaint valueTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private float valueTextScale = DEF_TEXT_SCALE;
    private float valueTextSize = BASE_TEXT_SIZE;
    private Layout valueTextLayout;




    private Rect textMeasureBounds = new Rect();
    private Rect internalTextOffsetBounds = new Rect();


    public PurseTotalView(final Context context) {
        super(context);
    }
    public PurseTotalView(final Context context, final AttributeSet set) {
        super(context, set);
        initDelegate(context, set);
    }
    public PurseTotalView(final Context context, final AttributeSet set, final int st){
        super(context, set, st);
        initDelegate(context, set);
    }


    private void initDelegate(final Context c, final AttributeSet set) {

        if (Build.VERSION.SDK_INT >= 23)
            initForAPI23();
        else
            initForPreMarshmallow();

        final TypedArray array = c.obtainStyledAttributes(set, R.styleable.PurseTotalView);

        for (int i = 0; i < array.getIndexCount(); i++) {

            final int index = array.getIndex(i);

            if (index == R.styleable.PurseTotalView_purseSize)
                this.viewSize = array.getDimensionPixelSize(index, 50);

            if(index ==  R.styleable.PurseTotalView_purseTextScale)
                setTextScale(array.getFloat(index, 0.5F));

            if(index == R.styleable.PurseTotalView_purseValue)
                setValue(array.getInt(index, 0));

            if(index == R.styleable.PurseTotalView_NAEnabled)
                setNAEnabled(array.getBoolean(index, false));
        }


        valueTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        purseTotalStrokePaint.setStyle(Paint.Style.STROKE);
        baseStrokePaint.setStyle(Paint.Style.STROKE);

        array.recycle();
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
    @Override
    protected void onDraw(Canvas canvas) {
        final int halfSize = getSize() / 2;

        generateStrokePaths(getWidth(), getHeight());

        final int rotateSave = canvas.save();

        canvas.rotate(270, halfSize, halfSize);

        canvas.drawCircle(halfSize, halfSize, halfSize, backgroundPaint);
        canvas.drawPath(baseStrokePath, baseStrokePaint);
        canvas.drawPath(valueStrokePath, purseTotalStrokePaint);

        canvas.restoreToCount(rotateSave);

        final int translateCenter = canvas.save();
        canvas.translate(halfSize, halfSize);

        final int textLayoutWidth = valueTextLayout.getWidth();
        final int textLayoutHeight = valueTextLayout.getHeight();

        final int translateForLayout = canvas.save();

        canvas.translate(-textLayoutWidth/2, -textLayoutHeight/2);
        valueTextLayout.draw(canvas);

        canvas.restoreToCount(translateCenter);

    }



    public int getValue(){
        return (NAEnabled) ? -1 : value;
    }
    public String getValueAsString(){
        return getValueAsText();
    }
    public void setValue(@IntRange(from = 0 , to = 100) final int value) {
        if(NAEnabled)
            return;

        this.value = value;


            makeLayout();
            invalidate();

    }
    public void setNAEnabled(final boolean enabled){
        this.NAEnabled = enabled;
        setValue(0);
    }
    public boolean getIsNAEnabled(){return NAEnabled;}
    public void setTextScale(@FloatRange(from = 0.0F , to = 1.0F) final float textScale){
        this.valueTextScale = textScale;

            requestLayout();
    }



    private int getSize(){
        return Math.max(getWidth(), getHeight());
    }
    private String getValueAsText(){
        return (NAEnabled) ? "N/A" :( String.valueOf(value) + "%");
    }



    private void fetchTextSize(){

        internalTextOffsetBounds.set(0,0, (int)(getSize()* valueTextScale), (int)(getSize()* valueTextScale) );

        final Paint measurePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        float newTextSize = BASE_TEXT_SIZE;

        while(true){

            measurePaint.setTextSize(newTextSize += BASE_TEXT_SIZE);
            measurePaint.getTextBounds(TOTAL_VALUE, 0, TOTAL_VALUE.length(), textMeasureBounds);

            if(textMeasureBounds.width() > internalTextOffsetBounds.width() |
               textMeasureBounds.height() > internalTextOffsetBounds.height())
               break;

        }

        this.valueTextSize = measurePaint.getTextSize();
    }
    private void makeLayout(){

        valueTextPaint.setTextSize(valueTextSize);


        final String text = getValueAsText();
        final TextPaint paint = valueTextPaint;
        final int width = (int)Layout.getDesiredWidth(text, valueTextPaint);
        final Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
        final float spacingMult = 1F;
        final float spacingAdd = 0F;
        final boolean includePadding = false;


        this.valueTextLayout = new StaticLayout(text, paint, width, alignment, spacingMult, spacingAdd, includePadding);

    }
    private void generateStrokePaths(final int width, final int height) {

        strokeWidth = getWidth() / STROKE_FACTOR;


        //Positions the outter edge of our stroke to the inner edge of the background circle
        final int strokeOffset = strokeWidth / 2;


        purseTotalStrokePaint.setStrokeWidth(strokeWidth);
        baseStrokePaint.setStrokeWidth(strokeWidth);


        strokeBounds.set(strokeOffset, strokeOffset, width-strokeOffset, height-strokeOffset);

        valueStrokePath.addArc(strokeBounds, 0, getValueSweepAngle());
        baseStrokePath.addArc(strokeBounds, 0, TOTAL_PERCENTAGE);
    }
    private int getValueSweepAngle(){
        final float valuePercentage = (value / 100F);
        return (int)(TOTAL_PERCENTAGE * valuePercentage);
    }





    @TargetApi(23)
    void initForAPI23() {
        valueTextPaint.setColor(getResources().getColor(R.color.light_red, null));
        purseTotalStrokePaint.setColor(getResources().getColor(R.color.light_red, null));
        baseStrokePaint.setColor(getResources().getColor(R.color.light_grey, null));
        backgroundPaint.setColor(getResources().getColor(R.color.milk_white, null));
    }
    void initForPreMarshmallow() {
        valueTextPaint.setColor(getResources().getColor(R.color.light_red));
        purseTotalStrokePaint.setColor(getResources().getColor(R.color.light_red));
        baseStrokePaint.setColor(getResources().getColor(R.color.light_grey));
        backgroundPaint.setColor(getResources().getColor(R.color.milk_white));
    }

}
