package com.example.pbs_mobile.ViewSalesOrders;

public class ViewSalesOrderDataModels {

    public static class SalesOrdersForTodayCardviewDataModel {

        private String status;

        public SalesOrdersForTodayCardviewDataModel() {

        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class SalesOrdersForTodayListViewDataModel {
        private String soNumber, customer, qty, part, line, isbn, description, unitPrice, binNumber;

        public SalesOrdersForTodayListViewDataModel() {

        }

        public String getSoNumber() {
            return soNumber;
        }

        public void setSoNumber(String soNumber) {
            this.soNumber = soNumber;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getBinNumber() {
            return binNumber;
        }

        public void setBinNumber(String binNumber) {
            this.binNumber = binNumber;
        }
    }
}
