package com.example.lenovo.managerdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Administrator on 2016/5/25.
 */
public class MyView extends View {
    private Paint paint;
    private int radius;
    private int angle;

    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.MyView, defStyleAttr ,0);
        radius=array.getDimensionPixelSize(R.styleable.MyView_radius, 100);
        angle=array.getInt(R.styleable.MyView_angle,50);
        array.recycle();
        paint=new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //Bitmap newBitmap=Bitmap.createBitmap(bitmap, 0, 0, getWidth(), getHeight());
        Rect rect=new Rect(0, 0, getWidth(), getHeight());
        paint.setColor(Color.BLUE);
        paint.setAlpha(30);
        canvas.drawCircle(getWidth()/2, getHeight()/4, radius, paint);
       // paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawBitmap(bitmap, getWidth()/2-50, getHeight()/4-50, paint);
    }


}
