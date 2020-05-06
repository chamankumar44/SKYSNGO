package com.ganaur.skys.skysngo.network_http;

import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by apple on 22/04/20.
 */

public class HttpNetwork {

    public boolean downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void downloadAllFiles(String urlTemp){


        File sdCardRoot = new File("/storage/emulated/0/testFolder");

        if (!sdCardRoot.exists()) {
            sdCardRoot.mkdirs();
        }

//        Log.e("check_path", "" + sdCardRoot.getAbsolutePath());

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlTemp);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();


//            sdCardRoot = new File(Environment.getExternalStorageDirectory(), "MyProfile");

//            if (!sdCardRoot.exists()) {
//                sdCardRoot.mkdirs();
//            }
//
            Log.e("check_path", "" + sdCardRoot.getAbsolutePath());

            String fileName = "abc.jpg";
//                    strings[0].substring(strings[0].lastIndexOf('/') + 1, strings[0].length());
//            Log.e("dfsdsjhgdjh", "" + fileName);
            File imgFile =
                    new File(sdCardRoot, fileName);
            if (!sdCardRoot.exists()) {
                imgFile.createNewFile();
            }
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            FileOutputStream outPut = new FileOutputStream(imgFile);
            int downloadedSize = 0;
            byte[] buffer = new byte[2024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                outPut.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.e("Progress:", "downloadedSize:" + Math.abs(downloadedSize * 100 / totalSize));
            }
            Log.e("Progress:", "imgFile.getAbsolutePath():" + imgFile.getAbsolutePath());

            Log.e("", "check image path 2" + imgFile.getAbsolutePath());

//            mImageArray.add(imgFile.getAbsolutePath());
            outPut.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("checkException:-", "" + e);
        }
//        return null;

    }
}
