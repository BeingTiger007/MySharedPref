package com.example.mysharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class iot_devices extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference FlagRef,UserRef;

    SharedPreferences preferences;
    SharedPreferences.Editor myEdit;

    Boolean D0, D1, D2, D3, D4, D5, D6, D7, D8, D9;
    Switch S0, S1, S2, S3, S4, S5, S6, S7, S8, S9;
    TextView T0, T1, T2, T3, T4, T5, T6, T7, T8, T9;
    TextView Sb0,Sb1,Sb2,Sb3,Sb4,Sb5,Sb6,Sb7,Sb8,Sb9;
    ImageView i1,i2,i3,i4,i5,i6,i7,i8,i9,i10;

    private String admin_usernameS, admin_passwordS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot_deviecs);

        toolbar = findViewById(R.id.myToolBar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("IoT Control panel");

        getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        getWindow().setNavigationBarColor(this.getResources().getColor(R.color.colorAccent));

        Toast.makeText(iot_devices.this, "Wait a movement\nFetching latest data", Toast.LENGTH_SHORT).show();

        S0 = findViewById(R.id.Iot_switch1);
        S1 = findViewById(R.id.Iot_switch2);
        S2 = findViewById(R.id.Iot_switch3);
        S3 = findViewById(R.id.Iot_switch4);
        S4 = findViewById(R.id.Iot_switch5);
        S5 = findViewById(R.id.Iot_switch6);
        S6 = findViewById(R.id.Iot_switch7);
        S7 = findViewById(R.id.Iot_switch8);
        S8 = findViewById(R.id.Iot_switch9);
        S9 = findViewById(R.id.Iot_switch10);

        T0 = findViewById(R.id.IoT_deviceName1);
        T1 = findViewById(R.id.IoT_deviceName2);
        T2 = findViewById(R.id.IoT_deviceName3);
        T3 = findViewById(R.id.IoT_deviceName4);
        T4 = findViewById(R.id.IoT_deviceName5);
        T5 = findViewById(R.id.IoT_deviceName6);
        T6 = findViewById(R.id.IoT_deviceName7);
        T7 = findViewById(R.id.IoT_deviceName8);
        T8 = findViewById(R.id.IoT_deviceName9);
        T9 = findViewById(R.id.IoT_deviceName10);

        Sb0 = findViewById(R.id.IoT_deviceIdText1);
        Sb1 = findViewById(R.id.IoT_deviceIdText2);
        Sb2 = findViewById(R.id.IoT_deviceIdText3);
        Sb3 = findViewById(R.id.IoT_deviceIdText4);
        Sb4 = findViewById(R.id.IoT_deviceIdText5);
        Sb5 = findViewById(R.id.IoT_deviceIdText6);
        Sb6 = findViewById(R.id.IoT_deviceIdText7);
        Sb7 = findViewById(R.id.IoT_deviceIdText8);
        Sb8 = findViewById(R.id.IoT_deviceIdText9);
        Sb9 = findViewById(R.id.IoT_deviceIdText10);

        i1 = findViewById(R.id.IoT_imageView1);
        i2 = findViewById(R.id.IoT_imageView2);
        i3 = findViewById(R.id.IoT_imageView3);
        i4 = findViewById(R.id.IoT_imageView4);
        i5 = findViewById(R.id.IoT_imageView5);
        i6 = findViewById(R.id.IoT_imageView6);
        i7 = findViewById(R.id.IoT_imageView7);
        i8 = findViewById(R.id.IoT_imageView8);
        i9 = findViewById(R.id.IoT_imageView9);
        i10 = findViewById(R.id.IoT_imageView10);

        preferences = getSharedPreferences("myDevices", Context.MODE_PRIVATE);
        toolbar.setSubtitle("Username : " + preferences.getString("users_username", ""));

        disableAllSwitch();

        database = FirebaseDatabase.getInstance();
        UserRef = database.getReference("IoT").child(preferences.getString("users_username", ""));

        updateAllDeviceData();
        updateAllSwitchData();
        updateAdminCredentials();

        myEdit = preferences.edit();

        S0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserRef.child("D0").setValue(true);
                    Sb0.setText("Device ID : D0 \\ Status : On");
                    i1.setImageResource(R.drawable.ic_bulb_on);
                }
                else {
                    UserRef.child("D0").setValue(false);
                    Sb0.setText("Device ID : D0 \\ Status : Off");
                    i1.setImageResource(R.drawable.ic_bulb_off);
                }
            }
        });

        S1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserRef.child("D1").setValue(true);
                    Sb1.setText("Device ID : D1 \\ Status : On");
                    i2.setImageResource(R.drawable.ic_bulb_on);
                }
                else {
                    UserRef.child("D1").setValue(false);
                    Sb1.setText("Device ID : D1 \\ Status : Off");
                    i2.setImageResource(R.drawable.ic_bulb_off);
                }
            }
        });

        S2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserRef.child("D2").setValue(true);
                    Sb2.setText("Device ID : D2 \\ Status : On");
                    i3.setImageResource(R.drawable.ic_bulb_on);
                }
                else {
                    UserRef.child("D2").setValue(false);
                    Sb2.setText("Device ID : D2 \\ Status : Off");
                    i3.setImageResource(R.drawable.ic_bulb_off);
                }
            }
        });

        S3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserRef.child("D3").setValue(true);
                    Sb3.setText("Device ID : D3 \\ Status : On");
                    i4.setImageResource(R.drawable.ic_bulb_on);
                }
                else {
                    UserRef.child("D3").setValue(false);
                    Sb3.setText("Device ID : D3 \\ Status : Off");
                    i4.setImageResource(R.drawable.ic_bulb_off);
                }
            }
        });

        S4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserRef.child("D4").setValue(true);
                    Sb4.setText("Device ID : D4 \\ Status : On");
                    i5.setImageResource(R.drawable.ic_bulb_on);
                }
                else {
                    UserRef.child("D4").setValue(false);
                    Sb4.setText("Device ID : D4 \\ Status : Off");
                    i5.setImageResource(R.drawable.ic_bulb_off);
                }
            }
        });

        S5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserRef.child("D5").setValue(true);
                    Sb5.setText("Device ID : D5 \\ Status : On");
                    i6.setImageResource(R.drawable.ic_bulb_on);
                }
                else {
                    UserRef.child("D5").setValue(false);
                    Sb5.setText("Device ID : D5 \\ Status : Off");
                    i6.setImageResource(R.drawable.ic_bulb_off);
                }
            }
        });

        S6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserRef.child("D6").setValue(true);
                    Sb6.setText("Device ID : D6 \\ Status : On");
                    i7.setImageResource(R.drawable.ic_bulb_on);
                }
                else {
                    UserRef.child("D6").setValue(false);
                    Sb6.setText("Device ID : D6 \\ Status : Off");
                    i7.setImageResource(R.drawable.ic_bulb_off);
                }
            }
        });

        S7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserRef.child("D7").setValue(true);
                    Sb7.setText("Device ID : D7 \\ Status : On");
                    i8.setImageResource(R.drawable.ic_bulb_on);
                }
                else {
                    UserRef.child("D7").setValue(false);
                    Sb7.setText("Device ID : D7 \\ Status : Off");
                    i8.setImageResource(R.drawable.ic_bulb_off);
                }
            }
        });

        S8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserRef.child("D8").setValue(true);
                    Sb8.setText("Device ID : D8 \\ Status : On");
                    i9.setImageResource(R.drawable.ic_bulb_on);
                }
                else {
                    UserRef.child("D8").setValue(false);
                    Sb8.setText("Device ID : D8 \\ Status : Off");
                    i9.setImageResource(R.drawable.ic_bulb_off);
                }
            }
        });

        S9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserRef.child("D9").setValue(true);
                    Sb9.setText("Device ID : D9 \\ Status : On");
                    i10.setImageResource(R.drawable.ic_bulb_on);
                }
                else {
                    UserRef.child("D9").setValue(false);
                    Sb9.setText("Device ID : D9 \\ Status : Off");
                    i10.setImageResource(R.drawable.ic_bulb_off);
                }
            }
        });

    }


    private void updateAllDeviceData() {
        T0.setText("Device Name : " + preferences.getString("D0", "Device 1"));
        T1.setText("Device Name : " + preferences.getString("D1", "Device 2"));
        T2.setText("Device Name : " + preferences.getString("D2", "Device 3"));
        T3.setText("Device Name : " + preferences.getString("D3", "Device 4"));
        T4.setText("Device Name : " + preferences.getString("D4", "Device 5"));
        T5.setText("Device Name : " + preferences.getString("D5", "Device 6"));
        T6.setText("Device Name : " + preferences.getString("D6", "Device 7"));
        T7.setText("Device Name : " + preferences.getString("D7", "Device 8"));
        T8.setText("Device Name : " + preferences.getString("D8", "Device 9"));
        T9.setText("Device Name : " + preferences.getString("D9", "Device 10"));

    }

    private void updateAdminCredentials() {
        database = FirebaseDatabase.getInstance();
        FlagRef = database.getReference("IoT").child("Admin");

        FlagRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                admin_usernameS = dataSnapshot.child("username").getValue(String.class);
                //Toast.makeText(MainActivity.this,"admin username updated",Toast.LENGTH_SHORT).show();
                admin_passwordS = dataSnapshot.child("password").getValue(String.class);
                //Toast.makeText(MainActivity.this,"admin password updated",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.iot_device_rename, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iot_device_rename_menuItem:
                // do something
                updateAdminCredentials();
                DeviceRename();
                return true;
            case R.id.iot_to_aboutUs:
                // do something
                updateAdminCredentials();
                //openAboutUsActivity();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void DeviceRename() {
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.TOP)
                .setMargin(50, 10, 50, 0)
                .setContentHolder(new ViewHolder(R.layout.iot_dialog_box))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        View holderView = (LinearLayout) dialog.getHolderView();

        final EditText admin_username = holderView.findViewById(R.id.admin_username);
        final EditText admin_password = holderView.findViewById(R.id.admin_password);
        final EditText DeviceID = holderView.findViewById(R.id.users_username);
        final EditText DeviceName = holderView.findViewById(R.id.users_password);

        DeviceID.setHint("enter device ID");
        DeviceName.setHint("enter new device name");

        Button Rename = holderView.findViewById(R.id.iot_dialog_box_doneBtn);
        Rename.setText("RENAME");

        Rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean failFlag = false;
                // TODO Auto-generated method stub
                if (admin_username.getText().toString().trim().length() == 0) {
                    failFlag = true;
                }
                if (admin_password.getText().toString().trim().length() == 0) {
                    failFlag = true;
                }
                if (DeviceID.getText().toString().trim().length() == 0) {
                    failFlag = true;
                }
                if (DeviceName.getText().toString().trim().length() == 0) {
                    failFlag = true;
                }

                if (failFlag) {
                    Toast.makeText(iot_devices.this, "Please enter all fields", Toast.LENGTH_SHORT).show();

                } else if (admin_username.getText().toString().equals(admin_usernameS) && admin_password.getText().toString().equals(admin_passwordS)) {
                    myEdit.putString(DeviceID.getText().toString().toUpperCase(), DeviceName.getText().toString());
                    myEdit.apply();
                    Toast.makeText(iot_devices.this, "Device renamed successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    updateAllDeviceData();
                } else {
                    Toast.makeText(iot_devices.this, "Credentials are wrong !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }

    private void updateAllSwitchData() {
        database = FirebaseDatabase.getInstance();
        FlagRef = database.getReference("IoT").child(preferences.getString("users_username", null));
        //Toast.makeText(iot_devices.this,preferences.getString("users_username",null),Toast.LENGTH_SHORT).show();

        FlagRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Toast.makeText(iot_devices.this,"Wait a movement\nFetching latest data",Toast.LENGTH_SHORT).show();

                D0 = dataSnapshot.child("D0").getValue(Boolean.class);
                D1 = dataSnapshot.child("D1").getValue(Boolean.class);
                D2 = dataSnapshot.child("D2").getValue(Boolean.class);
                D3 = dataSnapshot.child("D3").getValue(Boolean.class);
                D4 = dataSnapshot.child("D4").getValue(Boolean.class);
                D5 = dataSnapshot.child("D5").getValue(Boolean.class);
                D6 = dataSnapshot.child("D6").getValue(Boolean.class);
                D7 = dataSnapshot.child("D7").getValue(Boolean.class);
                D8 = dataSnapshot.child("D8").getValue(Boolean.class);
                D9 = dataSnapshot.child("D9").getValue(Boolean.class);

                S0.setChecked(D0);
                S1.setChecked(D1);
                S2.setChecked(D2);
                S3.setChecked(D3);
                S4.setChecked(D4);
                S5.setChecked(D5);
                S6.setChecked(D6);
                S7.setChecked(D7);
                S8.setChecked(D8);
                S9.setChecked(D9);

                if (!D0) i1.setImageResource(R.drawable.ic_bulb_off);
                if (!D1) i2.setImageResource(R.drawable.ic_bulb_off);
                if (!D2) i3.setImageResource(R.drawable.ic_bulb_off);
                if (!D3) i4.setImageResource(R.drawable.ic_bulb_off);
                if (!D4) i5.setImageResource(R.drawable.ic_bulb_off);
                if (!D5) i6.setImageResource(R.drawable.ic_bulb_off);
                if (!D6) i7.setImageResource(R.drawable.ic_bulb_off);
                if (!D7) i8.setImageResource(R.drawable.ic_bulb_off);
                if (!D8) i9.setImageResource(R.drawable.ic_bulb_off);
                if (!D9) i10.setImageResource(R.drawable.ic_bulb_off);

                enableAllSwitch();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void disableAllSwitch() {
        S0.setEnabled(false);
        S1.setEnabled(false);
        S2.setEnabled(false);
        S3.setEnabled(false);
        S4.setEnabled(false);
        S5.setEnabled(false);
        S6.setEnabled(false);
        S7.setEnabled(false);
        S8.setEnabled(false);
        S9.setEnabled(false);
    }

    private void enableAllSwitch() {
        S0.setEnabled(true);
        S1.setEnabled(true);
        S2.setEnabled(true);
        S3.setEnabled(true);
        S4.setEnabled(true);
        S5.setEnabled(true);
        S6.setEnabled(true);
        S7.setEnabled(true);
        S8.setEnabled(true);
        S9.setEnabled(true);
    }
}
