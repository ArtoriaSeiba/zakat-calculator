package com.example.zakatcalculator;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar toolbar = findViewById(R.id.toolbar_result);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Zakat Result");
        }

        // Get data from intent
        double weight = getIntent().getDoubleExtra("weight", 0);
        double price = getIntent().getDoubleExtra("price", 0);
        String type = getIntent().getStringExtra("type");
        double totalValue = getIntent().getDoubleExtra("totalValue", 0);
        double weightMinusX = getIntent().getDoubleExtra("weightMinusX", 0);
        double payableValue = getIntent().getDoubleExtra("payableValue", 0);
        double totalZakat = getIntent().getDoubleExtra("totalZakat", 0);

        // Results
        TextView tvTotalZakat = findViewById(R.id.tvResultTotalZakat);
        TextView tvTotalValue = findViewById(R.id.tvResultTotalValue);
        TextView tvWeightMinusX = findViewById(R.id.tvResultWeightMinusX);
        TextView tvPayableValue = findViewById(R.id.tvResultPayableValue);

        // Summary
        TextView tvSummaryWeight = findViewById(R.id.tvSummaryWeight);
        TextView tvSummaryType = findViewById(R.id.tvSummaryType);
        TextView tvSummaryPrice = findViewById(R.id.tvSummaryPrice);

        // Set Values
        tvTotalZakat.setText(getString(R.string.total_zakat_rm, totalZakat));
        tvTotalValue.setText(getString(R.string.total_gold_value_rm, totalValue));
        tvWeightMinusX.setText(getString(R.string.weight_minus_x, weightMinusX));
        tvPayableValue.setText(getString(R.string.zakat_payable_value_rm, payableValue));

        tvSummaryWeight.setText(getString(R.string.summary_weight, weight));
        tvSummaryType.setText(getString(R.string.summary_type, type));
        tvSummaryPrice.setText(getString(R.string.summary_price, price));

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
