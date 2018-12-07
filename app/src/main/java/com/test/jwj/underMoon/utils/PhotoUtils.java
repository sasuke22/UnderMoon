package com.test.jwj.underMoon.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.Material;
import com.test.jwj.underMoon.global.Result;
import com.test.jwj.underMoon.imageFactory.ImageFactoryActivity;
import com.test.jwj.underMoon.imageFactory.ImageFactoryFliter;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;


public class PhotoUtils {
	// 图片在files中的缓存路径
	private static final String IMAGE_PATH = ApplicationData.mApplication.getFilesDir() + File.separator + "Images/";
	// 图片在SD卡路径
	private static final String SD_IMAGE_PATH = Environment.getExternalStorageDirectory().toString() + File.separator + "images/";
	// video的缓存路径
	public static final String VIDEO_PATH = ApplicationData.mApplication.getFilesDir() + File.separator + "Videos/";
	// 相册的RequestCode
	public static final int INTENT_REQUEST_CODE_ALBUM = 0;
	// 照相的RequestCode
	public static final int INTENT_REQUEST_CODE_CAMERA = 1;
	// 裁剪照片的RequestCode
	public static final int INTENT_REQUEST_CODE_CROP = 2;
	// 滤镜图片的RequestCode
	public static final int INTENT_REQUEST_CODE_FLITER = 3;
	// 录像的RequestCode
	public static final int INTENT_REQUEST_CODE_VIDEO = 4;

	public static final String tempCrop = IMAGE_PATH + "cutcamera.jpg";

	public static final String tempVideo = IMAGE_PATH + "tempVideo.mp4";

