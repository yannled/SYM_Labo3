package ch.heigvd.sym.template;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

/**
 * Autheur: Yann Lederrey, Joel Schar, Yohann Meyer
 */

//Source : https://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278
public class nfc extends Activity implements nfcMethod{

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private EditText username;
    private EditText password;
    private Button buttonConnect;
    private ProgressBar progressBarNFC;
    private Long tagTime = null;
    private CountDownTimer countDownTime;
    private NdefReaderTask nfctask;
    private int total = 0;
    private static final long MAX_MINUTES = 1000 * 60 * 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        nfctask = new NdefReaderTask(this);

        mTextView = findViewById(R.id.nfcvalue);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        buttonConnect = findViewById(R.id.buttonConnect);
        progressBarNFC = findViewById(R.id.progressBarNFC);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled.");
        }

        handleIntent(getIntent());

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(username.getText().toString().equals("toto") && password.getText().toString().equals("1234")){

                    if(tagTime == null)
                        Toast.makeText(nfc.this, "You need to tag NFC to be authentified", Toast.LENGTH_LONG).show();
                    else {
                        long now = new Date().getTime();
                        long differ = (now - tagTime);

                        if (differ <= MAX_MINUTES) {
                            Toast.makeText(nfc.this, "Your are authentified", Toast.LENGTH_LONG).show();
                            Intent childIntent = new Intent(nfc.this, nfcChild.class);
                            childIntent.putExtra("time", tagTime);
                            startActivity(childIntent);
                        }
                        else{
                            Toast.makeText(nfc.this, "To late, sorry.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else
                {
                    mTextView.setText(R.string.erroAuthentification);
                }
            }
        });

        countDownTime = new CountDownTimer(MAX_MINUTES, 1000) {
            public void onTick(long millisUntilFinished) {
                    //forward progress
                    long finishedSeconds = MAX_MINUTES - millisUntilFinished;
                    total = (int) (((float) finishedSeconds / (float) MAX_MINUTES) * 100.0);
                    progressBarNFC.setProgress(100-total);
            }

            public void onFinish() {
                // DO something when 1 minute is up
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                nfctask.execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    nfctask.execute(tag);
                    break;
                }
            }
        }
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }


    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    public void doAfterNfcEnable() {
        tagTime = new Date().getTime();
        countDownTime.start();
    }
}