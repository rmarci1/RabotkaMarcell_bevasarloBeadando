package com.example.bevasarlo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText nameInput, unitPriceInput, quantityInput, unitInput;
    private Button addButton, viewProductsButton;
    private ApiService productApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.productName);
        unitPriceInput = findViewById(R.id.unitPrice);
        quantityInput = findViewById(R.id.quantity);
        unitInput = findViewById(R.id.unit);
        addButton = findViewById(R.id.addProductButton);

        productApi = ApiClient.getApiService();

        addButton.setOnClickListener(v -> createProduct());
        viewProductsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });
    }

    private void createProduct() {
        String name = nameInput.getText().toString().trim();
        String unitPriceStr = unitPriceInput.getText().toString().trim();
        String quantityStr = quantityInput.getText().toString().trim();
        String unit = unitInput.getText().toString().trim();

        if (name.isEmpty() || unitPriceStr.isEmpty() || quantityStr.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this, "Minden mező kitöltése kötelező", Toast.LENGTH_SHORT).show();
            return;
        }

        double unitPrice;
        double quantity;
        try {
            unitPrice = Double.parseDouble(unitPriceStr);
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Érvénytelen számformátum", Toast.LENGTH_SHORT).show();
            return;
        }

        Product newProduct = new Product(name, unitPrice, quantity, unit);

        productApi.createProduct(newProduct).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Termék sikeresen hozzáadva", Toast.LENGTH_SHORT).show();
                    clearInputs();
                } else {
                    Toast.makeText(MainActivity.this, "Hiba történt a termék létrehozásakor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Hiba: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearInputs() {
        nameInput.setText("");
        unitPriceInput.setText("");
        quantityInput.setText("");
        unitInput.setText("");
    }
}
