package com.prempal.expenses;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by prempal on 23/3/16.
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<ExpenseModel.Expense> expenses;
    private ExpenseModel model;

    public ExpenseAdapter(ExpenseModel model) {
        this.model = model;
        this.expenses = model.getExpenses();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("h:mm a, MMM d, ''yy", Locale.ENGLISH);
        Date date = null;
        try {
            date = originalFormat.parse(expenses.get(position).getTime());
            String formattedDate = targetFormat.format(date);
            holder.time.setText(formattedDate);
        } catch (ParseException e) {
            holder.time.setText(expenses.get(position).getTime());
            e.printStackTrace();
        }

        holder.category.setText(expenses.get(position).getCategory());
        holder.description.setText(expenses.get(position).getDescription());
        holder.amount.setText("Amount : ₹" + expenses.get(position).getAmount());
        holder.state.setText("State : " + expenses.get(position).getState());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view.getContext(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void update(ExpenseModel newModel) {
        Log.d("adapter", "update: ");
        model = newModel;
        expenses = model.getExpenses();
        notifyDataSetChanged();
    }

    private int getCheckedItem(int position) {
        switch (expenses.get(position).getState()) {
            case "verified":
                return 0;
            case "unverified":
                return 1;
            case "fraud":
                return 2;

        }
        return 0;
    }

    private void pushChanges(final Context context, final int position) throws JSONException {
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, MainActivity.URL, new JSONObject(new Gson().toJson(model)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "onResponse: " + response);
                        notifyItemChanged(position);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error setting state", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(putRequest);
    }

    private void showDialog(final Context context, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Expense State");

        final String[] items = {"verified", "unverified", "fraud"};
        final int[] choice = {-1};
        builder.setSingleChoiceItems(items, getCheckedItem(position),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice[0] = which;
                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        expenses.get(position).setState(items[choice[0]]);
                        try {
                            pushChanges(context, position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView amount;
        TextView description;
        TextView time;
        TextView state;
        TextView category;
        View view;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            category = (TextView) itemView.findViewById(R.id.tv_category);
            description = (TextView) itemView.findViewById(R.id.tv_description);
            amount = (TextView) itemView.findViewById(R.id.tv_amount);
            category = (TextView) itemView.findViewById(R.id.tv_category);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            state = (TextView) itemView.findViewById(R.id.tv_state);
        }
    }
}
