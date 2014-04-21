package net.tigerparents.nut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ShoppingTracker extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_tracker);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_tracker, menu);
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

    public void shoppingReports(View view) {
        Intent intent = new Intent(this, ShowReports.class);
        intent.putExtra(ShowReports.REPORTS_PARENT, ShowReports.REPORTS_FOR_SHOPPING);
        startActivity(intent);
    }

    public void onShoppingClick(View view) {
        Intent intent = new Intent(this, ShoppingEntry.class);
        startActivity(intent);
    }

    public void onShowRecenEntries(View view) {
        // empty
    }
}
