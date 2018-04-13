package com.example.kylen.active_house;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class select_room extends AppCompatActivity {
    String user_id, username;
    ImageButton androidImageButton;
    private RequestQueue requestQueue;
    private StringRequest request;
    private ArrayList<String> rooms = new ArrayList<>();
    ArrayAdapter adapter;
    private static final String URL1 = "http://activehousedatabase.xyz/read_data.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room);



        user_id = getIntent().getStringExtra("uid");
        username = getIntent().getStringExtra("username");

        androidImageButton = (ImageButton) findViewById(R.id.image_button_android);

        TextView tv = findViewById(R.id.tv1);


        tv.setText("Hi " + username + "\n Please select a room:");
        SpannableString ss=  new SpannableString(username);
        ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 5, 0);
        adapter = new ArrayAdapter<String>(this,
                R.layout.room_item, R.id.tv_room_item, rooms);

        ListView listView = (ListView) findViewById(R.id.lv_rooms);
        listView.setAdapter(adapter);

        androidImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  getIntent();
                finish();
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(select_room.this, MainActivity.class);
                intent.putExtra("room_num", position);
                intent.putExtra("uid", user_id);
                intent.putExtra("username", username);

                startActivity(intent);
                finish();
                rooms = new ArrayList<>();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new LoadInformation().execute();
    }

    private class LoadInformation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... location) {

            startURL();

            return null;
        }

        private void startURL() {
            request = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);

                        for(int i = 0; i < jsonObject.getJSONArray("ser_response").length(); i++)
                            rooms.add("Room  " + (i+1));

                        adapter.notifyDataSetChanged();

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
                    hashMap.put("uid", user_id);
                    return hashMap;

                }
            };
            requestQueue.add(request);
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Log Out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(select_room.this,LogIn.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
