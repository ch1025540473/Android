package com.wezebra.zebraking.util;

import android.os.Environment;
import android.text.TextUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils
{
    private static final String TAG = "FileUtils";

    /**
     * get the external storage file
     *
     * @return the file
     */
    public static File getExternalStorageDir()
    {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * get the external storage file path
     *
     * @return the file path
     */
    public static String getExternalStoragePath()
    {
        return getExternalStorageDir().getAbsolutePath();
    }

    /**
     * get the external storage state
     *
     * @return
     */
    public static String getExternalStorageState()
    {
        return Environment.getExternalStorageState();
    }

    /**
     * check the usability of the external storage.
     *
     * @return enable -> true, disable->false
     */
    public static boolean isExternalStorageEnable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        return false;

    }

    /**
     * 初始化我们的工作目录
     *
     * @param cleanFile
     */
    public static void initExternalDir(boolean cleanFile)
    {
        if (isExternalStorageEnable())
        {
            File external = new File(Constants.EXTERNAL_DIR);
            if (!external.exists())
            {
                external.mkdirs();
            }
            //check the cache whether exist
            File cache = new File(Constants.CACHE_DIR);
            if (!cache.exists())
            {
                cache.mkdirs();
            } else
            {
                if (cleanFile)
                {
                    //if exist,so clear the old file
                    cleanFile(cache, DateUtils.WEEK_MILLIS);
                }
            }
            //check the log dir
            File logs = new File(Constants.LOGS_DIR);
            if (!logs.exists())
            {
                logs.mkdirs();
            } else
            {
                if (cleanFile)
                {
                    cleanFile(logs, DateUtils.HALF_MONTH_MILLIS);
                }
            }
            //check the image dir
            File images = new File(Constants.IMAGE_DIR);
            if (!images.exists())
            {
                images.mkdirs();
            } else
            {
                if (cleanFile)
                {
                    cleanFile(images, DateUtils.HALF_MONTH_MILLIS);
                }
            }
        }
    }

    /**
     * 清理一个目录下面的文件
     *
     * @param dir
     * @param maxInterval
     * @return
     */
    public static int cleanFile(File dir, long maxInterval)
    {
        File[] files = dir.listFiles();
        if (files == null) return 0;
        int beforeNum = 0;
        long current = System.currentTimeMillis();
        for (File file : files)
        {
            long lastModifiedTime = file.lastModified();
            if ((current - lastModifiedTime) > maxInterval)
            {
                //if the file is exist more than a week , so need to delete.
                file.delete();
                beforeNum++;
            }
        }
        return beforeNum;
    }

    /**
     * 获取一个文件的内容
     *
     * @param path
     * @return
     */
    public static String getFileContent(String path)
    {

        File file = new File(path);
        return getFileContent(file);
    }

    /**
     * 获取一个文件的内容
     *
     * @param file
     * @return
     */
    public static String getFileContent(File file)
    {
        if (!file.exists())
        {
            return null;
        }
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
            StringBuilder sb = new StringBuilder();
            byte buffer[] = new byte[2048];
            int len = 0;
            while ((len = fis.read(buffer)) != -1)
            {
                sb.append(new String(buffer, 0, len));
            }
            return sb.toString();
        } catch (Exception e)
        {
            L.e("", "get file exception:" + file.getAbsolutePath(), e);
        } finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                } catch (IOException e)
                {
                }
            }
        }
        return null;
    }

    /**
     * 保存一个文件
     *
     * @param content
     * @param path
     * @return
     */
    public static boolean saveFile(String content, String path)
    {
        if (isExternalStorageEnable())
        {
            //看看本地有没有
            File cacheFile = new File(path);
            FileOutputStream fos = null;
            try
            {
                fos = new FileOutputStream(cacheFile);
                fos.write(content.getBytes());
                fos.flush();
                return true;
            } catch (IOException e)
            {
                L.e(TAG, "save file exception:" + path, e);
            } finally
            {
                if (fos != null)
                {
                    try
                    {
                        fos.close();
                    } catch (IOException e)
                    {
                    }
                }
            }
        }
        return false;
    }

    /**
     * 删除一个文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path)
    {
        if (TextUtils.isEmpty(path))
        {
            return true;
        }
        File file = new File(path);
        if (file.exists())
        {
            return file.delete();
        }
        return true;
    }

    /**
     * 给出一个路径，判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isFileExist(String path)
    {
        if (TextUtils.isEmpty(path))
            return false;
        File file = new File(path);
        return file.exists();
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException
    {
        if (!destFile.exists())
        {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try
        {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally
        {
            if (source != null)
            {
                source.close();
            }
            if (destination != null)
            {
                destination.close();
            }
        }
    }

    public static File getDataDirectory() {
        return Environment.getRootDirectory();
    }

}
