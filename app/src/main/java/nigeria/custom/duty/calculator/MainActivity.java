package nigeria.custom.duty.calculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    Button btnCalculate;
    FloatingActionButton fab;
    EditText edAmount;
    TextView tvTariff, tvBribe;
    CoordinatorLayout coordinatorLayout;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btnCalculate = findViewById(R.id.btnCalculate);
        fab = findViewById(R.id.fab);
        edAmount = findViewById(R.id.edAmount);
        tvTariff = findViewById(R.id.tvTariff);
        tvBribe = findViewById(R.id.tvBribe);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinator);


        btnCalculate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            
                calculateTariff();
                
            }
        });

        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });


        edAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                edAmount.removeTextChangedListener(this);

                try {
                    String givenstring = s.toString();
                    Long longval;
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                    }
                    longval = Long.parseLong(givenstring);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedString = formatter.format(longval);
                    edAmount.setText(formattedString);
                    edAmount.setSelection(edAmount.getText().length());
                    // to place the cursor at the end of text
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                edAmount.addTextChangedListener(this);

            }
        });



//        To calculate custom duty, your duty is calculated based on summing up surface duty (% of CIF), surcharge, administrative charge (CISS), ECOWAS Trade Liberalization Scheme (ETLS), and 5% VAT.
//
//                In your case, FOB + Freight and Insurance cost = CIF which is #4,400,000 in your case.
//
//        Surface duty @35% is 1,540,000
//        Surcharge @7% of surface is 107,800
//        ETLS @ 0.5% of CIF is 22,000
//        CISS @1% of FOB ($10,000) is 36,700
//        VAT @5% of all above is 85,325
//        Total duty payable is 1,791,825.
//
//        I hope this helps u.








    }

    public String formatText(String s) {


        try {
            String givenstring = s;
            Long longval;
            if (givenstring.contains(",")) {
                givenstring = givenstring.replaceAll(",", "");
            }
            longval = Long.parseLong(givenstring);
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String formattedString = formatter.format(longval);
            return formattedString;
            // to place the cursor at the end of text
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }

    private void calculateTariff() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (edAmount.getText().toString().isEmpty() || edAmount.getText().toString().equalsIgnoreCase("")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Amount Required", Snackbar.LENGTH_LONG);
            snackbar.show();
            edAmount.requestFocus();
            return;
        }

        String ed_Amount = edAmount.getText().toString().replaceAll(",", "");
        int Amount = Integer.valueOf(ed_Amount);
        double tariff = Amount * 0.408975;

        String calcTariff = formatText(""+ (int) tariff);

        if (calcTariff.equalsIgnoreCase("error")) {
            tvTariff.setText("#" + (int) tariff);
        } else {

            tvTariff.setText("#" + calcTariff);
        }

        int bribe1 = (int) (tariff * 0.356);
        int bribe2 = (int) (tariff * 0.678);

        tvBribe.setText("#" + formatText(""+bribe1) + "  -  #" + formatText(""+bribe2));

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
}
