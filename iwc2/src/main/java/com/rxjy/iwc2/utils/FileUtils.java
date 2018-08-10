package com.rxjy.iwc2.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtils
{

	/**
	 * 检查是否安装SD卡
	 *
	 * @return
	 */
	public static boolean checkSaveLocationExists()
	{
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED))
		{
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 检查是否安装外置的SD卡
	 *
	 * @return
	 */
	public static boolean checkExternalSDExists()
	{
		Map<String, String> evn = System.getenv();
		return evn.containsKey("SECONDARY_STORAGE");
	}

	/**
	 * 删除文件
	 *
	 * @param filePath
	 */
	public static boolean deleteFileWithPath(String filePath) {
		SecurityManager checker = new SecurityManager();
		File f = new File(filePath);
		checker.checkDelete(filePath);
		if (f.isFile()) {
//			Log.i("DirectoryManager deleteFile", filePath);
			f.delete();
			return true;
		}
		return false;
	}

	/**
	 * 删除文件
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

//			File path = Environment.getExternalStorageDirectory();
//			File newPath = new File(path.toString() + fileName);
			File newPath = new File(fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isFile()) {
				try {
//					Log.i("DirectoryManager deleteFile", fileName);
					newPath.delete();
					status = true;
				} catch (SecurityException se) {
					se.printStackTrace();
					status = false;
				}
			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * 清空一个文件夹
	 * @param filePath
	 */
	public static void clearFileWithPath(String filePath) {
		List<File> files = FileUtils.listPathFiles(filePath);
		if (files.isEmpty()) {
			return;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				clearFileWithPath(f.getAbsolutePath());
			} else {
				f.delete();
			}
		}
	}

	/**
	 * 获取一个文件夹下的所有文件
	 * @param root
	 * @return
	 */
	public static List<File> listPathFiles(String root) {

		List<File> allDir = new ArrayList<File>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		File[] files = path.listFiles();

		if(files != null && files.length != 0){
			for (File f : files) {
				if (f.isFile())
					allDir.add(f);
				else
					listPath(f.getAbsolutePath());
			}
		}
		return allDir;
	}

	/**
	 * 列出root目录下所有子目录
	 *
	 * @param root
	 * @return 绝对路径
	 */
	public static List<String> listPath(String root) {
		List<String> allDir = new ArrayList<String>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		// 过滤掉以.开始的文件夹
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				if (f.isDirectory() && !f.getName().startsWith(".")) {
					allDir.add(f.getAbsolutePath());
				}
			}
		}
		return allDir;
	}

	/**
	 * 获取SD卡的根目录
	 *
	 * @return
	 */
	public static String getSDRoot()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 检查文件是否存在
	 *
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String name)
	{
		boolean status;
		if (!name.equals(""))
		{
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + name);
			status = newPath.exists();
		} else
		{
			status = false;
		}
		return status;
	}

	/**
	 * 检查路径是否存在
	 *
	 * @param path
	 * @return
	 */
	public static boolean checkFilePathExists(String path)
	{
		return new File(path).exists();
	}

	/**
     * 使用当前时间戳拼接一个唯一的文件名
	 *
     * @return
     */
    public static String getTempFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
        String fileName = format.format(new Timestamp(System.currentTimeMillis()));
        return fileName;
    }

	/**
	 * 获得sd卡剩余容量，即可用大小
	 *
	 * @return
	 */
	private String getSDAvailableSize(Context context) {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(context, blockSize * availableBlocks);
	}

	public static void deleteDateFile(String deleteDate)
	{
		File parentFile = new File(Environment.
				getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IWC");
		File dateFile = new File(parentFile, deleteDate);
		if (dateFile.exists())
		{
			FileUtils.clearFileWithPath(dateFile.getPath());
			if (dateFile.isDirectory())
			{
				dateFile.delete();
			}
		}
	}

	public static void deleteDateFile(String[] deleteDates)
	{
		File parentFile = new File(Environment.
				getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IWC");
		for (String deleteDate : deleteDates)
		{
			File dateFile = new File(parentFile, deleteDate);
			if (dateFile.exists())
			{
				FileUtils.clearFileWithPath(dateFile.getPath());
				if (dateFile.isDirectory())
				{
					dateFile.delete();
				}
			}
		}
	}

}
