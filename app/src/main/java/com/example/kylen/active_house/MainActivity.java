package com.example.kylen.active_house;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

        Button retrieve;
        TextView roomdata;
        String user_id, username;
        int room_num;
    private RequestQueue requestQueue;
    private StringRequest request;
    private static final String URL1 = "http://activehousedatabase.xyz/read_data.php";

    TextView textView[] = new TextView[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        retrieve = findViewById(R.id.button3);

        textView[0] = findViewById(R.id.textView5);
        textView[1] = findViewById(R.id.textView2);
        textView[2] = findViewById(R.id.textView4);
        textView[3] = findViewById(R.id.textView3);
        textView[4] = findViewById(R.id.textView7);
        textView[5] = findViewById(R.id.textView6);
        textView[6] = findViewById(R.id.textView8);
        textView[7] = findViewById(R.id.textView9);
        textView[8] = findViewById(R.id.textView10);


        user_id = getIntent().getStringExtra("uid");
        room_num = getIntent().getIntExtra("room_num", -1);
        username = getIntent().getStringExtra("username");

        requestQueue = Volley.newRequestQueue(this);

        retrieve.setOnClickListener(new View.OnClickListener() {
            ProgressDialog progressDialog;
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                request = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            JSONObject mObj = jsonObject.getJSONArray("ser_response").getJSONObject(MainActivity.this.room_num);
                            textView[0].setText("RMS Level: \n"  + mObj.getString("RMS_level" ) + " amps");
                            textView[1].setText("Power: \n"  + mObj.getString("Power") + " Watts");
                            textView[2].setText("Gas: \n" + mObj.getString("Gas"));
                            textView[3].setText("Light: \n"  + mObj.getString("Light") + " Lux");
                            textView[4].setText("Temp: \n"  + mObj.getString("Temp") + " °C");
                            textView[5].setText("Humidity: \n"  + mObj.getString("Humid") + " %");
                            textView[6].setText("Lqd Qntty: \n" + mObj.getString("Lqd_Qntty")  + " mL");
                            textView[7].setText("Flow Rate: \n"  + mObj.getString("Flow_Rate") + " L/min");
                            textView[8].setText("Lqd Out: \n"  + mObj.getString("Lqd_Out") + " mL/sec");

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
        });



    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Log Out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(MainActivity.this,LogIn.class);
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

    public void startSelectRoom(View view) {
        Intent intent = new Intent(this, select_room.class);
        intent.putExtra("uid",user_id);
        intent.putExtra("username", username);

        startActivity(intent);
    }
}
