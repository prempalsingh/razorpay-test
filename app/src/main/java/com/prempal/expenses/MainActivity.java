package com.prempal.expenses;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static String URL = "https://jsonblob.com/api/jsonBlob/56f02ad3e4b01190df5714e8";
    private RecyclerView mRecyclerView;
    private Handler mHandler;
    private ProgressBar mProgressBar;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    mProgressBar.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    ExpenseModel model = gson.fromJson(response.toString(), ExpenseModel.class);
                    if (mRecyclerView.getAdapter() == null) {
                        mRecyclerView.setAdapter(new ExpenseAdapter(model));
                    } else {
                        ((ExpenseAdapter) mRecyclerView.getAdapter()).update(model);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Error fetching expenses", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            });
            request.setShouldCache(false);
            VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.expense_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mHandler = new Handler();
        mHandler.post(runnable);
    }

}
