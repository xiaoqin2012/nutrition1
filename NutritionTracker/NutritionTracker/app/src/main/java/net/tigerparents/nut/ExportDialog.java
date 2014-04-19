package net.tigerparents.nut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;


public class ExportDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_dialog);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.export_dialog, menu);
        return true;
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

    public void onExport(View view) {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            Log.e("Export Dialog", "media not mounted");
            // error out
            gotoErrorDialog();
        }
        TextView tv = (TextView) findViewById(R.id.editText);
        String filename = tv.getText().toString();
        try {
            File exportFile = new File(Environment.getExternalStorageDirectory(), filename);
            FileOutputStream output = new FileOutputStream(exportFile);
            output.write(0);
            output.close();

            String new_intent_string = getIntent().getStringExtra("callback");
            Intent return_value = new Intent(new_intent_string);
            return_value.putExtra("exportfile", exportFile.toURI());
            if (getParent() == null) {
                setResult(Activity.RESULT_OK, return_value);
            } else {
                getParent().setResult(Activity.RESULT_OK, return_value);
            }
            finish();
        } catch (Exception e) {
            // error out
            Log.e("Export Dialog", "Cannot write output", e);
            gotoErrorDialog();
        }

    }

    public void gotoErrorDialog() {
        Intent intent = new Intent(this, ExportDialogError.class);
        startActivity(intent);
    }

}
