package com.qualcomm.qnector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageButton;

public abstract class CircuitPart {
	public static final String RESISTOR = "Resistor";
	public static final String CAPACITOR = "Capacitor";
	public static final String BATTERY = "Battery";
	private Context context;
	private String partType;
	private ImageButton mImage;
	private int id;
	
	public CircuitPart(String partType, int id, Context context, float x, float y) {
		this.context = context;
		this.partType = partType;
		mImage = new ImageButton(context);
		mImage.setImageDrawable(context.getResources().getDrawable(id));
		mImage.setX(x);
		mImage.setY(y);
		this.id = id;
	}

	public String getPartType() {
		return partType;
	}

	public Bitmap getBitmap() {
		return BitmapFactory.decodeResource(context.getResources(), id);
	}
	
	public ImageButton getImage() {
		return mImage;
	}

	public boolean isSelected(int x, int y) {
		float imgX = mImage.getX();
		float imgY = mImage.getY();
		int width = mImage.getWidth();
		int height = mImage.getHeight();
		// return ( (x >= (imgX - halfWidth + 10)
		// && x <= (imgX + halfWidth - 10))
		// && (y >= (imgY - halfHeight + 10)
		// && y <= (imgY + halfHeight - 10)) );
		// TODO Handle stack of images
		return x >= imgX && x <= imgX + width && y >= imgY
				&& y <= imgY + height;
	}

}

class Resistor extends CircuitPart {
	public Resistor(Context context) {
		super(RESISTOR, R.drawable.resistor, context, 1100, 700);
		Log.d("CircuitPart:", "Resistor instantiated");
	}
}

class Capacitor extends CircuitPart {
	public Capacitor(Context context) {
		super(CAPACITOR, R.drawable.capacitor, context, 850, 700);
		Log.d("CircuitPart:", "Capacitor instantiated");
	}
}

class Battery extends CircuitPart {
	public Battery(Context context) {
		super(BATTERY, R.drawable.battery, context, 600, 700);
		Log.d("CircuitPart:", "Battery instantiated");
	}
}