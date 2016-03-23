package com.prempal.expenses;

/**
 * Created by prempal on 23/3/16.
 */
public class ExpenseModel {

    private Object id;
    private String description;
    private Integer amount;
    private String category;
    private String time;
    private String state;

    public ExpenseModel(Object id, String description, Integer amount, String category, String time, String state) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.time = time;
        this.state = state;
    }

    public Object getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getTime() {
        return time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
