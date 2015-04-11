package com.example.jt.nfcaplikacja4;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.nfc.NfcAdapter;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends ActionBarActivity {

    private NfcAdapter mNfcAdapter;
    private TextView mTextView;
    private String mUser;
    static final String STATE_USER = "user";
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Instance state", "onCreate");

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mTextView = (TextView) findViewById(R.id.textView_explanation);

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
        Toast.makeText(this, "Wykryto nalepkę!.", Toast.LENGTH_LONG).show();
        handleIntent(intent); //Odczyt zawartości taga
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
                mTextView.setText("Odczytana zawartość: " + result);

                // TU BĘDZIE ZAWARTY DALSZY KOD ZWIĄZANY Z LOGIKĄ APLIKACJI (PORÓWNYWANIE TRESCI TAGÓW ITP.)
            }
        }
    }
}