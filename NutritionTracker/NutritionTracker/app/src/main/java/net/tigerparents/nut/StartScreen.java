package net.tigerparents.nut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;


public class StartScreen extends Activity {

    static final int EXPORT_FILE_PATH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_screen, menu);
        return true;
    }

    public void onTrackerButtonClick(View view) {
        Intent intent = new Intent(this, NutritionTracker.class);
        startActivity(intent);
    }

    public void enterUserData(View view) {
        Intent intent = new Intent(this, EnterUserData.class);
        startActivity(intent);
    }

    public void onExportData(View view) {
        Intent intent = new Intent(this, ExportDialog.class);
        startActivityForResult(intent, EXPORT_FILE_PATH);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EXPORT_FILE_PATH) {
            if (resultCode == RESULT_OK) {
                String file_uri = data.getStringExtra(ExportDialog.EXPORT_FILE_NAME);
                try {
                    FileOutputStream output = new FileOutputStream(new File(file_uri));
                } catch (Exception e) {
                    Log.e("StartScreen", "Cannot export data", e);
                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
