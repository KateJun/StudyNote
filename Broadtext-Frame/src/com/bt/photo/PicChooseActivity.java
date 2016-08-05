package com.bt.photo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.bt.photo.util.DownLoadUtil;
import com.bt.utils.Data;
import com.bt.volley.util.VolleyTool;
import com.bt.R;

public class PicChooseActivity extends Activity implements OnClickListener{

	
	private Uri photoUri;
	private final int  TAKE_PHOTO = 0x100;
	private final int PICK_PHOTO = 0x200;
	private String picPath;
	private  ImageView img_photo ; 
	private TextView tv_img_path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_choose);
		findViewById(R.id.bt_camera).setOnClickListener(this);
		findViewById(R.id.bt_album).setOnClickListener(this);
		findViewById(R.id.bt_download).setOnClickListener(this);
		img_photo  = (ImageView)findViewById(R.id.img_photo) ;
		tv_img_path = (TextView)findViewById(R.id.tv_img_path);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_camera://相机
			takePhoto();
			break;
		case R.id.bt_album://相册
			pickPhoto();
			break;
		case R.id.bt_download://下载图片
			downloadImg();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			try {
				doPhoto(requestCode, data);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	/**
	 * 选择图片后，获取图片的路径
	 * @param requestCode
	 * @param data
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	//TODO
	private void doPhoto(int requestCode,Intent data) throws FileNotFoundException, IOException{
		if(requestCode == PICK_PHOTO ){ //从相册取图片，有些手机有异常情况，请注意
			if(data == null){
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if(photoUri == null ){
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			System.out.println("------------"+photoUri.getPath());
			String[] pojo = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};//图片路径，名称
			Cursor cursor = getContentResolver().query(photoUri, pojo, null, null,null); //  managedQuery 
			if(cursor != null ){
				int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
				cursor.moveToFirst();
				picPath = cursor.getString(columnIndex);
				System.out.println("pic path :"+picPath+ "\n\rname of pic :"+cursor.getString(cursor.getColumnIndexOrThrow(pojo[1])));
				cursor.close();
			}
			if(picPath !=null){
				Bitmap bm = BitmapFactory.decodeFile(picPath);
				img_photo.setImageBitmap(bm);
				tv_img_path.setText(picPath);
			}else{
				Toast.makeText(this, "选择文件不正确!", Toast.LENGTH_LONG).show();
				
			}
		}else if (requestCode==TAKE_PHOTO) {
			Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);//有的机型不适合
//			Bitmap	b = (Bitmap)data.getExtras().get("data");//获取的为缩略图，怎么破？
			if (b != null) {
				img_photo.setImageBitmap(b);
				tv_img_path.setText(photoUri.getPath());
				DownLoadUtil.setBitmapToFile(this, "", System.currentTimeMillis()+".jpg", b);
			}
		}
		
	}
	/**
	 * 图片下载
	 */
	private void downloadImg(){
		VolleyTool.getInstance(this);
		ImageLoader loader = VolleyTool.getInstance(this).getmImageLoader();
		ImageListener listener = ImageLoader.getImageListener(img_photo, R.drawable.ic_launcher, 0);
		loader.get(Data.imgUrl, listener,480,800);
	}
	/**
	 * 拍照获取图片
	 */
	//TODO
	private void takePhoto() {
		//执行拍照前，应该先判断SD卡是否存在
		if(DownLoadUtil.hasSDCard()) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
//			 intent.putExtra("crop", "true");
//			 intent.putExtra("aspectX", 1);// 裁剪框比例
//			 intent.putExtra("aspectY", 1);
//			 intent.putExtra("outputX", 180);// 输出图片大小
//			 intent.putExtra("outputY", 180);

			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
			 * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
//			ContentValues values = new ContentValues();  
//			photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
//			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0); 
			/**--------过滤第三方相机方法1---------*/
//			final Intent intent_camera = getPackageManager().getLaunchIntentForPackage("com.android.camera");
//			if (intent_camera != null) {
//				intent.setPackage("com.android.camera");
//			}
//			startActivityForResult(intent, TAKE_PHOTO);
			/**---------过滤第三方相机方法2--------------------*/
			PackageManager packageManager = getPackageManager();
			//PackageManager.COMPONENT_ENABLED_STATE_DEFAULT = 0
			List<ResolveInfo> listCam = packageManager.queryIntentActivities(intent, 0);
			for (ResolveInfo res : listCam) {
				ApplicationInfo ai = null;
				try {
					ai = packageManager.getApplicationInfo(res.activityInfo.packageName, 0);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				if (ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					intent.setPackage(res.activityInfo.packageName);
					startActivityForResult(intent, TAKE_PHOTO);
					break;
				}

			}
			/**-----------------------------*/
		}else{
			Toast.makeText(this,"内存卡不存在,无法使用相机",Toast.LENGTH_LONG).show();
		}
	}
	/***
	 * 从相册中取图片
	 */
	//TODO
	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
//		intent.putExtra("crop", "true");
//		intent.putExtra("aspectX", 1);// 裁剪框比例
//		intent.putExtra("aspectY", 1);
//		intent.putExtra("outputX", 180);// 输出图片大小
//		intent.putExtra("outputY", 180);
//		intent.putExtra("return-data", true);  
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, PICK_PHOTO);
	}
}
