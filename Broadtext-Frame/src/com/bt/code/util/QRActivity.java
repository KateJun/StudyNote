package com.bt.code.util;

import java.io.UnsupportedEncodingException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.bt.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class QRActivity extends Activity {

	//创建二维码
	private Button bt_cteateQR;
	//扫描二维码
	private Button bt_scanQR;
	//内容
	private EditText edt_content;
	//显示二维码图片
	private ImageView img;
	
	protected final int requestQRCode = 0x001;
	
	// 图片宽度的一般
	private static final int IMAGE_HALFWIDTH = 20;
	// 插入到二维码里面的图片对象
	private Bitmap mBitmap;
	// 需要插图图片的大小 这里设定为40*40
	int[] pixels = new int[2*IMAGE_HALFWIDTH * 2*IMAGE_HALFWIDTH];
	private String QRSTRING = "http://www.baidu.com";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		init();

		initListener();
		
		initQRBitmap();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			if (requestCode == requestQRCode) {
				//显示二维码内容  图片
				Bundle b = data.getBundleExtra("bdl");
				if (b != null) {
					img.setImageBitmap((Bitmap)b.getParcelable("bitmap"));
					edt_content.setText(b.getString("dimensionNo"));
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		LinearLayout layout = new LinearLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);
		params.gravity = Gravity.CENTER;
		layout.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.setOrientation(LinearLayout.VERTICAL);

		// 按钮
		bt_cteateQR = new Button(this);
		bt_scanQR = new Button(this);
		LayoutParams childParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		childParams.setMargins(0, 5, 0, 5);
		bt_cteateQR.setText("生成二维码");
		bt_scanQR.setText("扫描二维码");
		
		bt_cteateQR.setLayoutParams(childParams);
		bt_scanQR.setLayoutParams(childParams);

		// 用于生成二维码的内容
		edt_content = new EditText(this);
		edt_content.setLines(3);
		edt_content.setGravity(Gravity.TOP);
		edt_content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		edt_content.setText(QRSTRING);
		// 显示二维码
		img = new ImageView(this);
		img.setLayoutParams(childParams);
		img.setImageResource(R.drawable.ic_launcher);

		layout.addView(edt_content);
		layout.addView(bt_cteateQR);
		layout.addView(bt_scanQR);
		layout.addView(img);

		setContentView(layout);
	}
	/**
	 * 按钮监听
	 * 
	 */
	private void initListener(){
		bt_cteateQR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String s = TextUtils.isEmpty(edt_content.getText()) ? QRSTRING : edt_content.getText().toString().trim();
					img.setImageBitmap(cretaeBitmap(new String(s.getBytes(),"UTF-8")));//ISO-8859-1 汉字扫描出来为乱码
				} catch (WriterException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
		
		bt_scanQR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QRActivity.this,CaptureActivity.class);
				startActivityForResult(intent, requestQRCode);
			}
		});
	}
	
	/**
	 * 处理二维码中心图片，不能太大，否则扫描不出
	 */
	private void  initQRBitmap(){
		 // 构造需要插入的图片对象
		mBitmap = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.share_image2)).getBitmap();
		// 缩放图片
		Matrix m = new Matrix();
		float sx = (float) 2*IMAGE_HALFWIDTH / mBitmap.getWidth();
		float sy = (float) 2*IMAGE_HALFWIDTH / mBitmap.getHeight();
		m.setScale(sx, sy);
		// 重新构造一个40*40的图片
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
				mBitmap.getHeight(), m, false);

	}
	
	/**
	 * 生成二维码
	 * 
	 * @param 字符串
	 * @return Bitmap
	 * @throws WriterException
	 */
	public Bitmap cretaeBitmap(String str) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, 300, 300);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH
						&& y < halfH + IMAGE_HALFWIDTH) {
					pixels[y * width + x] = mBitmap.getPixel(x - halfW + IMAGE_HALFWIDTH, y
							- halfH + IMAGE_HALFWIDTH);
				} else {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					}
				}

			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return bitmap;
	}
}
 