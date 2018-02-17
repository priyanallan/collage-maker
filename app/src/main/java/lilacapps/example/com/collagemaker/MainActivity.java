package lilacapps.example.com.collagemaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import service.CollageMakerIntentService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view) {

        Intent collageIntent = new Intent(this, CollageMakerIntentService.class);
        startService(collageIntent);

    }
}
