package com.example.myapplication.constants;

public class DataBaseConstants {

    private DataBaseConstants() {
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "productDb";
    public static final String TABLE_PRODUCTS = "products";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";

    public static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE products(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);";
    public static final String DROP_TABLE_PRODUCTS = "DROP TABLE IF EXISTS products;";
}
