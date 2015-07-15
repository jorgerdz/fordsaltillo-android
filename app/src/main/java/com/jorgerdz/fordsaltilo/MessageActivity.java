package com.jorgerdz.fordsaltilo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        preferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        API.get("notifications", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
                if (statusCode < 300) {
                    try {
                        JSONArray notifications = response.getJSONArray("notifications");
                        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.list);

                        for(int i = 0; i < notifications.length(); i++){
                            JSONObject notification = notifications.getJSONObject(i);
                            LayoutInflater li =  (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View tempView = li.inflate(R.layout.message_template, null);
                            TextView textTitle = (TextView) tempView.findViewById(R.id.firstLine);
                            TextView textDescription = (TextView) tempView.findViewById(R.id.secondLine);

                            textTitle.setText(notification.getString("title"));
                            textDescription.setText(notification.getString("description"));
                            mainLayout.addView(tempView);
                        }

                    }catch(JSONException exception){

                    }
                }
            }
        });
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

    protected void returnToRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
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
}
