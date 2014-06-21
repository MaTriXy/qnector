package com.qualcomm.qnector;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageButton;

import com.qconnector.schematicparser.ParseSchematic;
import com.qconnector.schematicparser.Part;
 
public class ActiveCanvas extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "ActiveCanvas.java";
    private ArrayList<CircuitPart> circuitParts = new ArrayList<CircuitPart>();
    private Paint paint;
    int mTouched;
    private boolean pressed = false;
    public ActiveCanvas(Context context) {
        super(context);
        this.getHolder().addCallback(this);
        setFocusable(true);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        
    }
    
    public void update(){
    	circuitParts.clear();
    	pressed = false;
    	getCircuitsParts();
    	postInvalidate();
    }
 
    private void getCircuitsParts() {
    	ArrayList<Part> parts = ParseSchematic.getPartsList();
    	for(Part part : parts) {
    		if(part.getName().charAt(0) == 'R') {
    			circuitParts.add(new Resistor(this.getContext()));
    		} else if(part.getName().charAt(0) == 'B') {
    			circuitParts.add(new Battery(this.getContext()));
    		} else if(part.getName().charAt(0) == 'C') {
    			circuitParts.add(new Capacitor(this.getContext()));
    		}
    	}
    	//for(CircuitPart part : circuitParts) {
    		//part.getImage().getDrawingCache();
    	//	Log.d(TAG, "mImage == " + (part.getImage() == null));
    	//}
    }
    @Override
    protected void onDraw(Canvas canvas) {
    	if(circuitParts == null) {
    		return;
    	}
        for(CircuitPart c: circuitParts) {
        	ImageButton img = c.getImage();
        	Log.d(TAG, "canvas is not null == " + (canvas != null));
        	canvas.drawBitmap(c.getBitmap(), img.getX(), img.getY(), paint);
        	//canvas.drawBitmap(img.getDrawingCache(), img.getX() - (img.getWidth() / 2), img.getY() - (img.getHeight() / 2), null);
        	if(pressed){
        		paint.setColor(Color.GREEN);
        		paint.setStrokeWidth(10.0f);
        		if(c.getPartType().equals(CircuitPart.BATTERY)){
	        		if(c.getImage().getY() > 300){
		            	canvas.drawLine(392, 431, 397, 224, paint);
		            	canvas.drawLine(397, 224, 758, 224, paint);
		            	canvas.drawLine(943, 224, 943, 430, paint);
		            	canvas.drawLine(733, 430, 582, 430, paint);
		            	pressed = true;
	        		} else {
	                	canvas.drawLine(429,168, 780, 168, paint);
	                	canvas.drawLine(972,168,972,488, paint);
	                	canvas.drawLine(434,488,970,488, paint);
	                	canvas.drawLine(240,488,240,168, paint);
	                	pressed = true;
	        		}
        		}
	            	
        	}
        	
        	
        }
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {    
    	if(circuitParts == null)
    		return false;
        for(CircuitPart c: circuitParts){
        	if(c.isSelected((int) event.getX(), (int) event.getY())){
        		Log.d(TAG, "YUM");
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
    
    public int[] move(float xx, float yy){
    	int x = (int)xx; int y = (int)yy;
    	int start_x1 = 168; int start_y = 64;
    	int start_x2 = 372;
    	
    	x = x<168?30*((x-start_x1)/30+start_x1):30*((x-start_x2)/30+start_x2);
    	y = (y-start_y)/30+start_y;
    	return new int[] {x, y};
    }
    
    public void drawLines(){
    	pressed = true;
    	this.postInvalidate();
    }
    
}

