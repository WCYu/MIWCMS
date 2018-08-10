package com.rxjy.iwc2.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils
{

	/**
     * @throws IOException
     */
    public static void saveImage(Context context, String fileName, Bitmap bitmap)
            throws IOException {
        saveImage(context, fileName, bitmap, 100);
    }

    public static void saveImage(Context context, String fileName, Bitmap bitmap, int quality)
    {
        if (bitmap == null || fileName == null || context == null)
        	return;
        try
		{
        	FileOutputStream fos = new FileOutputStream(fileName);
//			FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, quality, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes);
			fos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
    }

	/**
	 * 把bitmap转换成String
	 * @param filePath
	 * @return
	 */
	public static String bitmapToString(String filePath, int quality) {

		Bitmap bm = getSmallBitmap(filePath);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		switch (quality){
			case 1:
				bm.compress(CompressFormat.JPEG, 100, baos);
				break;
			case 2:
				bm.compress(CompressFormat.JPEG, 90, baos);
				break;
			case 3:
				bm.compress(CompressFormat.JPEG, 40, baos);
				break;
		}
//		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	/**
	 * 计算图片的缩放值
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight; // 1920
		final int width = options.outWidth;   // 2560
		int inSampleSize = 2;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight); // 2
			final int widthRatio = Math.round((float) width / (float) reqWidth);    // 3
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 根据路径获得图片并压缩返回bitmap用于显示
	 * @param
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = calculateInSampleSize(options, 960, 1280);
//		options.inSampleSize = calculateInSampleSize(options, 1920, 2560);
		options.inSampleSize = 3;
		options.inJustDecodeBounds = false; // 设置为true 会报空指针异常 62 Line
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
//		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		return bitmap;
	}

	/**
	 * 根据路径删除图片
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 添加到图库
	 */
	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	/**
	 * 获取保存图片的目录
	 * @return
	 */
	public static File getAlbumDir() {
		File dir = new File(
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 获取保存 隐患检查的图片文件夹名称
	 * @return
	 */
	public static String getAlbumName() {
		return "sheguantong";
	}


}