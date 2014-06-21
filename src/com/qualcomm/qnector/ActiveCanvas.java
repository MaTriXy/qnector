package com.qualcomm.qnector;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageButton;
 
public class ActiveCanvas extends SurfaceView implements SurfaceHolder.Callback {
    private ArrayList<CircuitPart> circuitParts = new ArrayList<CircuitPart>();
    private Paint paint;
    int mTouched;
    public ActiveCanvas(Context context) {
        super(context);
        this.getHolder().addCallback(this);
        setFocusable(true);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        for(CircuitPart c: circuitParts) {
        	ImageButton img = c.getImage();
        	canvas.drawBitmap(img.getDrawingCache(), img.getX(), img.getY(), null);
        	//canvas.drawBitmap(img.getDrawingCache(), img.getX() - (img.getWidth() / 2), img.getY() - (img.getHeight() / 2), null);
        }
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {    
//    	
//    	if((int)event.getY() > 494)
//    		return true;
    	
        for(CircuitPart c: circuitParts){
        	if(c.isSelected((int) event.getX(), (int) event.getY())){
        		c.getImage().setX((int)event.getX());
        		c.getImage().setY((int)event.getY());
        		this.postInvalidate();
        		//mTouched = c.getId();
        		//TODO add unique ID to each button.
        		break;
        	}
        }
        
//        if(rTouched == -1){
//        	sscoord[0] = (int) event.getX();
//        	sscoord[1] = (int) event.getY();
//        }
//        if(lastPressed[0] != rTouched){
//        	lastPressed[1] = lastPressed[0]; //FROM
//        	lastPressed[0] = rTouched;		// TO
//        }

    	return true;
    }
 
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
 
    }
 
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
    }
 
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    
}