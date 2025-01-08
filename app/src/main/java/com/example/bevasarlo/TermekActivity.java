package com.example.bevasarlo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermekActivity extends AppCompatActivity {
    private EditText nameInput, unitPriceInput, quantityInput, unitInput;
    private Button updateButton, deleteButton, cancelButton;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termek);

        init();
        loadProduct();

        updateButton.setOnClickListener(v -> updateProduct());
        deleteButton.setOnClickListener(v -> confirmDelete());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void init(){
        productId = getIntent().getIntExtra("productId", -1);
        nameInput = findViewById(R.id.nameInput);
        unitPriceInput = findViewById(R.id.unitPriceInput);
        quantityInput = findViewById(R.id.quantityInput);
        unitInput = findViewById(R.id.unitInput);
        updateButton = findViewById(R.id.modifyButton);
        deleteButton = findViewById(R.id.deleteButton);
        cancelButton = findViewById(R.id.cancelButton);
    }
    private void loadProduct() {
        if (productId == -1) {
            Toast.makeText(this, "Érvénytelen termék azonosító", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ApiService api = ApiClient.getApiService();
        api.getProductById(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    nameInput.setText(product.getName());
                    unitPriceInput.setText(String.valueOf(product.getUnitPrice()));
                    quantityInput.setText(String.valueOf(product.getQuantity()));
                    unitInput.setText(product.getUnit());
                } else {
                    Toast.makeText(TermekActivity.this, "Nem sikerült betölteni a terméket", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(TermekActivity.this, "Hiba: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateProduct() {
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

        Product updatedProduct = new Product(name, unitPrice, quantity, unit);
        ApiService api = ApiClient.getApiService();
        api.updateProduct(productId, updatedProduct).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TermekActivity.this, "Termék sikeresen frissítve", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TermekActivity.this, "Nem sikerült a frissítés", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TermekActivity.this, "Hiba: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Törlés megerősítése")
                .setMessage("Biztosan törölni szeretnéd?")
                .setPositiveButton("Igen", (DialogInterface dialog, int which) -> deleteProduct())
                .setNegativeButton("Mégse", null)
                .show();
    }

    private void deleteProduct() {
        ApiService api = ApiClient.getApiService();
        api.deleteProduct(productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TermekActivity.this, "Termék sikeresen törölve", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TermekActivity.this, "Nem sikerült a törlés", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TermekActivity.this, "Hiba: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
