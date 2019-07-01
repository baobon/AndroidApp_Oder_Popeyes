package com.example.eatit.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.eatit.Model.Oder;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "EatitDB.db";
    private static final int DB_VER=1;

    public Database(Context context) {
        super(context, DB_NAME,null,DB_VER);
    }

    public List<Oder> getCard(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

//        String[] sqlSelect = {"ProductName","ProductId","Quantity","Price","Discount"};
        String[] sqlSelect = {"ProductId","ProductName","Quantity","Price","Discount"};
        String sqlTable ="OderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Oder> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                result.add(new Oder(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                        ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Oder oder){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OderDetail(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                oder.getProductId(),
                oder.getProductName(),
                oder.getQuantity(),
                oder.getPrice(),
                oder.getDiscount());
            db.execSQL(query);
    }
    public void cleanCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OderDetail");
        db.execSQL(query);
    }
}
