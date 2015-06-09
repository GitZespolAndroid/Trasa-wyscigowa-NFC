package com.example.jt.nfcaplikacja4;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class PlaceInformation extends ActionBarActivity {

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_information);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            String value = extras.getString("Nazwamiejsca");
            String value2 = extras.getString("Wspolrzedne");
            String value3 = extras.getString("Adres");
            String value4 = extras.getString("AdresWWW");
            String value5 = extras.getString("ZdjecieAdresWWW");
            String value6 = extras.getString("OpisMiejsca");

            TextView NazwaMiejsca = (TextView) findViewById(R.id.textView40);
            TextView Wspolrzedne = (TextView) findViewById(R.id.textView41);
            TextView Adres = (TextView) findViewById(R.id.textView42);
            TextView AdresWWW = (TextView) findViewById(R.id.textView43);
            TextView ZdjecieAdresWWW = (TextView) findViewById(R.id.textView44);
            TextView OpisMiejsca = (TextView) findViewById(R.id.textView45);

            NazwaMiejsca.setText(value);
            Wspolrzedne.setText(value2);
            Adres.setText(value3);
            AdresWWW.setText(value4);
            ZdjecieAdresWWW.setText(value5);
            OpisMiejsca.setText(value6);
        }


    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_information, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
