package ch.heigvd.sym.template;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.health.SystemHealthManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

//Source : https://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278
public class nfcChild extends Activity implements nfcMethod{

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private NdefReaderTask nfctask;
    private NfcAdapter mNfcAdapter;
    private CountDownTimer countDownTime1;
    private CountDownTimer countDownTime2;
    private CountDownTimer countDownTime3;
    private Long tagTime;
    private static final long ONE_MINUTE = 1000 * 60;
    private static final long TIMER_MAX_SECURITY = ONE_MINUTE * 3;
    private static final long TIMER_MEDIUM_SECURITY = ONE_MINUTE * 2;
    private static final long TIMER_MIN_SECURITY = ONE_MINUTE;
    private long timeRemainingMax = ONE_MINUTE;
    private long timeRemainingMedium  = ONE_MINUTE;
    private long timeRemainingMin = ONE_MINUTE;
    private ProgressBar progressBarNFC1;
    private ProgressBar progressBarNFC2;
    private ProgressBar progressBarNFC3;
    private Button buttonMinSecurity;
    private Button buttonMediumSecurity;
    private Button buttonMaxSecurity;
    private boolean maxPrivileges = true;
    private boolean mediumPrivileges = true;
    private boolean minPrivileges = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_child);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            tagTime = extras.getLong("time");
            Toast.makeText(nfcChild.this, tagTime.toString(), Toast.LENGTH_LONG).show();
        }

        nfctask = new NdefReaderTask(this);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        progressBarNFC1 = findViewById(R.id.progressBarNFC1);
        progressBarNFC2 = findViewById(R.id.progressBarNFC2);
        progressBarNFC3 = findViewById(R.id.progressBarNFC3);
        progressBarNFC1.setProgress(100);
        progressBarNFC2.setProgress(100);
        progressBarNFC3.setProgress(100);
        buttonMinSecurity = findViewById(R.id.buttonMinSecurity);
        buttonMediumSecurity = findViewById(R.id.buttonMediumSecurity);
        buttonMaxSecurity = findViewById(R.id.buttonMaxSecurity);

        handleIntent(getIntent());

        buttonMaxSecurity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(maxPrivileges)
                    showPersonalDialog(getString(R.string.access));
                else
                    showPersonalDialog(getString(R.string.noAccess));
            }
        });

        buttonMediumSecurity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mediumPrivileges)
                    showPersonalDialog(getString(R.string.access));
                else
                    showPersonalDialog(getString(R.string.noAccess));
            }
        });

        buttonMinSecurity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(minPrivileges)
                    showPersonalDialog(getString(R.string.access));
                else
                    showPersonalDialog(getString(R.string.noAccess));
            }
        });

        countDownTime3 = new CountDownTimer(timeRemainingMax, 1000) {
            public void onTick(long millisUntilFinished) {

                //forward progress
                long finishedSeconds = timeRemainingMax - millisUntilFinished;
                int total = (int) (((float)finishedSeconds / (float)timeRemainingMax) * 100.0);
                progressBarNFC3.setProgress(100-total);
            }

            public void onFinish() {
                maxPrivileges = false;
                mediumPrivileges = false;
                minPrivileges = false;
            }
        };

        countDownTime2 = new CountDownTimer(timeRemainingMedium, 1000) {
            public void onTick(long millisUntilFinished) {

                //forward progress
                long finishedSeconds = timeRemainingMedium - millisUntilFinished;
                int total = (int) (((float)finishedSeconds / (float)timeRemainingMedium) * 100.0);
                progressBarNFC2.setProgress(100-total);
            }

            public void onFinish() {
                countDownTime3.start();
                maxPrivileges = false;
                mediumPrivileges = false;
            }
        };

        countDownTime1 = new CountDownTimer(timeRemainingMin, 1000) {
            public void onTick(long millisUntilFinished) {

                //forward progress
                long finishedSeconds = timeRemainingMin - millisUntilFinished;
                int total = (int) (((float)finishedSeconds / (float)timeRemainingMin) * 100.0);
                progressBarNFC1.setProgress(100-total);
            }

            public void onFinish() {
                countDownTime2.start();
                maxPrivileges = false;
            }
        };

        startProgressBar();
    }

    public void startProgressBar(){
        long now = new Date().getTime();
        long differTime = (now - tagTime);

        //Si on a tag il y a moins d'une minute on a un accès MAX
        if(differTime <= ONE_MINUTE) {
            timeRemainingMax = differTime - TIMER_MEDIUM_SECURITY;
            countDownTime1.start();
        }
        //Si on a tag il y a moins de deux minute, plus de une minute : on a un accès MEDIUM
        else if(differTime <= TIMER_MEDIUM_SECURITY) {
            timeRemainingMedium = differTime - TIMER_MIN_SECURITY;
            progressBarNFC1.setProgress(0);
            maxPrivileges = false;
            countDownTime2.start();
        }
        //Si on a tag il y a moins de trois minute, plus de deux minutes : on a un accès Min
        else if(differTime <= TIMER_MAX_SECURITY) {
            progressBarNFC1.setProgress(0);
            progressBarNFC2.setProgress(0);
            mediumPrivileges = false;
            maxPrivileges = false;
            timeRemainingMin = differTime;
            countDownTime3.start();
        }
        else {
            minPrivileges = false;
        }
    }

    protected void showPersonalDialog(String message) {

        AlertDialog.Builder alertbd = new AlertDialog.Builder(this);

        alertbd.setMessage(message);
        alertbd.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // we do nothing...
                // dialog close automatically
            }
        });
        alertbd.create().show();
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
        countDownTime1.cancel();
        progressBarNFC1.setProgress(100);
        countDownTime2.cancel();
        progressBarNFC2.setProgress(100);
        countDownTime3.cancel();
        progressBarNFC3.setProgress(100);
        maxPrivileges = true;
        mediumPrivileges = true;
        minPrivileges = true;
        startProgressBar();
    }
}