package ch.heigvd.sym.template;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

// authorisation request :https://developer.android.com/training/permissions/requesting#java

public class ibeacon extends Activity implements BeaconConsumer {

    ListView listView ;
    List<String> ibeaconList;
    private BeaconManager beaconManager;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ibeacon);


        listView = findViewById(R.id.ibeacon_list);
        ibeaconList = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, ibeaconList);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        beaconManager.bind((BeaconConsumer) this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind((BeaconConsumer) this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    ibeaconList.clear();
                    for(Beacon beacon : beacons){
                        ibeaconList.add("UUID: " + beacon.getId1()
                                + "\nMAJOR: " + beacon.getId2()
                                + "\nMINOR: " + beacon.getId3()
                                + "\nRSSI: " + beacon.getRssi()
                                + "\nTX: " + beacon.getTxPower()
                                + "\nDISTANCE: " + beacon.getDistance());
                    }

                    adapter.notifyDataSetChanged();
                }
                else{
                    ibeaconList.add("Aucun iBeacon détecté");
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

}

