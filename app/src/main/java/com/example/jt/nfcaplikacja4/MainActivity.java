package com.example.jt.nfcaplikacja4;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.TimedText;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.nfc.NfcAdapter;
import android.view.LayoutInflater;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Toast;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.app.PendingIntent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.util.Log;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends ActionBarActivity implements LocationListener, NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NfcAdapter mNfcAdapter;
    private TextView mTextView;
    private TextView mCzas;
    private String mUser;
    static final String STATE_USER = "user";
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    int i = 0;
    String[] TABLICA1 = {"1","2","3","4","5","nn"};
    String[] TABLICA2 = {"","","","","",""};
    int j,m = 0;
    long time = 0;
    GoogleMap mMap;
    PolylineOptions polylineOptions;
    ArrayList<LatLng> coordList = new ArrayList<LatLng>();
    double lan;
    double lng;
    LatLng bdTest;
    LatLng bdTest1;
    TextView LAT;
    TextView LNG, SPEED, ACCURACY, LENGHT, DATA;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    // ZMIENNE ZWIĄZANE Z PRZYCISKAMI SĄ DANE ODRĘBNIE W KAŻDEJ Z FUNKCJI I KLAS, PONIEWAŻ W INNYM PRZYPADKU APLIKACJA "WYKRZACZA SIĘ"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Instance state", "onCreate");

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mTextView = (TextView) findViewById(R.id.textView_explanation);
        final Button START = (Button) findViewById(R.id.PRZYCISK_START);
        final Button STOP = (Button) findViewById(R.id.PRZYCISK_STOP);
        final Chronometer chrono = (Chronometer) findViewById(R.id.chronometer);
        LocationManager locationManager;

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        List<String> str = locationManager.getProviders(true);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

// Create a criteria object needed to retrieve the provider
        Criteria criteria = new Criteria();

// Get the name of the best available provider
        String provider = locationManager.getBestProvider(criteria, true);

// We can use the provider immediately to get the last known location
        Location location = locationManager.getLastKnownLocation(provider);

