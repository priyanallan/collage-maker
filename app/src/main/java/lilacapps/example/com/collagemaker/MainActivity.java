package lilacapps.example.com.collagemaker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.lopei.collageview.CollageView;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import service.CollageMakerIntentService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void startService(View view) {

        ArrayList<String> photos_final = new ArrayList<String>();

        boolean file_flag = false;
        Intent collageIntent = new Intent(this, CollageMakerIntentService.class);

        /*
         Verify if the android system has internal / external storage
          */

        // This verifies internal memory
        String extStore = System.getenv("EXTERNAL_STORAGE");
        if (extStore != null) {
            Log.i("External storage", extStore);
            //Get path of Camera
            File cameraPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            Log.i("Camera path", cameraPath.getAbsolutePath());
            String[] list = cameraPath.list();
            if (list != null) {
                for (String l : list) {
                    String path_absolute = cameraPath+"/"+l;
                    File path_absolute_File = new File(path_absolute);
                    file_flag = path_absolute_File.isDirectory();

                    if(file_flag){
                            File[] listOfFiles = path_absolute_File.listFiles();
                            for(File eachFile : listOfFiles){
                                String absolute_Path = eachFile.getAbsolutePath();
                                if(!absolute_Path.contains("/.")) {
                                Log.i("File path",absolute_Path);

                                    photos_final.add(eachFile.getAbsolutePath());
                                }
                            }

                    }
                    else{
                        photos_final.add(path_absolute_File.getAbsolutePath());
                    }
                }
            }
        }
        // This verifies external SD card
        String secStore = System.getenv("SECONDARY_STORAGE");
        if (secStore != null) {
            Log.i("Secondary storage", secStore);

            File file = new File(secStore, "/DCIM");
            File[] listofFiles = file.listFiles();
            int i = 0;
            for (File eachFile : listofFiles) {
                Log.i("File path of item" + (++i), eachFile.getAbsolutePath());
            }
        }

        collageIntent.putStringArrayListExtra(CollageMakerIntentService.PHOTO_URLS,photos_final);
        collageIntent.putExtra(CollageMakerIntentService.EXTRA_RESOURCE,R.layout.collage_view);
        collageIntent.putExtra(CollageMakerIntentService.EXTRA_WIDTH,540);
        collageIntent.putExtra(CollageMakerIntentService.EXTRA_HEIGHT,960);
        collageIntent.putExtra(CollageMakerIntentService.EXTRA_FILENAME,"view_"+System.currentTimeMillis()+".png");

        startService(collageIntent);
    }
}