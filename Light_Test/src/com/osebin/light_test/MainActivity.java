package com.osebin.light_test;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener
{
	SensorManager mSensorManager;
	Sensor mLight;
	LinearLayout Lightview;
	LightView view;
	
	int mCount = 0;
	TextView mCheck;
	TextView mDisplay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		mCheck = (TextView)findViewById(R.id.check);
		mDisplay = (TextView)findViewById(R.id.display);
		Lightview = (LinearLayout)findViewById(R.id.light_view);
		
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
		view = new LightView(this);
		Lightview.addView(view);
	}

	protected void onResume() {
		super.onResume();
		
		mCount = 0;
//		mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(view, mLight, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(view);
//		mSensorManager.unregisterListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE){
		}
		
		if(event.sensor.getType() == Sensor.TYPE_LIGHT){
			mCount++;
			String str = "";
			str = "Sensor Check Count : " + mCount;
			
			mCheck.setText(str);
			
			str = "Sensor Value : " + event.values[0]/2 + "lux";
			mDisplay.setText(str);			
		}
	}

	class LightView extends View implements SensorEventListener{
		
		int width;
		int height;
		int h10;
		
		ArrayList<Float> arValue = new ArrayList<Float>();
		boolean Stop = false;

		public LightView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public void onSizeChanged(int w, int h, int oldw, int oldh){
			super.onSizeChanged(w, h, oldw, oldh);
			
			width = w;
			height = h;
			h10 = height;
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if(event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE){
			}
			if(event.sensor.getType() == Sensor.TYPE_LIGHT){
				if(arValue.size() > width){
					arValue.clear();
				}
				arValue.add(event.values[0]);
				invalidate();
			}
		}
		
		public void onDraw(Canvas canvas){
			Paint Pnt = new Paint();
			Pnt.setColor(Color.RED);
			Pnt.setTextSize(30);
			
			Paint linePaint = new Paint();
			linePaint.setColor(Color.GREEN);
			linePaint.setStrokeWidth(1);
			
			Paint pntText = new Paint();
			pntText.setColor(Color.BLACK);
			pntText.setTextSize(30);
			
			int i;
			int x, y;
			int oldx, oldy;
			int basey;
			
			basey = oldy = h10;
			oldx = 0;
			
			canvas.drawLine(0, basey, width, basey, linePaint);
			canvas.drawText(" Lux Value : 0", 0, basey, pntText);
			
			canvas.drawLine(0, basey - 100, width, basey - 100, linePaint);
			canvas.drawText(" Lux Value : 100", 0, basey - 100, pntText);
			
			canvas.drawLine(0, basey - 200, width, basey - 200, linePaint);
			canvas.drawText(" Lux Value : 200", 0, basey - 200, pntText);
			
			canvas.drawLine(0, basey - 300, width, basey - 300, linePaint);
			canvas.drawText(" Lux Value : 300", 0, basey - 300, pntText);
			
			for(i = 0; i < arValue.size(); i++){
				y = (int)(basey - (arValue.get(i)/2));
				x = i;
				
				canvas.drawLine(oldx, oldy, x, y, Pnt);
				oldx = x;
				oldy = y;
			}
		}
		
	}
}
