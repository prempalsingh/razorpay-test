package com.prempal.expenses;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private List<ExpenseModel> expenses;

    public ExpenseAdapter(List<ExpenseModel> expenses) {
        this.expenses = expenses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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
                showDialog(view.getContext());
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void update(List<ExpenseModel> newExpenses) {

    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Expense State");

        String[] items = {"Verified", "Unverified", "Fraud"};
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
