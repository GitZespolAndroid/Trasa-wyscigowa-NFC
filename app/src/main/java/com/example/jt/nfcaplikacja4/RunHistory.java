package com.example.jt.nfcaplikacja4;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class RunHistory extends Activity {


    TableLayout table_layout;
    Button build_btn;
    GridLayout lol,lol2,lol3,lol4;
    TextView tak,tak2,tak3,tak4;
    ArrayList<String> sInfo = new ArrayList<String>();
    int m = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_history);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        tak = (TextView) findViewById(R.id.textView7);
        tak2 = (TextView) findViewById(R.id.textView8);
        tak3 = (TextView) findViewById(R.id.textView9);
        tak4 = (TextView) findViewById(R.id.textView6);


        lol = (GridLayout) findViewById(R.id.Grid1);
        lol2 = (GridLayout) findViewById(R.id.Grid2);
        lol3 = (GridLayout) findViewById(R.id.Grid3);
        lol4 = (GridLayout) findViewById(R.id.Grid4);
        table_layout = (TableLayout) findViewById(R.id.Layout2);

        RebuildTable();

    }

    // WSTAWIANIE DANYCH DOTYCZĄCYCH UKONCZONEGO BIEGU (PO JEGO ZAKONCZENIU)
    private void BuildTable(String NazwaBiegu, String CzasBiegu, String Data, int DobreNalepki, int ZleNalepki, int RazemNalepek ) {

        // outer for loop
        for (int i = 1; i <= 1; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 7; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                tv.setBackgroundResource(R.drawable.cell_shape);
                tv.setPadding(5, 5, 5, 5);
                tv.setGravity(Gravity.CENTER);


                if (j == 1){tv.setMinWidth(lol.getWidth());  tv.setText(m + ".");  m = m + 1;}
                if (j == 2){tv.setMinWidth(lol2.getWidth());  tv.setText(NazwaBiegu);}
                if (j == 3){tv.setMinWidth(lol3.getWidth());  tv.setText(CzasBiegu);}
                if (j == 4){tv.setMinWidth(lol4.getWidth());  tv.setText(Data);}
                if (j == 5){tv.setMinWidth(tak.getWidth());  tv.setText(String.valueOf(DobreNalepki));}
                if (j == 6){tv.setMinWidth(tak2.getWidth());  tv.setText(String.valueOf(ZleNalepki));}
                if (j == 7){tv.setMinWidth(tak3.getWidth());  tv.setText(String.valueOf(RazemNalepek));}

                sInfo.add(tv.getText().toString());
                row.addView(tv);

            }

            table_layout.addView(row);

        }
    }

    // WCZYTYWANIE TABELI Z DOTYCHCZASOWYMI WCZESNIEJSZYMI BIEGAMI

    private void RebuildTable(){


        SharedPreferences sPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int size=sPrefs.getInt("size",0);

        for(int j=0;j<size;j++)
        {
            sInfo.add(sPrefs.getString("val"+j,null));
        }

        if (!sInfo.isEmpty()) {

            int rows2 = size / 7;
            int cols2 = -6;

            // outer for loop
            for (int i = 1; i <= rows2; i++) {

                if (cols2 != size) {
                    cols2 = cols2 + 7;
                }

                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                // inner for loop
                for (int j = cols2; j <= cols2 + 6; j++) {

                    final TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    tv.setBackgroundResource(R.drawable.cell_shape);
                    tv.setPadding(5, 5, 5, 5);
                    tv.setGravity(Gravity.CENTER);


                    if (j == cols2) {

                        ViewTreeObserver vto = lol.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                Log.d("H ", "" + lol.getHeight());
                                Log.d("W ", ""+lol.getWidth());
                                tv.setMinWidth(lol.getWidth());
                                lol.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                            }
                        });

                        tv.setText(sInfo.get(cols2-1).toString());
                        m = m + 1;
                    }
                    if (j == cols2 + 1) {

                        ViewTreeObserver vto = lol2.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                Log.d("H ", ""+lol2.getHeight());
                                Log.d("W ", ""+lol2.getWidth());
                                tv.setMinWidth(lol2.getWidth());
                                lol2.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                            }
                        });

                        tv.setText(sInfo.get(cols2).toString());
                    }
                    if (j == cols2 + 2) {

                        ViewTreeObserver vto = lol3.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                Log.d("H ", ""+lol3.getHeight());
                                Log.d("W ", ""+lol3.getWidth());
                                tv.setMinWidth(lol3.getWidth());
                                lol3.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                            }
                        });

                        tv.setText(sInfo.get(cols2+1).toString());
                    }
                    if (j == cols2 + 3) {

                        ViewTreeObserver vto = lol4.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                Log.d("H ", ""+lol4.getHeight());
                                Log.d("W ", ""+lol4.getWidth());
                                tv.setMinWidth(lol4.getWidth());
                                lol4.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                            }
                        });

                        tv.setText(sInfo.get(cols2+2).toString());
                    }
                    if (j == cols2 + 4) {

                        ViewTreeObserver vto = tak.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                Log.d("H ", ""+tak.getHeight());
                                Log.d("W ", ""+tak.getWidth());
                                tv.setMinWidth(tak.getWidth());
                                tak.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                            }
                        });

                        tv.setText(sInfo.get(cols2+3).toString());
                    }
                    if (j == cols2 + 5) {

                        ViewTreeObserver vto = tak2.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                Log.d("H ", ""+tak2.getHeight());
                                Log.d("W ", ""+tak2.getWidth());
                                tv.setMinWidth(tak2.getWidth());
                                tak2.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                            }
                        });

                        tv.setText(sInfo.get(cols2+4).toString());
                    }
                    if (j == cols2 + 6) {

                        ViewTreeObserver vto = tak3.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                Log.d("H ", ""+tak3.getHeight());
                                Log.d("W ", ""+tak3.getWidth());
                                tv.setMinWidth(tak3.getWidth());
                                tak3.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                            }
                        });

                        tv.setText(sInfo.get(cols2+5).toString());
                    }

                    row.addView(tv);
                }
                table_layout.addView(row);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_run_history, menu);
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
}
