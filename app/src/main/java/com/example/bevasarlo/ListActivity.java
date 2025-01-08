package com.example.bevasarlo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
    private ListView productListView;
    private Button backButton;
    private List<Product> productList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        productListView = findViewById(R.id.productListView);
        backButton = findViewById(R.id.backButton);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        productListView.setAdapter(adapter);

        backButton.setOnClickListener(v -> finish());

        productListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Product selectedProduct = productList.get(position);
            Intent intent = new Intent(ListActivity.this, TermekActivity.class);
            intent.putExtra("productId", position);
            startActivity(intent);
        });

        loadProducts();
    }

    private void loadProducts() {
        ApiService api = ApiClient.getApiService();
        api.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    productList = response.body();
                    adapter.clear();
                    for (Product product : productList) {
                        adapter.add(product.getName() + "\nEgységár: " + product.getUnitPrice() +
                                "\nMennyiség: " + product.getQuantity() + " " + product.getUnit() +
                                "\nBruttó ár: " + product.getGrossPrice());
                    }
                } else {
                    Toast.makeText(ListActivity.this, "Nem sikerült a termékek betöltése", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ListActivity.this, "Hiba: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
