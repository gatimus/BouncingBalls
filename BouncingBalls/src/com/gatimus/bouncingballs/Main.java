package com.gatimus.bouncingballs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.gatimus.bouncingballs.point.PointCartesian;
import com.gatimus.bouncingballs.point.PointCylindrical;

public class Main extends Activity {
	
	private static final String TAG = "Main:";
	private Resources res;
	private FragmentManager fragMan;
	private DialogFragment about;
	private DialogFragment help;
	private View view;
	//private AudioListening aLis;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Create");
		super.onCreate(savedInstanceState);
		view = new MyView(this);
		setContentView(new BoxView(this));
		res = getApplicationContext().getResources();
		fragMan = this.getFragmentManager();
		about = new About();
		help = new Help();
		//aLis = new AudioListening(this);
	} //onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "Create Options Menu");
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	} //onCreateOptionsMenu

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, item.getTitle().toString() + " Option Selected");
		int id = item.getItemId();
		switch(id){
		case R.id.action_settings :
			Intent intent = new Intent(Main.this, Settings.class);
            startActivity(intent);
			break;
		case R.id.action_about :
			about.show(fragMan, res.getString(R.string.action_about));
			break;
		case R.id.action_help :
			help.show(fragMan, res.getString(R.string.action_help));
			break;
		case R.id.action_quit : System.exit(0);
			break;
		default : 
			break;
		}
		return super.onOptionsItemSelected(item);
	} //onOptionsItemSelected
	
	public class MyView extends View {
		
		private static final int FRAME_TIME = 17;
		private Handler handler;
		private Runnable run;
		private PointF position;
		private PointF viewSize;
		private float radius;
		private int deltaY;
		private int deltaX;
		private int deltaZ;
		private Paint bg;
		private Paint color;
		private Paint shadow;
		
        public MyView(Context context) {
             super(context);
             handler = new Handler();
             radius = 75;
             deltaY = 5;
             deltaX = 5;
             deltaZ = 1;
             bg = new Paint();
             bg.setStyle(Paint.Style.FILL);
             bg.setColor(Color.WHITE);
             color = new Paint();
             color.setStyle(Paint.Style.FILL);
             color.setColor(Color.RED);
             shadow = new Paint();
             shadow.setStyle(Paint.Style.FILL);
             shadow.setColor(Color.LTGRAY);
             run = new Runnable(){
				@Override
				public void run() {
					if(radius >= 100 || radius <= 50){
						deltaZ = deltaZ * -1;
					}
					if(position.y + radius >= viewSize.y || position.y - radius <= 2){
						deltaY = deltaY * -1;
					}
					if(position.x + radius >= viewSize.x || position.x - radius <= 2){
						deltaX = deltaX * -1;
					}
					position.y = position.y + deltaY;
					position.x = position.x + deltaX;
					radius = radius + deltaZ;
					Log.v("Update:", "Position");
					invalidate();
				} //run()
             }; //run
        } //constructor

        @Override
        protected void onDraw(Canvas canvas) {
           super.onDraw(canvas);
           canvas.drawPaint(bg);
           canvas.drawCircle(position.x+radius-(radius/(float)Math.PI), position.y+radius-(radius/(float)Math.PI), 50-(radius-49), shadow);
           canvas.drawCircle(position.x, position.y, radius, color);
           setLayerType(View.LAYER_TYPE_SOFTWARE, null);
           canvas.drawVertices(Canvas.VertexMode.TRIANGLES, 6, new float[]{10,10,10,50,50,10}, 0, null, 0, new int[]{Color.RED,Color.RED,Color.RED,0xFF000000, 0xFF000000, 0xFF000000} , 0, null, 0, 0, new Paint());
           Log.v("Canvas:", "Draw");
           handler.postDelayed(run, FRAME_TIME);
       }
        
        @Override 
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        	viewSize = new PointF(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        	position = new PointF(viewSize.x/2, viewSize.y /2);
        	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        
    } //inner class
	



	
} //class
