package com.bt.code.util;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.PatternMatcher;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.code.util.camera.CameraManager;
import com.bt.code.util.camera.FlashlightManager;
import com.bt.code.util.decoding.CaptureActivityHandler;
import com.bt.code.util.decoding.InactivityTimer;
import com.bt.code.util.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.bt.R;

public class CaptureActivity extends Activity implements Callback{

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private Button bt_back;
	//开启闪关灯
	private CheckBox cbox_light;
	
	 private boolean orient=true;
	 private Handler handlerLine=new Handler();
	  private static int x;
	  private static int y;
	  private static int z;
	  private SensorManager sensorManager;
	  private final SensorEventListener sensorListener= new SensorEventListener(){

		  @Override
		  public void onSensorChanged(SensorEvent e) {
			  x = (int)e.values[SensorManager.DATA_X]; 
			  y = (int)e.values[SensorManager.DATA_Y]; 
			  z = (int)e.values[SensorManager.DATA_Z]; 
		  }

		  @Override
		  public void onAccuracyChanged(Sensor sensor, int accuracy) {

		  }
	  };
	
	/** Called when the activity is first created. */

//	private DeviceItemDetails deviceDetails;	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.scan);
		//初始化 CameraManager
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		txtResult = (TextView) findViewById(R.id.txtResult);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		
		bt_back = (Button)findViewById(R.id.bt_back);
		cbox_light= (CheckBox)findViewById(R.id.cbox_light);
		
		 sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		 Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //TYPE_ACCELEROMETER
		 sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);//SENSOR_DELAY_FASTEST
		 Timer updateTimer= new Timer("upadte");
		 updateTimer.scheduleAtFixedRate(new TimerTask(){

			 @Override
			 public void run() {
				 updateGUI();
			 }}, 0, 1000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
		bt_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		cbox_light.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					CameraManager.get().openLight();
				}else {
					CameraManager.get().closeLight();
				}
			}
		});
		 
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	//TODO
	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();
//		txtResult.setText(obj.getBarcodeFormat().toString() + ":" + obj.getText());
		
		String  dimensionNo  =  obj.getText().toString();
		 
		Intent data = new Intent();
		Bundle b = new Bundle();
		b.putString("dimensionNo", dimensionNo);
		b.putParcelable("bitmap", barcode);
		data.putExtra("bdl", b);
		setResult(RESULT_OK, data);
		finish();
	}
	
	
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	 private void updateGUI(){
	  	  handlerLine.post(new Runnable(){
	  		@Override
	  		public void run() {
	  			// TODO Auto-generated method stub
	  			if(x<-4||x>4){
	  				if(0<=y&y<6){
	  					if(orient){
	  						viewfinderView.changeLaser();
	  						orient=false;
	  					}
	  				}
	  			}else if(-4<=x&x<=4){
	  				if(6<=y&y<=9){
	  					if(!orient){
	  						viewfinderView.changeLaser();
	  						orient=true;
	  					}
	  				}
	  			}
	  		}});
	    }
	
	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}