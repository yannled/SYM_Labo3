package ch.heigvd.sym.template;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.nfc.NfcAdapter;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class nfc extends Activity {

    private EditText username;
    private EditText password;
    private Button connect;
    private NfcAdapter mNfcAdapter;

    private static final String USERNAME = "toto";
    private static final String PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        connect = findViewById(R.id.buttonConnect);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, getResources().getString(R.string.error_NFC), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(nfc.this, getResources().getString(R.string.disabled_NFC), Toast.LENGTH_LONG).show();
        }

        handleIntent(getIntent());

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = VerifAuthentification();
                switch (result){
                    case "USERNAME_EMPTY":
                        Toast.makeText(nfc.this, getResources().getString(R.string.username_empty), Toast.LENGTH_LONG).show();
                        break;
                    case "PASSWORD_EMPTY":
                        Toast.makeText(nfc.this, getResources().getString(R.string.password_empty), Toast.LENGTH_LONG).show();
                        break;
                    case "USERNAME_PASSWORD_FALSE":
                        Toast.makeText(nfc.this, getResources().getString(R.string.username_password_false), Toast.LENGTH_LONG).show();
                        break;
                    case "OK":
                        Toast.makeText(nfc.this, getResources().getString(R.string.authentification_ok), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        setupForegroundDispatch();

    }

    @Override
    public void onPause(){
        super.onPause();
        stopForegroundDispatch();
    }

    private void handleIntent(Intent intent) {
        // TODO: handle Intent
    }

    private void setupForegroundDispatch() {
        if(mNfcAdapter == null)
            return;
        final Intent intent = new Intent(this.getApplicationContext(),
                this.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent =
                PendingIntent.getActivity(this.getApplicationContext(), 0, intent, 0);
        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};
// Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Log.e(TAG, "MalformedMimeTypeException", e);
        }
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList);
    }

    private void stopForegroundDispatch() {
        if(mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    private String VerifAuthentification(){
        if(username.getText().toString().equals(""))
            return "USERNAME_EMPTY";
        if(password.getText().toString().equals(""))
            return "PASSWORD_EMPTY";
        if(username.getText().toString().equals(USERNAME) && password.getText().toString().equals(PASSWORD)) {
            return "OK";
        }
        else {
            return "USERNAME_PASSWORD_FALSE";
        }
    }
}
