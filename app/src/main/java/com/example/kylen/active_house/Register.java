package com.example.kylen.active_house;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Register extends AppCompatActivity {

    Button SignUp;
    private RequestQueue requestQueue;
    EditText usernameText, passwordText, emailText;
    private static final String URL2 = "http://activehousedatabase.xyz/register1.php";

    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SignUp = findViewById(R.id.registerButton);
        usernameText = findViewById(R.id.userEdit);
        passwordText = findViewById(R.id.passwordEdit);
        emailText = findViewById(R.id.emailEdit);

        requestQueue = Volley.newRequestQueue(this);

        SignUp.setOnClickListener(new View.OnClickListener() {
            ProgressDialog progressDialog;
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(Register.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                final String email = emailText.getText().toString();
                final String username = usernameText.getText().toString();
                final String password = passwordText.getText().toString();
                final String content = "test";


                if(email.isEmpty() || username.isEmpty() || password.isEmpty() ||content.isEmpty()){
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Registration failed. Fill in all text fields")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();


                }else {

                    request = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                assert jsonObject != null;
                                progressDialog.dismiss();
                                if (jsonObject.names().get(0).equals("success")) {
                                    Toast.makeText(getApplicationContext(), "SUCCESS" + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LogIn.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
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
                            hashMap.put("username", usernameText.getText().toString());
                            hashMap.put("password", passwordText.getText().toString());
                            hashMap.put("content", content);
                            hashMap.put("email", emailText.getText().toString());

                            return hashMap;

                        }
                    };
                    requestQueue.add(request);
                }



            }
        });
    }
}
