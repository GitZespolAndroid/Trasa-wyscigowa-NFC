package com.example.jt.nfcaplikacja4;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import android.media.TimedText;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
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

public class MainActivity extends ActionBarActivity {

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

        // Sprawdzenie czy urządzenie wspiera NFC
        if (mNfcAdapter == null) { // Jeśli nie to wyświetl odpowiedni komunikat i zakończ aplikację
            Toast.makeText(this, "To urządzenie nie wspiera NFC. Aplikacja zostanie zamknięta.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Sprawdzenie czy w urządzeniu zostało włączone NFC
        if (!mNfcAdapter.isEnabled()) { // Jeśli nie to wyświetl odpowiedni komunikat i zakończ aplikację
            Toast.makeText(this, "Włącz NFC przed uruchomieniem aplikacji.", Toast.LENGTH_LONG).show();
            finish();

        } else { // Jeśli tak to wyświetl odpowiedni komunikat
            Toast.makeText(this, "NFC zostało włączone poprawnie.", Toast.LENGTH_LONG).show();
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

                for (m = 0; m<6 ; m++){
                    TABLICA2[m]="";
                }

                mTextView.setText("Zbliż do nalepki startowej");
                chrono.stop();
                chrono.setBase(SystemClock.elapsedRealtime());
            }
        });

    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    // Klasa realizująca odliczanie czasu:




}

