package com.wezebra.zebraking.util;

import android.text.TextUtils;
import android.util.Log;

import com.wezebra.zebraking.Settings;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * log util
 *
 * @version 1.0.0
 */
public class L
{
//    private static final String TAG = "weZebra";
    /**
     * log file name
     */
    public static final String PERSIST_PATH = Constants.LOGS_DIR + File.separator + "log";

    public static void p(String TAG, String log)
    {
        if (Settings.DEBUG)
        {
            System.out.println(log);
        }
        L.d(TAG, log);
    }

    public static void v(String TAG, String text)
    {
        print(TAG, text, Log.VERBOSE);
    }

    public static void d(String TAG, String text)
    {
        print(TAG, text, Log.DEBUG);
    }

    public static void i(String TAG, String text)
    {
        print(TAG, text, Log.INFO);
    }

    public static void w(String TAG, String text)
    {
        print(TAG, text, Log.WARN);
    }

    public static void e(String TAG, String text)
    {
        print(TAG, text, Log.ERROR);
    }

    public static void e(String TAG, String text, Throwable throwable)
    {
        print(TAG, text + "#message:" + throwable.getMessage(), Log.ERROR);
        StackTraceElement[] elements = throwable.getStackTrace();
        for (StackTraceElement e : elements)
        {
            print(TAG, e.toString(), Log.ERROR);
        }
    }

    private static synchronized void print(final String TAG, final String text, final int level)
    {
        if (TextUtils.isEmpty(text))
        {
            return;
        }
        if (Settings.DEBUG)
        {
            switch (level)
            {
                case Log.VERBOSE:
                    Log.v(TAG, text);
                    break;
                case Log.DEBUG:
                    Log.d(TAG, text);
                    break;
                case Log.INFO:
                    Log.i(TAG, text);
                    break;
                case Log.WARN:
                    Log.w(TAG, text);
                    break;
                case Log.ERROR:
                    Log.e(TAG, text);
                    break;
            }
        }
        if (Settings.PERSIST_LOG)
        {
            ThreadPoolUtil.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    writeLog(TAG, text, level);
                }
            });
        }
    }

    /**
     * write the log into the file
     *
     * @param text
     * @param level
     */
    private static synchronized void writeLog(String TAG, String text, int level)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[" + DateUtils.toTime(System.currentTimeMillis(), DateUtils.DATE_FORMAT_HOUR_MINUTE_SECOND) + "]");
        switch (level)
        {
            case Log.VERBOSE:
                sb.append("[VERBOSE]\t");
                break;
            case Log.DEBUG:
                sb.append("[DEBUG]\t");
                break;
            case Log.INFO:
                sb.append("[INFO ]\t");
                break;
            case Log.WARN:
                sb.append("[WARN ]\t");
                break;
            case Log.ERROR:
                sb.append("[ERROR]\t");
                break;
        }

        RandomAccessFile raf = null;
        try
        {
            String fileName = PERSIST_PATH + "_" + DateUtils.toTime(System.currentTimeMillis(), DateUtils.DATE_DEFAULT_FORMAT);
            File logFile = new File(fileName);
            if (!logFile.exists())
            {
                FileUtils.initExternalDir(false);
                logFile.createNewFile();
            }
            raf = new RandomAccessFile(fileName, "rw");
            raf.seek(raf.length());
            raf.writeBytes(sb.toString() + TAG + "-->" + text + "\r\n");

        } catch (IOException e)
        {
            L.p(TAG, "can`t write the log tp file");
        } finally
        {
            if (raf != null)
            {
                try
                {
                    raf.close();
                } catch (IOException e)
                {
                }
            }
        }
    }


}
