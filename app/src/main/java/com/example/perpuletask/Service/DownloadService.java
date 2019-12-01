package com.example.perpuletask.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;

import com.example.perpuletask.DI.Models.AudioModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;
    AudioModel audioModel;
    public DownloadService() {
        super("DownloadService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
         audioModel= (AudioModel) intent.getSerializableExtra("audio");
        String urlToDownload = audioModel.getAudio();
        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
        try {

            //create url and connect
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();

            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());

            String path = getVideoFilePath(this,audioModel) ;
            OutputStream output = new FileOutputStream(path);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;

                // publishing the progress....
                Bundle resultData = new Bundle();
                resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                resultData.putSerializable("audio",audioModel);

                receiver.send(UPDATE_PROGRESS, resultData);
                output.write(data, 0, count);
            }

            // close streams 
            output.flush();
            output.close();
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle resultData = new Bundle();
        resultData.putInt("progress" ,100);
        resultData.putSerializable("audio",audioModel);

        receiver.send(UPDATE_PROGRESS, resultData);
    }

    public static String getVideoFilePath(Context context,AudioModel audioModel) throws IOException {
        final File dir = context.getExternalFilesDir(null);
        if(dir == null){
            return "";
        }else{
            File folder = new File(dir.getAbsolutePath()+"/PerpuleAudios" );
            folder.mkdirs();


            //Save the path as a string value
            String extStorageDirectory = folder.toString();

            //Create New file and name it Image2.PNG
            File file = new File(extStorageDirectory,audioModel.getDesc() );
            if(!file.exists()){
                file.createNewFile();
            }
            return file.getAbsolutePath();
        }

    }
}