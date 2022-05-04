package com.example.ptunga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter recyclerAdapter;
    List<Item> itemList;
    List<Product> productList;
    ProductRVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView=(RecyclerView) findViewById(R.id.recycler_id);
        productList =new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(this, itemList);
        recyclerView.setAdapter(recyclerAdapter);
        productList=fillTheList();
        rvAdapter = new ProductRVAdapter(productList,this);
        recyclerView.setAdapter(rvAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recycler_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }

    private List<Product> fillTheList() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Skirt",R.drawable.j,340,"red and green apple from south africa keep it in dry and cool place"));
        productList.add(new Product("S skirt",R.drawable.jj,120,"available as row or as ready to consume"));
        productList.add(new Product("Shirt",R.drawable.tt,200,"red chery at very low price"));
        productList.add(new Product("Trouser",R.drawable.s,300,"available as row or as ready to consume"));
        productList.add(new Product("B Skirt",R.drawable.jjj,100,"available as row or as ready to consume"));
        productList.add(new Product("C hat",R.drawable.hh,200,"available as row or as ready to consume"));
        productList.add(new Product("B Shirt",R.drawable.t,300,"available as row or as ready to consume"));
        productList.add(new Product("Wardrobe",R.drawable.w,200,"available as row or as ready to consume"));
        productList.add(new Product("W Hat",R.drawable.h,300,"available as row or as ready to consume"));
        return productList;
    }

}