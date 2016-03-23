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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Handler mHandler;
    private ProgressBar mProgressBar;
    private String FETCH_URL = "https://jsonblob.com/api/jsonBlob/56f02ad3e4b01190df5714e8";
    private List<ExpenseModel> expenses;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, FETCH_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    expenses.clear();
                    mProgressBar.setVisibility(View.GONE);
                    try {
                        JSONArray array = response.getJSONArray("expenses");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            ExpenseModel expense = new ExpenseModel(object.get("id"), object.getString("description"),
                                    object.getInt("amount"), object.getString("category"), object.getString("time"),
                                    object.getString("state"));
                            expenses.add(expense);
                        }
                        if (mRecyclerView.getAdapter() == null) {
                            mRecyclerView.setAdapter(new ExpenseAdapter(expenses));
                        } else {
                            ((ExpenseAdapter) mRecyclerView.getAdapter()).update(expenses);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Error fetching expenses", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
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
        expenses = new ArrayList<>();
        mHandler = new Handler();
        mHandler.post(runnable);
    }

}
