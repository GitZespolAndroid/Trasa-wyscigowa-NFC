package com.example.jt.nfcaplikacja4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.TimedText;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.nfc.NfcAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Text;



public class MainActivity extends ActionBarActivity implements LocationListener, NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NfcAdapter mNfcAdapter;
    private TextView mTextView;
    private TextView mCzas;
    private String mUser;
    static final String STATE_USER = "user";
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    int i = 0;

    int j,m,z,v = 0;
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

    Button buttonOpenDialog;
    Button buttonUp;
    TextView textFolder, timerValue;
    ImageView image;
    int o,b,zlenalepki,dobrenalepki = 0;
    float distance;

    String KEY_TEXTPSS = "TEXTPSS";
    static final int CUSTOM_DIALOG_ID = 0;

    ListView dialog_ListView;

    File root;
    File curFolder;

    private List<String> fileList = new ArrayList<String>();
    private List<String> fileList2 = new ArrayList<String>();

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    Intent intent3;

    Circle circle = null;

    ArrayList<String> NumerNalepki = new ArrayList<String>();
    ArrayList<String> Wspolrzedna1 = new ArrayList<String>();
    ArrayList<String> Wspolrzedna2 = new ArrayList<String>();
    ArrayList<String> mmm7 = new ArrayList<String>();
    ArrayList<String> Lista = new ArrayList<String>();
    ArrayList<String> KoloryKolek = new ArrayList<String>();
    ArrayList<String> InformacjeOMiejscu = new ArrayList<String>();
    List<Circle> Circles = new ArrayList<Circle>();


    // ZMIENNE ZWIAZANE Z PRZYCISKAMI SA DANE ODREBNIE W KAZDEJ Z FUNKCJI I KLAS, PONIEWAZ W INNYM PRZYPADKU APLIKACJA "WYKRZACZA SIE"

    /**
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Instance state", "onCreate");

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        final Button START = (Button) findViewById(R.id.PRZYCISK_START);
        final Button STOP = (Button) findViewById(R.id.PRZYCISK_STOP);

        TextView PREDKOSC = (TextView) findViewById(R.id.textView32);
        TextView WYSOKOSCTERENU = (TextView) findViewById(R.id.textView29);
        timerValue = (TextView) findViewById(R.id.textView34);
        LocationManager locationManager;

        image = (ImageView) findViewById(R.id.image);

        buttonOpenDialog = (Button) findViewById(R.id.button);

        root = new File("/");
        curFolder = root;

        ArrayList<String> Czasybiegow = new ArrayList<String>();
        ArrayList<String> Czasybiegow2 = new ArrayList<String>();
        ArrayList<String> Nazwybiegow = new ArrayList<String>();
        ArrayList<String> Datybiegow = new ArrayList<String>();
        ArrayList<String> Dobrenalepkii = new ArrayList<String>();
        ArrayList<String> Zlenalepkii = new ArrayList<String>();
        ArrayList<String> Razemnalepeki = new ArrayList<String>();

        // --------------------- INFORMACJE O AKTUALNYM MIEJSCU ----------------------------------

        Intent intent999 = new Intent(MainActivity.this,PlaceInformation.class);

        if (InformacjeOMiejscu.size()==7 && b==1) {

            intent999.putExtra("Nazwamiejsca", InformacjeOMiejscu.get(1));
            intent999.putExtra("Wspolrzedne", InformacjeOMiejscu.get(2));
            intent999.putExtra("Adres", InformacjeOMiejscu.get(3));
            intent999.putExtra("AdresWWW", InformacjeOMiejscu.get(4));
            intent999.putExtra("ZdjecieAdresWWW", InformacjeOMiejscu.get(5));
            intent999.putExtra("OpisMiejsca", InformacjeOMiejscu.get(6));
        }




        //------------------------OBSLUGA HIGHSCORA-----------------------------------------------

        SharedPreferences sPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int size=sPrefs.getInt("size",0);
        int cv = 0;
        LayoutInflater inf22 = LayoutInflater.from(this);
        final View view5 = inf22.inflate(R.layout.activity_highscore, null);

        if(size!=0) {

            for (int j = 2; j <= size - 5; j = j + 7) {
                Czasybiegow.add(sPrefs.getString("val" + j, null));
                Czasybiegow2.add(sPrefs.getString("val" + j, null));
            }

            for (int j = 1; j <= size - 6; j = j + 7) {
                Nazwybiegow.add(sPrefs.getString("val" + j, null));
            }

            for (int j = 3; j <= size - 4; j = j + 7) {
                Datybiegow.add(sPrefs.getString("val" + j, null));
            }

            for (int j = 4; j <= size - 3; j = j + 7) {
                Dobrenalepkii.add(sPrefs.getString("val" + j, null));
            }

            for (int j = 5; j <= size - 2; j = j + 7) {
                Zlenalepkii.add(sPrefs.getString("val" + j, null));
            }

            for (int j = 6; j <= size - 1; j = j + 7) {
                Razemnalepeki.add(sPrefs.getString("val" + j, null));
            }

            Collections.sort(Czasybiegow);

            for (int i = 0; i < Czasybiegow.size(); i++) {

                if (Czasybiegow.get(0).equals(Czasybiegow2.get(i))) {

                    cv = i;
                    break;

                }

            }

            final TextView Czasbiegu = (TextView) view5.findViewById(R.id.textView17);
            final TextView Nazwabiegu = (TextView) view5.findViewById(R.id.textView5);
            final TextView Databiegu = (TextView) view5.findViewById(R.id.textView3);
            final TextView Lacznienalepek = (TextView) view5.findViewById(R.id.textView4);
            final TextView Dobreizlenalepki = (TextView) view5.findViewById(R.id.textView16);


            Czasbiegu.setText(Czasybiegow.get(0));
            Nazwabiegu.setText("Był to bieg o nazwie:  " + Nazwybiegow.get(cv));
            Databiegu.setText("Dnia:  " + Datybiegow.get(cv));
            Lacznienalepek.setText("Zeskanowałeś łącznie:  " + Razemnalepeki.get(cv) + " nalepek");
            Dobreizlenalepki.setText("z czego:  " + Dobrenalepkii.get(cv) + "dobrze, a  " + Zlenalepkii.get(cv) + " źle");

        }

        //------------------------OBIEKTY DO MAPY------------------------------------------------

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


        //-------------------OBIEKTY DO ROZWIJANEGO PO LEWO MENU-------------------------------

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //-------------------------------------------------------------------------------------


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

        if (mNfcAdapter!=null && str.size()>0) {

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
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) && mNfcAdapter.isEnabled()) { // Jeśli nie to wyświetl odpowiedni komunikat i zakończ aplikację
                Toast.makeText(this, "NFC i GPS zostały włączone poprawnie!", Toast.LENGTH_LONG).show();
            }
        }


        //OBSLUGA PRZYCISKU START
        START.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                START.setEnabled(false);
                STOP.setEnabled(true);
                buttonOpenDialog .setEnabled(false);
                distance = 0;
                z = 0;
                Lista.clear();
                coordList.clear();
                KoloryKolek.clear();

                TextView Nalepki = (TextView) findViewById(R.id.textView33);
                Nalepki.setText("0/"+String.valueOf(NumerNalepki.size()));
                mMap.clear();

                for (int i = 0; i<NumerNalepki.size(); i++){

                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.valueOf(Wspolrzedna1.get(i)), Double.valueOf(Wspolrzedna2.get(i))))
                            .radius(20)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.GRAY));

                    KoloryKolek.add("0");
                }

                LatLng currentPosition = new LatLng(lan, lng);
                mMap.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .snippet("Lat:" + lan + "Lng:" + lng));

            }
        });

        //OBSLUGA PRZYCISKU STOP
        STOP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                START.setEnabled(true);
                STOP.setEnabled(false);
                buttonOpenDialog .setEnabled(true);

                Lista.clear();
                dobrenalepki = 0;
                zlenalepki = 0;
                updatedTime=0;
                timeSwapBuff = 0;
                timeInMilliseconds = 0;
                customHandler.removeCallbacks(updateTimerThread);
                timerValue.setText("000:00:00:000");
                distance = 0;
                TextView Dystans = (TextView) findViewById(R.id.textView27);
                Dystans.setText(String.valueOf("0 m"));
                b = 0;
                j = 0;
                TextView PREDKOSC = (TextView) findViewById(R.id.textView32);
                TextView WYSOKOSCTERENU = (TextView) findViewById(R.id.textView29);
                PREDKOSC.setText(String.valueOf("0 km/h"));
                WYSOKOSCTERENU.setText(String.valueOf("-"));

            }
        });

        //OBLUGA PRZYCISKU "WCZYTAJ TRASE"

        buttonOpenDialog = (Button) findViewById(R.id.button);
        buttonOpenDialog.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog(CUSTOM_DIALOG_ID);
                coordList.clear();
            }
        });

    }

    //--------MENU ROZWIJANE PO LEWO ---------------------------------------------------------------

    /**
     *
     * @param position
     */

    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position){

            case 1 :

                Intent intent = new Intent(MainActivity.this,PlaceInformation.class);

                if (InformacjeOMiejscu.size()!=0 && InformacjeOMiejscu.size()==7 && b==1) {

                    intent.putExtra("Nazwamiejsca", InformacjeOMiejscu.get(1));
                    intent.putExtra("Wspolrzedne", InformacjeOMiejscu.get(2));
                    intent.putExtra("Adres", InformacjeOMiejscu.get(3));
                    intent.putExtra("AdresWWW", InformacjeOMiejscu.get(4));
                    intent.putExtra("ZdjecieAdresWWW", InformacjeOMiejscu.get(5));
                    intent.putExtra("OpisMiejsca", InformacjeOMiejscu.get(6));
                }

                startActivity(intent);

                break;


            case 2:

                Intent intent2 = new Intent(MainActivity.this,RunHistory.class);

                intent2.putExtra("ID","1");


                startActivity(intent2);

                break;

            case 3:

                Highscore();

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
                .replace(R.id.container, PlaceholderFragment.newInstance(position))
                .commit();

    }

    /**
     *
     * @param number
     */

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

    /**
     *
     * @param menu
     * @return
     */

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

    /**
     *
     * @param intent
     */

    @Override
    protected void onNewIntent(Intent intent) {

        final Button START = (Button) findViewById(R.id.PRZYCISK_START);
        final Button STOP = (Button) findViewById(R.id.PRZYCISK_STOP);

        if (!START.isEnabled()) {
            handleIntent(intent); //Odczyt zawartości taga
        }
    }

    // Odczyt zawartości taga

    /**
     *
     * @param intent
     */

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

    /**
     *
     * @param activity
     * @param adapter
     */

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

    /**
     *
     * @param activity
     * @param adapter
     */

    public static void stopForegroundDispatch(final ActionBarActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }



    // RYSOWANIE NA MAPIE GDY JEST ZALADOWANA

    /**
     *
     * @param location
     */

    @Override
    public void onLocationChanged(Location location) {

        int t = 0;
        LatLng currentPosition = new LatLng(lan, lng);

        if (mMap != null)
        {
            drawMarker(location);
        }
    }

    // OBSLUGA MAPY (RYSOWANIE KURSORA I SLADU BIEGU NA MAPIE)

    /**
     *
     * @param location
     */

    private void drawMarker(Location location) {

        lan = Math.round(location.getLatitude() * 100000d) / 100000d;
        lng = Math.round(location.getLongitude() * 100000d) / 100000d;
        int p = 0;
        final Button START = (Button) findViewById(R.id.PRZYCISK_START);
        final Button STOP = (Button) findViewById(R.id.PRZYCISK_STOP);

//  convert the location object to a LatLng object that can be used by the map API
        LatLng currentPosition = new LatLng(lan, lng);
        polylineOptions = new PolylineOptions().width(10).color(Color.RED);
        TextView Dystans = (TextView) findViewById(R.id.textView27);
        Location crntLocation = new Location("crntlocation");
        Location newLocation = new Location("newlocation");

        TextView PREDKOSC = (TextView) findViewById(R.id.textView32);
        TextView WYSOKOSCTERENU = (TextView) findViewById(R.id.textView29);

// add a marker to the map indicating our current position

        if ((!START.isEnabled() && STOP.isEnabled())) {

            mMap.clear();

            if (b==0) {

                mMap.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .snippet("Lat:" + lan + "Lng:" + lng));

            }

            for (int i = 0; i<NumerNalepki.size(); i++){

                if (KoloryKolek.get(i).equals("0")){

                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.valueOf(Wspolrzedna1.get(i)), Double.valueOf(Wspolrzedna2.get(i))))
                            .radius(20)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.GRAY));

                }

                else if (KoloryKolek.get(i).equals("1")){

                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.valueOf(Wspolrzedna1.get(i)), Double.valueOf(Wspolrzedna2.get(i))))
                            .radius(20)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.GREEN));

                }

                else if (KoloryKolek.get(i).equals("2")){

                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.valueOf(Wspolrzedna1.get(i)), Double.valueOf(Wspolrzedna2.get(i))))
                            .radius(20)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.RED));

                }

            }

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16));
            PREDKOSC.setText(String.valueOf(Math.round(location.getSpeed() * 1000d) / 1000d + " km/h"));
            WYSOKOSCTERENU.setText(String.valueOf(location.getAltitude() + " m.n.p.m"));

            if (b == 1) {

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

                        for (int l = 0; l < coordList.size() - 1; l++) {

                            crntLocation.setLatitude(coordList.get(l).latitude);
                            crntLocation.setLongitude(coordList.get(l).longitude);

                            newLocation.setLatitude(coordList.get(l + 1).latitude);
                            newLocation.setLongitude(coordList.get(l + 1).longitude);

                            if (l == 0) {
                                distance = crntLocation.distanceTo(newLocation);
                            } else {
                                distance = distance + crntLocation.distanceTo(newLocation);
                            }
                        }

                        if (distance < 1000) {
                            Dystans.setText(String.valueOf(Math.round(distance * 100d) / 100d) + " m");
                        } else if (distance > 1000 && distance != 0) {
                            Dystans.setText(String.valueOf(Math.round((distance / 1000) * 1000d) / 1000d) + " km");
                        }

                    } else if (coordList.size() >= 2) {

                        coordList.set(1, coordList.get(0));
                        mMap.addPolyline(polylineOptions.addAll(coordList));
                        mMap.addMarker(new MarkerOptions()
                                .position(coordList.get(coordList.size() - 1))
                                .snippet("Lat:" + lan + "Lng:" + lng));

                        for (int l = 0; l < coordList.size() - 1; l++) {

                            crntLocation.setLatitude(coordList.get(l).latitude);
                            crntLocation.setLongitude(coordList.get(l).longitude);

                            newLocation.setLatitude(coordList.get(l + 1).latitude);
                            newLocation.setLongitude(coordList.get(l + 1).longitude);

                            if (l == 0) {
                                distance = crntLocation.distanceTo(newLocation);
                            } else {
                                distance = distance + crntLocation.distanceTo(newLocation);
                            }
                        }

                        if (distance < 1000) {
                            Dystans.setText(String.valueOf(Math.round(distance * 100d) / 100d) + " m");
                        } else if (distance > 1000 && distance != 0) {
                            Dystans.setText(String.valueOf(Math.round((distance / 1000) * 1000d) / 1000d) + " km");
                        }

                    }

                    else if (coordList.size() == 1) {

                        mMap.addMarker(new MarkerOptions()
                                .position(currentPosition)
                                .snippet("Lat:" + lan + "Lng:" + lng));

                        if (distance < 1000) {
                            Dystans.setText(String.valueOf(Math.round(distance * 100d) / 100d) + " m");
                        } else if (distance > 1000 && distance != 0) {
                            Dystans.setText(String.valueOf(Math.round((distance / 1000) * 1000d) / 1000d) + " km");
                        }


                    } else if (coordList.size() > 1) {

                        mMap.addMarker(new MarkerOptions()
                                .position(coordList.get(coordList.size() - 1))
                                .snippet("Lat:" + lan + "Lng:" + lng));

                        if (distance < 1000) {
                            Dystans.setText(String.valueOf(Math.round(distance * 100d) / 100d) + " m");
                        } else if (distance > 1000 && distance != 0) {
                            Dystans.setText(String.valueOf(Math.round((distance / 1000) * 1000d) / 1000d) + " km");
                        }
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
        }

        else if ((START.isEnabled() && !STOP.isEnabled())) {
            b = 0;

            mMap.clear();

            for (int i = 0; i<NumerNalepki.size(); i++){

                if (KoloryKolek.get(i).equals("0")){

                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.valueOf(Wspolrzedna1.get(i)), Double.valueOf(Wspolrzedna2.get(i))))
                            .radius(20)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.GRAY));

                }

                else if (KoloryKolek.get(i).equals("1")){

                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.valueOf(Wspolrzedna1.get(i)), Double.valueOf(Wspolrzedna2.get(i))))
                            .radius(20)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.GREEN));

                }

                else if (KoloryKolek.get(i).equals("2")){

                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.valueOf(Wspolrzedna1.get(i)), Double.valueOf(Wspolrzedna2.get(i))))
                            .radius(20)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.RED));

                }

            }

            mMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .snippet("Lat:" + lan + "Lng:" + lng));

            mMap.addPolyline(polylineOptions.addAll(coordList));
        }
    }


    /**
     *
     * @param provider
     * @param status
     * @param extras
     */

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     *
     * @param provider
     */

    @Override
    public void onProviderEnabled(String provider) {

    }


    /**
     *
     * @param provider
     */

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

        /**
         *
         * @param record
         * @return
         * @throws UnsupportedEncodingException
         */

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


        /**
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            String[] arr;

            InformacjeOMiejscu.clear();
            LatLng currentPosition = new LatLng(lan, lng);

            arr = result.split(",");

            for (int i = 0; i<arr.length; i++){

                InformacjeOMiejscu.add(arr[i]);

            }

            if (arr.length ==0){

                Toast.makeText(MainActivity.this, "Nalepka jest pusta!", Toast.LENGTH_LONG).show();

            }

            if (arr.length<7){

                Toast.makeText(MainActivity.this, "Nalepka zawiera za mało danych!", Toast.LENGTH_LONG).show();

            }

            if (arr.length>7){

                Toast.makeText(MainActivity.this, "Nalepka zawiera za dużo danych!", Toast.LENGTH_LONG).show();

            }

            if (arr.length == 7 && !START.isEnabled()==true && !STOP.isEnabled()==false) {


                if (Math.abs(Double.valueOf(Wspolrzedna1.get(j))-currentPosition.latitude)<=0.01 && Math.abs(Double.valueOf(Wspolrzedna2.get(j))-currentPosition.longitude)<=0.01){

                    b = 1;

                }

                //PIERWSZA PRYMITYWNA TESTOWA WERSJA "LOGIKI" ZWIĄZANEJ ZE SPRAWDZANIEM KOLEJNOSCI ZESKANOWANIA NALEPEK NFC

                if (j < NumerNalepki.size()){

                    Lista.add(arr[0]);

                    if (NumerNalepki.get(j).equals(arr[0]) && Math.abs(Double.valueOf(Wspolrzedna1.get(j))-currentPosition.latitude)<=0.01 && Math.abs(Double.valueOf(Wspolrzedna2.get(j))-currentPosition.longitude)<=0.01){
                        if (j < NumerNalepki.size()-1) {
                            if (j > 0){
                                if (Lista.get(j).equals(Lista.get(j-1))){
                                    Toast.makeText(MainActivity.this, "Ta sama nalepka! Spróbój jeszcze raz!", Toast.LENGTH_SHORT).show();

                                    Lista.remove(j);

                                }
                                else  {
                                    Toast.makeText(MainActivity.this, "Dobra nalepka!", Toast.LENGTH_SHORT).show();

                                    KoloryKolek.set(j,"1");

                                    TextView Nalepki = (TextView) findViewById(R.id.textView33);
                                    Nalepki.setText(String.valueOf(j+1)+"/"+String.valueOf(NumerNalepki.size()));
                                    Log.i("Instance state", "Zielone");

                                    mMap.addCircle(new CircleOptions()
                                            .center(new LatLng(Double.valueOf(Wspolrzedna1.get(j)), Double.valueOf(Wspolrzedna2.get(j))))
                                            .radius(20)
                                            .strokeColor(Color.BLACK)
                                            .fillColor(Color.GREEN));


                                    j++;
                                    dobrenalepki++;
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Dobra nalepka!", Toast.LENGTH_SHORT).show();

                                KoloryKolek.set(j,"1");

                                TextView Nalepki = (TextView) findViewById(R.id.textView33);
                                Nalepki.setText(String.valueOf(j+1)+"/"+String.valueOf(NumerNalepki.size()));
                                Log.i("Instance state", "Zielone");

                                mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(Double.valueOf(Wspolrzedna1.get(j)), Double.valueOf(Wspolrzedna2.get(j))))
                                        .radius(20)
                                        .strokeColor(Color.BLACK)
                                        .fillColor(Color.GREEN));


                                j++;
                                dobrenalepki++;
                                startTime = SystemClock.uptimeMillis();
                                customHandler.postDelayed(updateTimerThread, 0);
                            }
                        }

                        else if (j == NumerNalepki.size()-1){
                            if (Lista.get(j).equals(Lista.get(j - 1))){
                                Toast.makeText(MainActivity.this, "Ta sama nalepka! Spróbój jeszcze raz!", Toast.LENGTH_SHORT).show();
                                Lista.remove(j);
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Dobra nalepka! Koniec trasy!", Toast.LENGTH_SHORT).show();

                                KoloryKolek.set(j,"1");

                                Log.i("Instance state", "Zielone");

                                TextView Nalepki = (TextView) findViewById(R.id.textView33);
                                Nalepki.setText(String.valueOf(j+1)+"/"+String.valueOf(NumerNalepki.size()));

                                mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(Double.valueOf(Wspolrzedna1.get(j)), Double.valueOf(Wspolrzedna2.get(j))))
                                        .radius(20)
                                        .strokeColor(Color.BLACK)
                                        .fillColor(Color.GREEN));


                                j++;
                                dobrenalepki++;
                                EndOfRun();
                            }
                        }
                    }
                    else if (!NumerNalepki.get(j).equals(arr[0]) && Math.abs(Double.valueOf(Wspolrzedna1.get(j))-currentPosition.latitude)<=0.01 && Math.abs(Double.valueOf(Wspolrzedna2.get(j))-currentPosition.longitude)<=0.01) {
                        if (j < NumerNalepki.size()-1) {
                            if (j > 0){
                                if (Lista.get(j).equals(Lista.get(j-1))){
                                    Toast.makeText(MainActivity.this, "Ta sama nalepka! Spróbój jeszcze raz!", Toast.LENGTH_SHORT).show();
                                    Lista.remove(j);
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Zła nalepka!", Toast.LENGTH_SHORT).show();

                                    KoloryKolek.set(Integer.valueOf(result)-1,"2");

                                    TextView Nalepki = (TextView) findViewById(R.id.textView33);
                                    Nalepki.setText(String.valueOf(j+1)+"/"+String.valueOf(NumerNalepki.size()));
                                    Log.i("Instance state", "Czerwone");

                                    mMap.addCircle(new CircleOptions()
                                            .center(new LatLng(Double.valueOf(Wspolrzedna1.get(Integer.valueOf(result)-1)), Double.valueOf(Wspolrzedna2.get(Integer.valueOf(result)-1))))
                                            .radius(20)
                                            .strokeColor(Color.BLACK)
                                            .fillColor(Color.RED));

                                    j++;
                                    zlenalepki++;
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Zła nalepka!", Toast.LENGTH_SHORT).show();

                                KoloryKolek.set(Integer.valueOf(result)-1,"2");

                                TextView Nalepki = (TextView) findViewById(R.id.textView33);
                                Nalepki.setText(String.valueOf(j+1)+"/"+String.valueOf(NumerNalepki.size()));
                                Log.i("Instance state", "Czerwone");

                                mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(Double.valueOf(Wspolrzedna1.get(Integer.valueOf(result)-1)), Double.valueOf(Wspolrzedna2.get(Integer.valueOf(result)-1))))
                                        .radius(20)
                                        .strokeColor(Color.BLACK)
                                        .fillColor(Color.RED));

                                j++;
                                zlenalepki++;
                                startTime = SystemClock.uptimeMillis();
                                customHandler.postDelayed(updateTimerThread, 0);
                            }
                        }

                        else if (j == NumerNalepki.size()-1){
                            if (Lista.get(j).equals(Lista.get(j-1))){
                                Toast.makeText(MainActivity.this, "Ta sama nalepka! Spróbój jeszcze raz!", Toast.LENGTH_SHORT).show();
                                Lista.remove(j);
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Zła nalepka! Koniec trasy!", Toast.LENGTH_SHORT).show();


                                KoloryKolek.set(Integer.valueOf(result)-1,"2");

                                mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(Double.valueOf(Wspolrzedna1.get(Integer.valueOf(result)-1)), Double.valueOf(Wspolrzedna2.get(Integer.valueOf(result)-1))))
                                        .radius(20)
                                        .strokeColor(Color.BLACK)
                                        .fillColor(Color.RED));

                                TextView Nalepki = (TextView) findViewById(R.id.textView33);
                                Nalepki.setText(String.valueOf(j+1)+"/"+String.valueOf(NumerNalepki.size()));
                                Log.i("Instance state", "Czerwone");

                                j++;
                                zlenalepki++;
                                EndOfRun();
                            }
                        }
                    }
                }
                else {
                    TextView Nalepki = (TextView) findViewById(R.id.textView33);
                    Nalepki.setText(String.valueOf(j+1)+"/"+String.valueOf(NumerNalepki.size()));
                    EndOfRun();
                }
            }
        }
    }

    // WYSWIETLANIE OKIENKA Z ZAPISEM BIEGU DO HISTORII (PO SKONCZONYM BIEGU) I OBSLUGA PRZYCISKOW ORAZ STOPERA PO ZAKONCZENIU BIEGU


    public void EndOfRun(){

        LayoutInflater inf = LayoutInflater.from(this);
        final View view = inf.inflate(R.layout.activity_end_of_run, null);
        final EditText edt = (EditText)view.findViewById(R.id.editText);
        edt.setCursorVisible(false);
        edt.selectAll();
        distance = 0;

        final Button START = (Button) findViewById(R.id.PRZYCISK_START);
        final Button STOP = (Button) findViewById(R.id.PRZYCISK_STOP);
        TextView PREDKOSC = (TextView) findViewById(R.id.textView32);
        TextView WYSOKOSCTERENU = (TextView) findViewById(R.id.textView29);
        timerValue = (TextView) findViewById(R.id.textView34);

        STOP.setEnabled(false);
        START.setEnabled(true);
        buttonOpenDialog.setEnabled(true);

        updatedTime=0;
        timeSwapBuff = 0;
        timeInMilliseconds = 0;
        customHandler.removeCallbacks(updateTimerThread);

        final TextView Czasbiegu =(TextView)view.findViewById(R.id.textView2);

        final TextView Zlenalepki = (TextView) view.findViewById(R.id.textView3);
        final TextView Dobrenalepki = (TextView) view.findViewById(R.id.textView4);
        final EditText Nazwabiegu = (EditText) view.findViewById(R.id.editText);

        Czasbiegu.setText("Twój czas wynosi:  "+ timerValue.getText());
        Zlenalepki.setText("Źle zeskanowane nalepki:  "+zlenalepki);
        Dobrenalepki.setText("Dobrze zeskanowane nalepki:  "+dobrenalepki);

        timerValue.setText("000:00:00:000");
        dobrenalepki = 0;
        zlenalepki = 0;
        j = 0;
        z = 0;
        PREDKOSC.setText(String.valueOf("0 km/h"));
        WYSOKOSCTERENU.setText(String.valueOf("-"));


        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt.setCursorVisible(true);
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Zapisz wynik do historii!")
                .setView(view)
                .setNeutralButton("PUBLIKUJ WYNIK NA FB", new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("ZATWIERDŹ",  new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intentRunHistory = new Intent(MainActivity.this,RunHistory.class);
                        intentRunHistory.putExtra("ID","2");
                        intentRunHistory.putExtra("Nazwabiegu",Nazwabiegu.getText());
                        intentRunHistory.putExtra("Czasbiegu",Czasbiegu.getText());
                        intentRunHistory.putExtra("Dobrenalepki",Dobrenalepki.getText());
                        intentRunHistory.putExtra("Zlenalepki",Zlenalepki.getText());
                        intentRunHistory.putExtra("Razemnalepek",String.valueOf(NumerNalepki.size()));
                        startActivity(intentRunHistory);

                    }
                })
                .show();
    }

    // WYSWIETLANIE OKIENKA Z NAJLEPSZYM CZASEM BIEGU

    public void Highscore(){

        LayoutInflater inf = LayoutInflater.from(this);
        final View view = inf.inflate(R.layout.activity_highscore, null);

        new AlertDialog.Builder(this)
                .setTitle("Najlepszy wynik")
                .setView(view)
                .setPositiveButton("OK", new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton("PUBLIKUJ WYNIK NA FB", new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    // ------------------------ KLASA DO MENU ROZWIJANEGO PO LEWO ---------------------------------

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

        /**
         *
         */

        public PlaceholderFragment() {
        }

        /**
         *
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return
         */

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        /**
         *
         * @param activity
         */

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    //----------------------OTWIERANIE PLIKU Z TRASĄ I WCZYTYWANIE----------------------------------

    /**
     *
     * @param id
     * @return
     */

    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id) {
            case CUSTOM_DIALOG_ID:

                dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Otwórz plik z trasą:");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);


                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonUp.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        ListDir(curFolder.getParentFile());
                    }
                });

                //Prepare ListView in dialog
                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist);

                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        File selected = new File(fileList.get(position));
                        if (selected.isDirectory()) {
                            ListDir(selected);
                        } else {

                            dismissDialog(CUSTOM_DIALOG_ID);

                            // ODCZYT PLIKU TXT

                            NumerNalepki.clear();
                            Wspolrzedna1.clear();
                            Wspolrzedna2.clear();
                            mmm7.clear();

                            odczytTxt(selected);
                        }

                    }
                });

                break;
        }

        return dialog;
    }

    /**
     *
     * @param id
     * @param dialog
     * @param bundle
     */

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
        // TODO Auto-generated method stub
        super.onPrepareDialog(id, dialog, bundle);

        switch (id) {
            case CUSTOM_DIALOG_ID:
                ListDir(curFolder);
                break;
        }

    }

    /**
     *
     * @param f
     */

    void ListDir(File f) {

        if (f.equals(root)) {
            buttonUp.setEnabled(false);
            o = 1;
        } else {
            buttonUp.setEnabled(true);
            o = 0;
        }

        curFolder = f;
        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();
        fileList2.clear();


        if (files == null){

            ArrayAdapter<String> directoryList
                    = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, fileList2);
            dialog_ListView.setAdapter(directoryList);
        }

        else {

            Arrays.sort(files, filecomparator);

            for (int i=0; i < files.length; i++) {

                File file = files[i];

                if (file.isDirectory()) {

                    fileList.add(file.getPath());
                    fileList2.add(file.getName());


                } else {

                    Uri selectedUri = Uri.fromFile(file);
                    String fileExtension
                            = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
                    if (fileExtension.equalsIgnoreCase("txt")) {
                        fileList.add(file.getPath());
                        fileList2.add(file.getName());
                    }
                }
            }

            ArrayAdapter<String> directoryList
                    = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, fileList2);

            dialog_ListView.setAdapter(directoryList);

        }
    }


    Comparator<? super File> filecomparator = new Comparator<File>(){

        public int compare(File file1, File file2) {

            if(file1.isDirectory()){
                if (file2.isDirectory()){
                    return String.valueOf(file1.getName().toLowerCase()).compareTo(file2.getName().toLowerCase());
                }else{
                    return -1;
                }
            }else {
                if (file2.isDirectory()){
                    return 1;
                }else{
                    return String.valueOf(file1.getName().toLowerCase()).compareTo(file2.getName().toLowerCase());
                }
            }

        }
    };

    //------------------------FUNKCJA REALIZUJĄCA STOPER:-------------------------------------------

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText(String.format("%03d", hours) + ":" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };

    //--------------------- FUNKCJA REALIZUJĄCA ODCZYT Z PLIKU TXT----------------------------------

    /**
     *
     * @param plik
     */
    private void odczytTxt (File plik){

        String[] arr, arr2;
        int p = 0;
        int j = 0;
        int s = 0;


        try {
            BufferedReader br = new BufferedReader(new FileReader(plik));
            String line;

            while ((line = br.readLine()) != null) {

                if(line.equals("/NFCWYSCIGI/")){j = 1;}

                else if (!line.equals("/latlen/") && p == 0 && j == 1) {
                    arr = line.split(",");

                    if (arr.length == 3) {

                        if (arr[0].matches(("[+]?\\d+")) && (arr[1].matches(("[+]?\\d+(\\.\\d+)?")) || arr[1].matches(("[+]?\\d+"))) && (arr[2].matches(("[+]?\\d+(\\.\\d+)?")) || arr[2].matches(("[+]?\\d+")))) {
                            NumerNalepki.add(arr[0]);
                            Wspolrzedna1.add(arr[1]);
                            Wspolrzedna2.add(arr[2]);
                            s = 1;
                        }

                        else {
                            Toast.makeText(this, "Plik zawiera niepoprawne dane! Popraw go przed wczytaniem!", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }

                    else if (arr.length <3) {
                        Toast.makeText(this, "Plik zawiera niekompletne dane! Popraw go przed wczytaniem!", Toast.LENGTH_LONG).show();
                        break;
                    }

                    else if (arr.length >3) {
                        Toast.makeText(this, "Plik zawiera niepoprawną ilość danych! Popraw go przed wczytaniem!", Toast.LENGTH_LONG).show();
                        break;
                    }

                }

                else if (line.equals("/latlen/") && j == 1){
                    p = 1;
                }

                else if (!line.equals("/latlen/") && p == 1 && j == 1){
                    arr2 = line.split(",");

                    if (arr2.length == 3) {

                        if ((arr2[0].matches(("[+]?\\d+(\\.\\d+)?")) || arr2[0].matches(("[+]?\\d+"))) && (arr2[1].matches(("[+]?\\d+(\\.\\d+)?")) || arr2[1].matches(("[+]?\\d+"))) && arr2[2].matches(("[+]?\\d+"))) {
                            mmm7.add(arr2[0]);
                            mmm7.add(arr2[1]);
                            mmm7.add(arr2[2]);
                        } else {
                            Toast.makeText(this, "Plik zawiera niepoprawne dane! Popraw go przed wczytaniem!", Toast.LENGTH_LONG).show();
                            break;
                        }

                    }


                    else if (arr2.length <3) {
                        Toast.makeText(this, "Plik zawiera niekompletne dane! Popraw go przed wczytaniem!", Toast.LENGTH_LONG).show();
                        break;
                    }

                    else if (arr2.length >3) {
                        Toast.makeText(this, "Plik zawiera niepoprawną ilość danych! Popraw go przed wczytaniem!", Toast.LENGTH_LONG).show();
                        break;
                    }

                }

                else if (p == 0 && s == 0){

                    Toast.makeText(this, "Plik zawiera niekompletne dane! Popraw go przed wczytaniem!", Toast.LENGTH_LONG).show();
                    break;

                }
            }
            br.close();

            if (j==0){Toast.makeText(this, "Plik nie zawiera trasy wyścigowej lub zawiera niekompletne dane! Popraw go przed wczytaniem!", Toast.LENGTH_LONG).show();}

            if (!NumerNalepki.isEmpty() && !Wspolrzedna1.isEmpty() && !Wspolrzedna2.isEmpty()  && !mmm7.isEmpty()){

                Toast.makeText(this, "Załadowano plik poprawnie!", Toast.LENGTH_LONG).show();
                final Button START = (Button) findViewById(R.id.PRZYCISK_START);
                START.setEnabled(true);
                TworzTrase();

            }

        }
        catch (IOException e) {
            //You'll need to add proper error handling here

            Toast.makeText(this, "Nie można odczytać pliku!", Toast.LENGTH_LONG).show();

        }
    }

    // Przygotowywanie mapy pod trasę

    private void TworzTrase () {

        LatLng coordinate = new LatLng(Double.valueOf(mmm7.get(0)), Double.valueOf(mmm7.get(1)));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, Float.valueOf(mmm7.get(2)));
        mMap.animateCamera(yourLocation);

        TextView Nalepki = (TextView) findViewById(R.id.textView33);


        Nalepki.setText("0/" + String.valueOf(NumerNalepki.size()));

        mMap.clear();

        for (int u = 0; u < NumerNalepki.size(); u++) {

            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(Double.valueOf(Wspolrzedna1.get(u)), Double.valueOf(Wspolrzedna2.get(u))))
                    .radius(20)
                    .strokeColor(Color.BLACK)
                    .fillColor(Color.GRAY));

            KoloryKolek.add("0");

        }
    }
}