package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myapplication.constants.DataBaseConstants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> products = new ArrayList();
    private ArrayAdapter<String> adapter;

    private ArrayList<String> selectedProducts = new ArrayList();
    private ListView productsList;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        productsList = findViewById(R.id.productList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, read(database));
        productsList.setAdapter(adapter);

        // обработка установки и снятия отметки в списке
        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // получаем нажатый элемент
                String phone = adapter.getItem(position);
                if (productsList.isItemChecked(position)) {
                    selectedProducts.add(phone);
                } else {
                    selectedProducts.remove(phone);
                }
            }
        });
    }

    private List<String> read(SQLiteDatabase database) {
        Cursor cursor = database.query(DataBaseConstants.TABLE_PRODUCTS,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null);

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(DataBaseConstants.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DataBaseConstants.KEY_NAME);
            products.add(cursor.getString(nameIndex));
        }
        cursor.close();
        return products;
    }


    public void add(View view) {
        EditText phoneEditText = findViewById(R.id.product);
        String phone = phoneEditText.getText().toString();
        if(!phone.isEmpty() && !products.contains(phone)) {
            dbHelper = new DBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstants.KEY_NAME, phone);
            database.insert(DataBaseConstants.TABLE_PRODUCTS, null, contentValues);

            adapter.add(phone);
            phoneEditText.setText("");
            adapter.notifyDataSetChanged();
        }
    }

    public void remove(View view){
        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        // получаем и удаляем выделенные элементы
        for(String product : selectedProducts) {
            database.delete(DataBaseConstants.TABLE_PRODUCTS, null, null);
            adapter.remove(product);
        }
        // снимаем все ранее установленные отметки
        productsList.clearChoices();
        // очищаем массив выбраных объектов
        selectedProducts.clear();
        adapter.notifyDataSetChanged();
    }
}
