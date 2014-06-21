package com.qualcomm.qnector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
 
public class ActiveCanvas extends SurfaceView implements SurfaceHolder.Callback {
    //private ArrayList<Reactor> reactors = new ArrayList<Reactor>();
    private Paint paint;
    
    public ActiveCanvas(Context context) {
        super(context);
        this.getHolder().addCallback(this);
        setFocusable(true);
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
/*        for(Reactor r: reactors)
        	canvas.drawBitmap(r.getReactor(), r.getX() - (r.getWidth() / 2), r.getY() - (r.getHeight() / 2), null);
        for(int i = 0; i < lines.size()/2; i+=2){
        	canvas.drawLine(reactors.get(lines.get(i)).getX(), reactors.get(lines.get(i)).getY(), 
        			reactors.get(lines.get(i+1)).getX(), reactors.get(lines.get(i+1)).getY(), paint);
        }*/
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {    
    	if((int)event.getY() > 494)
    		return true;
    	
/*        for(Reactor r: reactors){
        	if(r.isSelected((int) event.getX(), (int) event.getY())){
        		r.setX((int)event.getX());
        		r.setY((int)event.getY());
        		this.postInvalidate();
        		rTouched = r.getId();
        		break;
        	}
        }
        if(rTouched == -1){
        	sscoord[0] = (int) event.getX();
        	sscoord[1] = (int) event.getY();
        }
        if(lastPressed[0] != rTouched){
        	lastPressed[1] = lastPressed[0]; //FROM
        	lastPressed[0] = rTouched;		// TO
        }
*/
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