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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor myEdit;
    String admin_usernameS,admin_passwordS,user_usernameS,user_passwordS;

    FirebaseDatabase database;
    DatabaseReference FlagRef;
    TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //changing statusbar color
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        getWindow().setNavigationBarColor(this.getResources().getColor(R.color.colorAccent));

        Toolbar toolbar = findViewById(R.id.myToolBar);
        usernameText= findViewById(R.id.IoT_username);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home Automation System");

        updateAdminCredentials();

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            toolbar.setSubtitle("Your not connected");
        }
        else {
            toolbar.setSubtitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }

        preferences = getSharedPreferences("myDevices", Context.MODE_PRIVATE);
        updateUserConnectedStatus();
        myEdit= preferences.edit();
    }

    private void updateUserConnectedStatus(){
        user_usernameS = preferences.getString("users_username",null);
        user_passwordS = preferences.getString("users_password",null);
        if (user_usernameS != null && user_passwordS != null )
        {   //username and password are present, do your stuff
            usernameText.setText("Username : "+user_usernameS+"\nPassword : "+user_passwordS);
            Toast.makeText(MainActivity.this,"Connected Successfully",Toast.LENGTH_SHORT).show();
        }
        else{
            usernameText.setText("Currently your not connected to any IoT user account. Please contact admin for more information.");
            Toast.makeText(MainActivity.this,"Your not connected",Toast.LENGTH_SHORT).show();
        }
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
        new MenuInflater(this).inflate(R.menu.iot_admin_menu, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_new_user:
                // do something
                updateAdminCredentials();
                create_new_user();
                return true;
            case R.id.connect_existing_user:
                // do something
                updateAdminCredentials();
                connect_existing_user();
                return true;
            case R.id.delete_existing_user:
                // do something
                updateAdminCredentials();
                delete_existing_user();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void delete_existing_user() {

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.TOP)
                .setMargin(50,10,50,0)
                .setContentHolder(new ViewHolder(R.layout.iot_dialog_box))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        View holderView = dialog.getHolderView();

        final EditText admin_username = holderView.findViewById(R.id.admin_username);
        final EditText admin_password = holderView.findViewById(R.id.admin_password);
        final EditText users_username = holderView.findViewById(R.id.users_username);
        final EditText users_password = holderView.findViewById(R.id.users_password);

        Button delete = holderView.findViewById(R.id.iot_dialog_box_doneBtn);
        delete.setText("DELETE");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean failFlag = false;
                // TODO Auto-generated method stub
                if( admin_username.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }
                if( admin_password.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }
                if( users_username.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }
                if( users_password.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }

                if (failFlag) {
                    Toast.makeText(MainActivity.this,"Please enter all fields",Toast.LENGTH_SHORT).show();

                } else if(admin_username.getText().toString().equals(admin_usernameS) && admin_password.getText().toString().equals(admin_passwordS)
                        && users_password.getText().toString().equals(preferences.getString("users_password",""))
                        && users_username.getText().toString().equals(preferences.getString("users_username",""))){

                    FirebaseDatabase.getInstance().getReference()
                            .child("IoT")
                            .child(users_username.getText().toString()) //get value from shared preferences
                            .setValue(null)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MainActivity.this,"User deleted and disconnected successfully",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });

                    myEdit.remove("users_username");
                    myEdit.remove("users_password");
                    myEdit.clear();
                    myEdit.apply();
                    updateUserConnectedStatus();
                }
                else {
                    Toast.makeText(MainActivity.this,"Credentials are wrong !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }

    private void connect_existing_user() {

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.TOP)
                .setMargin(50,10,50,0)
                .setContentHolder(new ViewHolder(R.layout.iot_dialog_box))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        View holderView = (LinearLayout)dialog.getHolderView();

        final EditText admin_username = holderView.findViewById(R.id.admin_username);
        final EditText admin_password = holderView.findViewById(R.id.admin_password);
        final EditText users_username = holderView.findViewById(R.id.users_username);
        final EditText users_password = holderView.findViewById(R.id.users_password);

        Button connect = holderView.findViewById(R.id.iot_dialog_box_doneBtn);
        connect.setText("CONNECT");

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean failFlag = false;
                // TODO Auto-generated method stub
                if( admin_username.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }
                if( admin_password.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }
                if( users_username.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }
                if( users_password.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }

                if (failFlag) {
                    Toast.makeText(MainActivity.this,"Please enter all fields",Toast.LENGTH_SHORT).show();

                } else if(admin_username.getText().toString().equals(admin_usernameS) && admin_password.getText().toString().equals(admin_passwordS)){
                    myEdit.putString("users_username", users_username.getText().toString());
                    myEdit.putString("users_password", users_password.getText().toString());
                    myEdit.apply();
                    Toast.makeText(MainActivity.this,"User Connected successfully",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    updateUserConnectedStatus();
                }
                else {
                    Toast.makeText(MainActivity.this,"Credentials are wrong !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }

    private void create_new_user() {

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.TOP)
                .setMargin(50,10,50,0)
                .setContentHolder(new ViewHolder(R.layout.iot_dialog_box))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        View holderView = (LinearLayout)dialog.getHolderView();

        final EditText admin_username = holderView.findViewById(R.id.admin_username);
        final EditText admin_password = holderView.findViewById(R.id.admin_password);
        final EditText users_username = holderView.findViewById(R.id.users_username);
        final EditText users_password = holderView.findViewById(R.id.users_password);

        Button create = holderView.findViewById(R.id.iot_dialog_box_doneBtn);
        create.setText("CREATE");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean failFlag = false;
                // TODO Auto-generated method stub
                if( admin_username.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }
                if( admin_password.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }
                if( users_username.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }
                if( users_password.getText().toString().trim().length() == 0 )
                {
                    failFlag = true;
                }

                if (failFlag) {
                    Toast.makeText(MainActivity.this,"Please enter all fields",Toast.LENGTH_SHORT).show();

                } else if(admin_username.getText().toString().equals(admin_usernameS) && admin_password.getText().toString().equals(admin_passwordS)){

                    myEdit.putString("users_username", users_username.getText().toString());
                    myEdit.putString("users_password", users_password.getText().toString());
                    myEdit.apply();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("password", users_password.getText().toString());
                    map.put("D0", 0);
                    map.put("D1", 0);
                    map.put("D2", 0);
                    map.put("D3", 0);
                    map.put("D4", 0);
                    map.put("D5", 0);
                    map.put("D6", 0);
                    map.put("D7", 0);
                    map.put("D8", 0);
                    map.put("D9", 0);

                    FirebaseDatabase.getInstance().getReference()
                            .child("IoT")
                            .child(preferences.getString("users_username", "")) //get value from shared preferences
                            .setValue(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MainActivity.this,"User added and connected successfully",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });

                    updateUserConnectedStatus();
                }
                else {
                    Toast.makeText(MainActivity.this,"Credentials are wrong !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

}