	/**
	 * 通过手机相册获取图片
	 *
	 * @param activity
	 */
	public static void selectPhoto(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_ALBUM);
	}

	/**
	 * 获取本地所有的图片
	 *
	 * @return list
	 */
	public static List<Material> getAllLocalPhotos(Context context) {
		List<Material> list = new ArrayList<>();
		String[] projection = {
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.DISPLAY_NAME,
				MediaStore.Images.Media.SIZE
		};
		//全部图片
		String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
				+ MediaStore.Images.Media.MIME_TYPE + "=? or "
				+ MediaStore.Images.Media.MIME_TYPE + "=?";
		//指定格式
		String[] whereArgs = {"image/jpeg","image/png", "image/jpg"};
		//查询
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, where, whereArgs,
				MediaStore.Images.Media.DATE_MODIFIED + " desc ");
		if (cursor == null) {
			return list;
		}
		//遍历
		while (cursor.moveToNext()) {
			Material materialBean = new Material();
			//获取图片的名称
			materialBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
			long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)); // 大小

			//获取图片的生成日期
			byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

			String path = new String(data, 0, data.length - 1);
			File file = new File(path);

			if (size < 5 * 1024 * 1024 && size > 0) {//<5M
				long time = file.lastModified();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
				String t = format.format(time);
				materialBean.setTime(t);
				materialBean.setLogo(path);
				materialBean.setFilePath(path);
				materialBean.setFileSize(size);
				materialBean.setChecked(false);
				materialBean.setFileType(6);
				materialBean.setFileId(0);
				materialBean.setUploadedSize(0);
				materialBean.setTimeStamps(System.currentTimeMillis() + "");
				list.add(materialBean);
			}
		}
		cursor.close();
		return list;
	}

	/**
	 * 获取本地所有的视频
	 *
	 * @return list
	 */
	public static List<Material> getAllLocalVideos(Context context) {
		String[] projection = {
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DATA,
				MediaStore.Video.Media.DISPLAY_NAME,
				MediaStore.Video.Media.DURATION,
				MediaStore.Video.Media.SIZE
		};
		String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
				MediaStore.Video.Thumbnails.VIDEO_ID};
		//全部图片
		String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
				+ MediaStore.Video.Media.MIME_TYPE + "=? or "
				+ MediaStore.Video.Media.MIME_TYPE + "=? or "
				+ MediaStore.Video.Media.MIME_TYPE + "=? or "
				+ MediaStore.Video.Media.MIME_TYPE + "=? or "
				+ MediaStore.Video.Media.MIME_TYPE + "=? or "
				+ MediaStore.Video.Media.MIME_TYPE + "=? or "
				+ MediaStore.Video.Media.MIME_TYPE + "=? or "
				+ MediaStore.Video.Media.MIME_TYPE + "=?";
		String[] whereArgs = {"video/mp4", "video/3gp", "video/avi", "video/rmvb", "video/vob", "video/flv",
				"video/mkv", "video/mov", "video/mpg"};
		List<Material> list = new ArrayList<>();
		Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				projection, where, whereArgs, MediaStore.Video.Media.DATE_ADDED + " DESC ");
		if (cursor == null) {
			return list;
		}
		try {
			while (cursor.moveToNext()) {
				long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)); // 大小
				if (size < 600 * 1024 * 1024) {//<600M
					Material materialBean = new Material();
					String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // 路径
					long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)); // 时长

					int id = cursor.getInt(cursor
							.getColumnIndex(MediaStore.Video.Media._ID));
					Cursor thumbCursor = context.getContentResolver().query(
							MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
							thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
									+ "=" + id, null, null);
					if (thumbCursor.moveToNext()) {
						materialBean.setThumb(thumbCursor.getString(thumbCursor
								.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
					}

					materialBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
					materialBean.setLogo(path);
					materialBean.setFilePath(path);
					materialBean.setChecked(false);
					materialBean.setFileType(2);
					materialBean.setFileId(1);
					materialBean.setUploadedSize(0);
					materialBean.setTimeStamps(System.currentTimeMillis() + "");
					SimpleDateFormat format = new SimpleDateFormat("mm:ss",Locale.CHINA);
					format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
					String t = format.format(duration);
					materialBean.setDuration(duration);
					materialBean.setTime(t);
					materialBean.setFileSize(size);
					list.add(materialBean);
				}
			}
		} catch (Exception e) {
			Log.e("tag","exception " + e.getMessage());
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return list;
	}

	/**
	 * 通过手机照相获取图片
	 * 
	 * @param activity
	 * @return 照相后图片的路径
	 */
	public static String takePicture(Activity activity) {
		FileUtils.createDirFile(SD_IMAGE_PATH);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String path = SD_IMAGE_PATH + System.currentTimeMillis() + ".jpg";
		File file = FileUtils.createNewFile(path);
		if (file != null) {
			if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity,activity.getApplicationContext().getPackageName() +
				".provider",file));
			}else
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
		return path;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param context
	 * @param activity
	 * @param path
	 *            需要裁剪的图片路径
	 */
	public static void cropPhoto(Context context, Activity activity, String path) {
		Intent intent = new Intent(context, ImageFactoryActivity.class);
		if (path != null) {
			intent.putExtra("path", path);
			intent.putExtra(ImageFactoryActivity.TYPE,
					ImageFactoryActivity.CROP);
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CROP);
	}

	public static void CropCamera(Activity context,Uri imgUri){
		context.startActivityForResult(CutForPhoto(context,imgUri), INTENT_REQUEST_CODE_CROP);
	}

	/**
	 * 图片裁剪
	 * @param uri
	 * @return
	 */
	@NonNull
	private static Intent CutForPhoto(Context context,Uri uri) {
		try {
			//直接裁剪
			Intent intent = new Intent("com.android.camera.action.CROP");
			//设置裁剪之后的图片路径文件
			File cutfile = new File(tempCrop); //随便命名一个
			if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
				cutfile.delete();
			}
			File imgDir = new File(IMAGE_PATH);
			if (!imgDir.exists())
				FileUtils.createDirFile(IMAGE_PATH);
			cutfile.createNewFile();
			//初始化 uri
			Uri imageUri = uri; //返回来的 uri
//			Uri outputUri; //真实的 uri
			Log.e("tag","pakage " + context.getPackageName());
//			if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
//				outputUri = FileProvider.getUriForFile(context, context.getPackageName() +
//						".provider", cutfile);
//
//			}else
//				outputUri = Uri.fromFile(cutfile);
//			mCutUri = outputUri;
			// crop为true是设置在开启的intent中设置显示的view可以剪裁
			intent.putExtra("crop",true);
			// aspectX,aspectY 是宽高的比例，这里设置正方形
			intent.putExtra("aspectX",0.99);//TODO 为什么改成1就是裁圆形
			intent.putExtra("aspectY",1);
			//设置要裁剪的宽高
			intent.putExtra("outputX", SystemMethod.dip2px(context,400)); //200dp
			intent.putExtra("outputY", SystemMethod.dip2px(context,400));
			intent.putExtra("scale",true);
			//如果图片过大，会导致oom，通过onactivityresult返回，在intent中，但是返回的是缩略图，需要在outputUri中获取
			intent.putExtra("return-data",false);
//			if (imageUri != null) {
//				intent.setDataAndType(uri, "image/*");
//			}
//			if (outputUri != null) {
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
//			}
			intent.putExtra("noFaceDetection", true);
			//压缩图片
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			context.grantUriPermission(context.getApplicationContext().getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			//最后用intent.getStringExtra("output")来拿返回的uri
//			context.grantUriPermission(context.getApplicationContext().getPackageName(), outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			return intent;
		} catch (IOException e) {
			Log.e("tag","exception " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static Result startUCrop(Activity activity,File sourceFile,int requestCode) {
		File cutfile = new File(tempCrop); //随便命名一个
		if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
			cutfile.delete();
		}
		File imgDir = new File(IMAGE_PATH);
		if (!imgDir.exists())
			FileUtils.createDirFile(IMAGE_PATH);
		try {
			cutfile.createNewFile();
		} catch (IOException e) {
			return Result.FAILED;
		}
		Uri sourceUri;
		Uri outputUri;
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
			sourceUri = FileProvider.getUriForFile(activity,activity.getApplicationContext().getPackageName() +
					".provider",sourceFile);
			outputUri = FileProvider.getUriForFile(activity, activity.getPackageName() +
					".provider", cutfile);
		} else {
			sourceUri = Uri.fromFile(sourceFile);
			outputUri = Uri.fromFile(cutfile);
		}
		UCrop uCrop = UCrop.of(sourceUri,outputUri);
		UCrop.Options options = new UCrop.Options();
		//设置裁剪图片可操作的手势
		options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
		//是否隐藏底部容器，默认显示
		options.setHideBottomControls(true);
		//设置toolbar颜色
		options.setToolbarColor(ActivityCompat.getColor(activity, R.color.themeColor));
		//设置状态栏颜色
		options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.themeColor));
		//是否能调整裁剪框
		options.setFreeStyleCropEnabled(true);
		//UCrop配置
		uCrop.withOptions(options);
		//设置裁剪图片的宽高比，比如16：9
		uCrop.withAspectRatio(1, 1);
		//跳转裁剪页面
		uCrop.start(activity, requestCode);
		return Result.SUCCESS;
	}

	public static void takeVideo(Activity activity){
		Intent intent = new Intent();
		intent.setAction("android.media.action.VIDEO_CAPTURE");
		intent.addCategory("android.intent.category.DEFAULT");
		File file = new File(tempVideo);
		if(file.exists()){
			file.delete();
		}
		Uri uri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		activity.startActivityForResult(intent, 0);
	}

	/**
	 * 滤镜图片
	 * 
	 * @param context
	 * @param activity
	 * @param path
	 *            需要滤镜的图片路径
	 */
	public static void fliterPhoto(Context context, Activity activity,
			String path) {
		Intent intent = new Intent(context, ImageFactoryActivity.class);
		if (path != null) {
			intent.putExtra("path", path);
			intent.putExtra(ImageFactoryActivity.TYPE,
					ImageFactoryActivity.FLITER);
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_FLITER);
	}

	/**
	 * 删除图片缓存目录
	 */
	public static void deleteImageFile() {
		File dir = new File(IMAGE_PATH);
		if (dir.exists()) {
			FileUtils.delFolder(IMAGE_PATH);
		}
	}

	/**
	 * 从文件中获取图片
	 * 
	 * @param path
	 *            图片的路径
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String path) {
		return BitmapFactory.decodeFile(path);
	}

	/**
	 * 从Uri中获取图片
	 * 
	 * @param cr
	 *            ContentResolver对象
	 * @param uri
	 *            图片的Uri
	 * @return
	 */
	public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) {
		try {
			return BitmapFactory.decodeStream(cr.openInputStream(uri));
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	/**
	 * 根据宽度和长度进行缩放图片
	 * 
	 * @param path
	 *            图片的路径
	 * @param w
	 *            宽度
	 * @param h
	 *            长度
	 * @return
	 */
	public static Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取图片的长度和宽度
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static Bundle getBitmapWidthAndHeight(Bitmap bitmap) {
		Bundle bundle = null;
		if (bitmap != null) {
			bundle = new Bundle();
			bundle.putInt("width", bitmap.getWidth());
			bundle.putInt("height", bitmap.getHeight());
			return bundle;
		}
		return null;
	}

	/**
	 * 判断图片高度和宽度是否过大
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static boolean bitmapIsLarge(Bitmap bitmap) {
		final int MAX_WIDTH = 60;
		final int MAX_HEIGHT = 60;
		Bundle bundle = getBitmapWidthAndHeight(bitmap);
		if (bundle != null) {
			int width = bundle.getInt("width");
			int height = bundle.getInt("height");
			if (width > MAX_WIDTH && height > MAX_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据比例缩放图片
	 * 
	 * @param screenWidth
	 *            手机屏幕的宽度
	 * @param filePath
	 *            图片的路径
	 * @param ratio
	 *            缩放比例
	 * @return
	 */
	public static Bitmap CompressionPhoto(float screenWidth, String filePath,
			int ratio) {
		Bitmap bitmap = PhotoUtils.getBitmapFromFile(filePath);
		Bitmap compressionBitmap = null;
		float scaleWidth = screenWidth / (bitmap.getWidth() * ratio);
		float scaleHeight = screenWidth / (bitmap.getHeight() * ratio);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		try {
			compressionBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (Exception e) {
			return bitmap;
		}
		return compressionBitmap;
	}

	/**
	 * 保存图片到SD卡
	 * 
	 * @param bitmap
	 *            图片的bitmap对象
	 * @return
	 */
	public static String savePhotoToSDCard(Bitmap bitmap) {
		if (!FileUtils.isSdcardExist()) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		FileUtils.createDirFile(IMAGE_PATH);

		String fileName = UUID.randomUUID().toString() + ".jpg";
		String newFilePath = IMAGE_PATH + fileName;
		File file = FileUtils.createNewFile(newFilePath);
		if (file == null) {
			return null;
		}
		try {
			fileOutputStream = new FileOutputStream(newFilePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
		} catch (FileNotFoundException e1) {
			return null;
		} finally {
			try {
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				return null;
			}
		}
		return newFilePath;
	}

	/**
	 * 根据滤镜类型获取图片
	 * 
	 * @param filterType
	 *            滤镜类型
	 * @param defaultBitmap
	 *            默认图片
	 * @return
	 */
	public static Bitmap getFilter(ImageFactoryFliter.FilterType filterType, Bitmap defaultBitmap) {
		if (filterType.equals(ImageFactoryFliter.FilterType.默认)) {
			return defaultBitmap;
		} else if (filterType.equals(ImageFactoryFliter.FilterType.LOMO)) {
			return lomoFilter(defaultBitmap);
		}
		return defaultBitmap;
	}

	/**
	 * 滤镜效果--LOMO
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap lomoFilter(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int dst[] = new int[width * height];
		bitmap.getPixels(dst, 0, width, 0, 0, width, height);

		int ratio = width > height ? height * 32768 / width : width * 32768
				/ height;
		int cx = width >> 1;
		int cy = height >> 1;
		int max = cx * cx + cy * cy;
		int min = (int) (max * (1 - 0.8f));
		int diff = max - min;

		int ri, gi, bi;
		int dx, dy, distSq, v;

		int R, G, B;

		int value;
		int pos, pixColor;
		int newR, newG, newB;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = y * width + x;
				pixColor = dst[pos];
				R = Color.red(pixColor);
				G = Color.green(pixColor);
				B = Color.blue(pixColor);

				value = R < 128 ? R : 256 - R;
				newR = (value * value * value) / 64 / 256;
				newR = (R < 128 ? newR : 255 - newR);

				value = G < 128 ? G : 256 - G;
				newG = (value * value) / 128;
				newG = (G < 128 ? newG : 255 - newG);

				newB = B / 2 + 0x25;

				// ==========边缘黑暗==============//
				dx = cx - x;
				dy = cy - y;
				if (width > height)
					dx = (dx * ratio) >> 15;
				else
					dy = (dy * ratio) >> 15;

				distSq = dx * dx + dy * dy;
				if (distSq > min) {
					v = ((max - distSq) << 8) / diff;
					v *= v;

					ri = newR * v >> 16;
					gi = newG * v >> 16;
					bi = newB * v >> 16;

					newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
					newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
					newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
				}
				// ==========边缘黑暗end==============//

				dst[pos] = Color.rgb(newR, newG, newB);
			}
		}

		Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height,
				Config.RGB_565);
		acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
		return acrossFlushBitmap;
	}

	/**
	 * 根据文字获取图片
	 * 
	 * @param text
	 * @return
	 */
	public static Bitmap getIndustry(Context context, String text) {
		String color = "#ffefa600";
		if ("艺".equals(text)) {
			color = "#ffefa600";
		} else if ("学".equals(text)) {
			color = "#ffbe68c1";
		} else if ("商".equals(text)) {
			color = "#ffefa600";
		} else if ("医".equals(text)) {
			color = "#ff30c082";
		} else if ("IT".equals(text)) {
			color = "#ff27a5e3";
		}
		Bitmap src = BitmapFactory.decodeResource(context.getResources(),
				R.mipmap.ic_userinfo_group);
		int x = src.getWidth();
		int y = src.getHeight();
		Bitmap bmp = Bitmap.createBitmap(x, y, Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(bmp);
		canvasTemp.drawColor(Color.parseColor(color));
		Paint p = new Paint(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.WHITE);
		p.setFilterBitmap(true);
		int size = (int) (13 * context.getResources().getDisplayMetrics().density);
		p.setTextSize(size);
		float tX = (x - getFontlength(p, text)) / 2;
		float tY = (y - getFontHeight(p)) / 2 + getFontLeading(p);
		canvasTemp.drawText(text, tX, tY, p);

		return toRoundCorner(bmp, 2);
	}

	/**
	 * @return 返回指定笔和指定字符串的长度
	 */
	public static float getFontlength(Paint paint, String str) {
		return paint.measureText(str);
	}

	/**
	 * @return 返回指定笔的文字高度
	 */
	public static float getFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.descent - fm.ascent;
	}

	/**
	 * @return 返回指定笔离文字顶部的基准距离
	 */
	public static float getFontLeading(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.leading - fm.ascent;
	}

	/**
	 * 获取圆角图片
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获取颜色的圆角bitmap
	 * 
	 * @param context
	 * @param color
	 * @return
	 */
	public static Bitmap getRoundBitmap(Context context, int color) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics));
		int height = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4.0f, metrics));
		int round = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2.0f, metrics));
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		canvas.drawRoundRect(new RectF(0.0F, 0.0F, width, height), round,
				round, paint);
		return bitmap;
	}

	/*
	 * Bitmap序列化成byte[]
	 */
	public static byte[] getBytes(Bitmap bitmap) {
		// 实例化字节数组输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);// 压缩位图
		return baos.toByteArray();// 创建分配字节数组
	}

	/*
	 * byte[]转换成Bitmap
	 */
	public static Bitmap getBitmap(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0, data.length);// 从字节数组解码位图
	}

	// 压缩图片大小
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 20&&options >= 1) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options /= 2;// 每次都减少5
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		
		//return ThumbnailUtils.extractThumbnail(image, 120, 120);
		return bitmap;
		
	}

	/**
	 * 初始化多图选择的配置
	 *
	 * @param activity
	 * @param maxTotal
	 */
	public static void initMultiConfig(Activity activity, int maxTotal) {
		// 进入相册 以下是例子：用不到的api可以不写
		PictureSelector.create(activity)
				.openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
				.maxSelectNum(maxTotal)// 最大图片选择数量 int
				.minSelectNum(0)// 最小选择数量 int
				.imageSpanCount(3)// 每行显示个数 int
				.selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
				.previewImage(true)// 是否可预览图片 true or false
				.previewVideo(false)// 是否可预览视频 true or false
				.enablePreviewAudio(false) // 是否可播放音频 true or false
				.isCamera(true)// 是否显示拍照按钮 true or false
				//                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
				//                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
				//                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
				.enableCrop(false)// 是否裁剪 true or false
				.compress(true)// 是否压缩 true or false
				.compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
				.compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
				//                .compressMaxKB(1024)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
				//                .compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int
				.glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
				//                .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
				//                .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
				.isGif(false)// 是否显示gif图片 true or false
				//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
				.circleDimmedLayer(false)// 是否圆形裁剪 true or false
				//                .showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
				//                .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
				.openClickSound(false)// 是否开启点击声音 true or false
				//                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
				.previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
				//                .cropCompressQuality()// 裁剪压缩质量 默认90 int
				//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
				//                .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
				//                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
				//                .videoQuality()// 视频录制质量 0 or 1 int
				//                .videoSecond()// 显示多少秒以内的视频or音频也可适用 int
				//                .recordVideoSecond()//视频秒数录制 默认60s int
				.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
	}

	/**
	 * 初始化单张图片选择的配置
	 *
	 * @param activity
	 */
	public static void initSingleConfig(Activity activity) {
		// 进入相册 以下是例子：用不到的api可以不写
		PictureSelector.create(activity)
				.openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
				.maxSelectNum(1)// 最大图片选择数量 int
				.minSelectNum(0)// 最小选择数量 int
				.imageSpanCount(3)// 每行显示个数 int
				.selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
				.previewImage(true)// 是否可预览图片 true or false
				.previewVideo(false)// 是否可预览视频 true or false
				.enablePreviewAudio(false) // 是否可播放音频 true or false
				.isCamera(true)// 是否显示拍照按钮 true or false
				//                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
				//                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
				//                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
				.enableCrop(true)// 是否裁剪 true or false
				.compress(true)// 是否压缩 true or false
				.compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
				.compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
				//                .compressMaxKB(500)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
				//                .compressWH(7, 10) // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int
				.glideOverride(130, 130)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
				//                .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
				.hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
				.isGif(false)// 是否显示gif图片 true or false
				.freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
				.circleDimmedLayer(true)// 是否圆形裁剪 true or false
				.showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
				.showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
				.openClickSound(false)// 是否开启点击声音 true or false
				//                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
				.previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
				//                .cropCompressQuality()// 裁剪压缩质量 默认90 int
				//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
				.rotateEnabled(true) // 裁剪是否可旋转图片 true or false
				.scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
				//                .videoQuality()// 视频录制质量 0 or 1 int
				//                .videoSecond()// 显示多少秒以内的视频or音频也可适用 int
				//                .recordVideoSecond()//视频秒数录制 默认60s int
				.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
	}

}
