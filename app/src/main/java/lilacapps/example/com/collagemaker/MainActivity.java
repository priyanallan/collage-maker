package lilacapps.example.com.collagemaker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.nio.file.Files;
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
        }
    }

    public void startService(View view) {

        boolean file_flag = false;
        Intent collageIntent = new Intent(this, CollageMakerIntentService.class);
        startService(collageIntent);

        // Verify if the android system has internal / external storage

        // This verifies internal SD card
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
                                Log.i("File path",eachFile.getAbsolutePath());
                            }
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
    }
}