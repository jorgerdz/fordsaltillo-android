package com.jorgerdz.fordsaltilo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends AppCompatActivity {

    private ImageView mMensaje;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        mMensaje = (ImageView) findViewById(R.id.mensaje);

        mMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessageView();
            }
        });
    }

    protected void logout(){
        API.get("api/auth/signout", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                if (statusCode < 400) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("status", "not logged");
                    editor.commit();
                    returnToRegister();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String fail, java.lang.Throwable throwable){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("status", "not logged");
                    editor.commit();
                    returnToRegister();
            }
        });
    };

    protected void returnToRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    protected void openMessageView(){
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void createNotification(){
        Intent intent = new Intent(this, RegisterActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle("Bienvenido a Ford Saltillo")
                .setContentText("Ford")
                .setSmallIcon(R.drawable.ford)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }
}
