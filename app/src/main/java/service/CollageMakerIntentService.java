package service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.lopei.collageview.CollageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import lilacapps.example.com.collagemaker.MainActivity;
import lilacapps.example.com.collagemaker.R;

/**
 * Created by 1021422 on 2/14/2018.
 */

public class CollageMakerIntentService extends IntentService {

    private static final int COLLAGE_SERVICE_NOTIFICATION_ID = 1221;
    private static final int REQUEST_CODE = 0;
    public static final String EXTRA_WIDTH = "width";
    public static final String EXTRA_HEIGHT = "height";
    public static final String EXTRA_RESOURCE = "resource";
    public static final String EXTRA_FILENAME = "filename";
    public static final String PHOTO_URLS = "photo_urls";

    public CollageMakerIntentService() {
        super("Collage Maker Intent Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int resId = intent.getIntExtra(EXTRA_RESOURCE, 0);
        int width = intent.getIntExtra(EXTRA_WIDTH, -1);
        int height = intent.getIntExtra(EXTRA_HEIGHT, -1);
        String filename = intent.getStringExtra(EXTRA_FILENAME);

        // Send a notification when a service starts
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), REQUEST_CODE, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Service Info")
                .setContentText("Service Started")
                .setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
                .setAutoCancel(true);
        mBuilder.setContentIntent(mPendingIntent);
        notificationManager.notify(COLLAGE_SERVICE_NOTIFICATION_ID, mBuilder.build());

        //Get the photo urls
        List<String> list_of_urls = intent.getStringArrayListExtra("Photo_urls");
        saveLayoutBitmap(resId, width, height, filename, list_of_urls);
    }

    // TODO : Replace the collageView with normal imageView and get the photos from resources folder instead of phone memory.

    // This method creates collages and saves into a local directory
    private void saveLayoutBitmap(int resId, int width, int height, String filename, List<String> urls)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);;
        View view =  inflater.inflate(resId, null);
        CollageView collageView = (CollageView) view;

        collageView.setVisibility(View.VISIBLE);
        collageView
                .backgroundColor(Color.WHITE)
                .photoFrameColor(Color.BLUE)
                .defaultPhotosForLine(6) // sets default photos number for line of photos (can be changed by program at runtime)
                .useCards(true) // adds cardview backgrounds to all photos
                .maxWidth(100) // will resize images if their side is bigger than 100
                .loadPhotos(urls);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        int w = width < 0 ? displayMetrics.widthPixels : width;
        int h = height < 0 ? displayMetrics.heightPixels : height;

        collageView.measure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(h,View.MeasureSpec.EXACTLY));
        collageView.layout(0,0,collageView.getMeasuredWidth(),collageView.getMeasuredHeight());
        collageView.setDrawingCacheEnabled(true);
        collageView.buildDrawingCache();

        Bitmap bmp = collageView.getDrawingCache();

        // Get the Downloads folder in storage and save the collage on device 
        String target = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + filename;
        File f = new File(target);

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException io){
            io.printStackTrace();
        }

    }
}
