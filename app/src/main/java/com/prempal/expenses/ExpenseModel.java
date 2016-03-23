package com.prempal.expenses;

import java.util.List;

/**
 * Created by prempal on 23/3/16.
 */
public class ExpenseModel {

    private List<Expense> expenses;

    public List<Expense> getExpenses() {
        return expenses;
    }

    public class Expense {

        private Object id;
        private String description;
        private Integer amount;
        private String category;
        private String time;
        private String state;

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

}