// request that the provider send this activity GPS updates every 20 seconds
        locationManager.requestLocationUpdates(provider, 2000, 0, this);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));







        // Sprawdzenie czy urządzenie zawiera GPS i NFC oraz czy są one włączone

        if (mNfcAdapter == null && str.size()<=0){ // Jeśli nie to wyświetl odpowiedni komunikat i zakończ aplikację
            Toast.makeText(this, "Urządzenie nie posiada NFC i GPS!", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(mNfcAdapter == null && str.size()>0) { // Jeśli nie to wyświetl odpowiedni komunikat i zakończ aplikację
            Toast.makeText(this, "Urządzenie nie posiada NFC!", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(str.size()<=0 && mNfcAdapter != null) { // Jeśli nie to wyświetl odpowiedni komunikat i zakończ aplikację
            Toast.makeText(this, "Urządzenie nie posiada GPS!", Toast.LENGTH_LONG).show();
            finish();
        }

        // Sprawdzenie czy jest włączone NFC i GPS

            if (!mNfcAdapter.isEnabled() && (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                    !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) { // Jeśli nie to wyświetl odpowiedni komunikat i zakończ aplikację
                Toast.makeText(this, "Włącz NFC i GPS przed uruchomieniem!", Toast.LENGTH_LONG).show();
                finish();
            } else if (!mNfcAdapter.isEnabled() && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) { // Jeśli nie to wyświetl odpowiedni komunikat i zakończ aplikację
                Toast.makeText(this, "Włącz NFC przed uruchomieniem!", Toast.LENGTH_LONG).show();
                finish();
            } else if ((!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                    !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) && mNfcAdapter.isEnabled()) { // Jeśli nie to wyświetl odpowiedni komunikat i zakończ aplikację
                Toast.makeText(this, "Włącz GPS przed uruchomieniem!", Toast.LENGTH_LONG).show();
                finish();
            } else if ((locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) && mNfcAdapter.isEnabled()){ // Jeśli nie to wyświetl odpowiedni komunikat i zakończ aplikację
                Toast.makeText(this, "NFC i GPS zostały włączone poprawnie!", Toast.LENGTH_LONG).show();
            }

        //OBSLUGA PRZYCISKU START
        START.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                START.setEnabled(false);
                STOP.setEnabled(true);
                chrono.setBase(SystemClock.elapsedRealtime());
                chrono.start();
            }
        });

        //OBSLUGA PRZYCISKU STOP
        STOP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                START.setEnabled(true);
                STOP.setEnabled(false);

                for (m = 0; m < 6; m++) {
                    TABLICA2[m] = "";
                }

                mTextView.setText("Zbliż do nalepki startowej");
                chrono.stop();
                chrono.setBase(SystemClock.elapsedRealtime());
            }
        });

    }

    //--------MENU ROZWIJANE PO LEWO ---------------------------------------------------------------

    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position){

            case 1 :

                Intent intent = new Intent(this,PlaceInformation.class);
                startActivity(intent);

                break;


            case 2:

                Intent intent2 = new Intent(this,RunHistory.class);
                startActivity(intent2);
                break;

            case 3:


            break;
            case 4:

                Intent intent3 = new Intent(this,Sets.class);
                startActivity(intent3);
                break;

            case 5:

                Intent intent4 = new Intent(this,About.class);
                startActivity(intent4);
                break;

            default:
                break;


        }

        // update the main content by replacing fragments
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

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

    //----------------------------------------------------------------------------------------------

    // Włączenie ForegroundDispatcha w trakcie działania aplikacji lub jej wznowienia
    @Override
        protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
    }

    // Wstrzymanie ForegroundDispatcha w trakcie wstrzymania aplikacji
    @Override
    protected void onPause() {
        super.onPause();
        stopForegroundDispatch(this, mNfcAdapter);
    }

    //Zapobiegnięcie przed przypadkowym zamknięciem aplikacji
    public void onBackPressed()
    {
        if(i == 1)
        {
                i=0;
                finish();
        }
        else
        {
               Toast.makeText(this, "Wciśnij jeszcze raz aby zamknąć.", Toast.LENGTH_SHORT).show();
               i++;

        }
        return;
    }

    // Obsługa tagu bezpośrednio po wykryciu
    @Override
    protected void onNewIntent(Intent intent) {

        final Button START = (Button) findViewById(R.id.PRZYCISK_START);
        final Button STOP = (Button) findViewById(R.id.PRZYCISK_STOP);

        if (!START.isEnabled()) {
            handleIntent(intent); //Odczyt zawartości taga
        }
    }

    // Odczyt zawartości taga
    private void handleIntent(Intent intent) {


        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    // Funkcja realizująca działanie ForegroundDispatcha
    public static void setupForegroundDispatch(final ActionBarActivity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    // Funkcja realizująca wstrzymanie ForegroundDispatcha
    public static void stopForegroundDispatch(final ActionBarActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }



    // RYSOWANIE NA MAPIE GDY JEST ZAŁADOWANA

    @Override
    public void onLocationChanged(Location location) {

        int t = 0;
        LatLng currentPosition = new LatLng(lan, lng);

        if (mMap != null)
        {
            drawMarker(location);
        }
    }

    // OBSŁUGA MAPY

    private void drawMarker(Location location) {
        mMap.clear();
        lan = Math.round(location.getLatitude() * 100000d) / 100000d;
        lng = Math.round(location.getLongitude() * 100000d) / 100000d;
        int p = 0;
        int j = 0;
        final Button START = (Button) findViewById(R.id.PRZYCISK_START);
        final Button STOP = (Button) findViewById(R.id.PRZYCISK_STOP);

//  convert the location object to a LatLng object that can be used by the map API
        LatLng currentPosition = new LatLng(lan, lng);
        polylineOptions = new PolylineOptions().width(10).color(Color.RED);

// zoom to the current location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16));

// add a marker to the map indicating our current position

        if ((!START.isEnabled() && STOP.isEnabled())) {

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16));

            if (coordList.isEmpty()) {
                coordList.add(new LatLng(lan, lng));
                mMap.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .snippet("Lat:" + lan + "Lng:" + lng));
            } else if (location.getSpeed() >= 0.01) {
                coordList.add(new LatLng(lan, lng));

                if (coordList.size() > 2 && p == 1) {

                    p = 0;
                    coordList.set(coordList.size() - 1, coordList.get(coordList.size() - 2));
                    mMap.addMarker(new MarkerOptions()
                            .position(coordList.get(coordList.size() - 1))
                            .snippet("Lat:" + lan + "Lng:" + lng));
                } else if (coordList.size() >= 2) {

                    coordList.set(1, coordList.get(0));
                    mMap.addPolyline(polylineOptions.addAll(coordList));
                    mMap.addMarker(new MarkerOptions()
                            .position(coordList.get(coordList.size() - 1))
                            .snippet("Lat:" + lan + "Lng:" + lng));

                }
            } else if (location.getSpeed() < 0.01) {

                p = 1;
                mMap.addPolyline(polylineOptions.addAll(coordList));

                if (coordList.size() == 1) {
                    mMap.addMarker(new MarkerOptions()
                            .position(currentPosition)
                            .snippet("Lat:" + lan + "Lng:" + lng));
                } else if (coordList.size() > 1) {

                    mMap.addMarker(new MarkerOptions()
                            .position(coordList.get(coordList.size() - 1))
                            .snippet("Lat:" + lan + "Lng:" + lng));
                }
            }
        }

        else if ((START.isEnabled() && !STOP.isEnabled())) {
            coordList.clear();
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    // Klasa realizująca odczyt zawartości z taga (poprzez funkcję HandleIntent)
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        final Button START = (Button) findViewById(R.id.PRZYCISK_START);
        final Button STOP = (Button) findViewById(R.id.PRZYCISK_STOP);


        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) { // Tag nie wspiera NDEF
                return null;
            }

            if(ndef.getCachedNdefMessage()==null){ // Tag jest pusty
                mTextView.setText("Pusty Tag");
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord); //Zwróć pobraną zawartość z taga
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }
            return null;
        }

        //Pobierz zawartość z taga
        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Format zawartości
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Kod języka
            int languageCodeLength = payload[0] & 0063;

            // Bezpośrednie pobranie zawartości
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        //Wyświelt pobraną zawartość taga z doInBackground (realizacja bezpośrednio po zakończeniu się doInBackground).
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {

                //PIERWSZA PRYMITYWNA TESTOWA WERSJA "LOGIKI" ZWIĄZANEJ ZE SPRAWDZANIEM KOLEJNOSCI ZESKANOWANIA NALEPEK NFC

                if (j < 5){
                    TABLICA2[j] = result;

                    if (TABLICA1[j].equals(result)){
                        if (j < 4) {
                            if (j > 0){
                                if (TABLICA2[j].equals(TABLICA2[j-1])){
                                    Toast.makeText(MainActivity.this, "Ta sama nalepka! Spróbój jeszcze raz!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Dobra nalepka!", Toast.LENGTH_SHORT).show();
                                    j++;
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Dobra nalepka!", Toast.LENGTH_SHORT).show();
                                j++;
                            }
                        }

                        else if (j == 4){
                            if (TABLICA2[j].equals(TABLICA2[j-1])){
                                Toast.makeText(MainActivity.this, "Ta sama nalepka! Spróbój jeszcze raz!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Dobra nalepka! Koniec trasy!", Toast.LENGTH_SHORT).show();
                                j++;
                            }
                        }

                    }
                    else {
                        if (j < 4) {
                            if (j > 0){
                                if (TABLICA2[j].equals(TABLICA2[j-1])){
                                    Toast.makeText(MainActivity.this, "Ponownie zła nalepka! Spróbój jeszcze raz!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Zła nalepka!", Toast.LENGTH_SHORT).show();
                                    j++;
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Zła nalepka!", Toast.LENGTH_SHORT).show();
                                j++;
                            }
                        }

                        else if (j == 4){
                            if (TABLICA2[j].equals(TABLICA2[j-1])){
                                Toast.makeText(MainActivity.this, "Ponownie zła nalepka! Spróbój jeszcze raz!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Zła nalepka! Koniec trasy!", Toast.LENGTH_SHORT).show();
                                j++;
                            }
                        }
                    }

                mTextView.setText("Odczytana zawartość: " + result);
                }
                else {
                    Toast.makeText(MainActivity.this, "Koniec trasy!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }



}

