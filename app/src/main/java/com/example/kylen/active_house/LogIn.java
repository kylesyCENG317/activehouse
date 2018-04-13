package com.example.kylen.active_house;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {

    Button loginButton, registerButton;
    EditText userE, passE;
    private RequestQueue requestQueue;
    private static final String URL1 = "http://activehousedatabase.xyz/LoginCheck1.php";
    private StringRequest request;

    MediaPlayer mysong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mysong = MediaPlayer.create(LogIn.this,R.raw.new_bark_town__stereo__pokemon_goldsilvercrystal);
        mysong.setAudioStreamType(AudioManager.STREAM_MUSIC);


        //mysong.setLooping(true);
        mysong.start();

        userE = findViewById(R.id.editText);
        passE = findViewById(R.id.editText2);
        loginButton = findViewById(R.id.button);
        registerButton = findViewById(R.id.button4);

        requestQueue = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            ProgressDialog progressDialog;
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(LogIn.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                final String username1 = userE.getText().toString();
                request = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        progressDialog.dismiss();
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {

                            if (jsonObject.names().get(0).equals("success")) {
                                //String content = jsonObject.getString("content1");
                                //Toast.makeText(getApplicationContext(), "Welcome! " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                //String name = jsonObject.getString("name1")

                                Intent intent = new Intent(LogIn.this, select_room.class);
                                //Toast.makeText(LogIn.this, jsonObject.getString("userid1"), Toast.LENGTH_SHORT).show();
                                //intent.putExtra("content", content);
                                intent.putExtra("uid",jsonObject.getString("userid1"));
                                intent.putExtra("username", username1);

                                startActivity(intent);

                                // startActivity(new Intent(getApplicationContext(), NavigationDrawer.class));
                            } else {
                                // Toast.makeText(getApplicationContext(),"no! " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "Wrong Credentials, are you already a member?", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("username", userE.getText().toString());
                        hashMap.put("password", passE.getText().toString());

                        return hashMap;

                    }
                };
                requestQueue.add(request);

            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, Register.class));
            }
        });







    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
