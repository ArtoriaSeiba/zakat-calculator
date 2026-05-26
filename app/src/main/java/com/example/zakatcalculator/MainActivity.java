package com.example.zakatcalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextInputEditText etWeight, etPrice;
    private RadioGroup rgType;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, 0, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        etWeight = findViewById(R.id.etWeight);
        etPrice = findViewById(R.id.etPrice);
        rgType = findViewById(R.id.rgType);

        Button btnCalculate = findViewById(R.id.btnCalculate);
        Button btnReset = findViewById(R.id.btnReset);

        btnCalculate.setOnClickListener(v -> calculateZakat());
        btnReset.setOnClickListener(v -> resetFields());
    }

    private void calculateZakat() {
        Editable weightEditable = etWeight.getText();
        Editable priceEditable = etPrice.getText();

        String weightStr = weightEditable != null ? weightEditable.toString().trim() : "";
        String priceStr = priceEditable != null ? priceEditable.toString().trim() : "";

        if (weightStr.isEmpty()) {
            etWeight.setError(getString(R.string.error_empty));
            etWeight.requestFocus();
            return;
        }
        if (priceStr.isEmpty()) {
            etPrice.setError(getString(R.string.error_empty));
            etPrice.requestFocus();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double price = Double.parseDouble(priceStr);

            if (weight <= 0 || price <= 0) {
                Toast.makeText(this, "Please enter values greater than zero.", Toast.LENGTH_SHORT).show();
                return;
            }
            
            boolean isKeep = rgType.getCheckedRadioButtonId() == R.id.rbKeep;
            double xValue = isKeep ? 85.0 : 200.0;
            String typeStr = isKeep ? getString(R.string.keep) : getString(R.string.wear);

            double totalValue = weight * price;
            double weightMinusX = weight - xValue;
            double zakatPayableWeight = Math.max(0, weightMinusX);
            double zakatPayableValue = zakatPayableWeight * price;
            double totalZakat = zakatPayableValue * 0.025;

            // Start Result Activity
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("weight", weight);
            intent.putExtra("price", price);
            intent.putExtra("type", typeStr);
            intent.putExtra("totalValue", totalValue);
            intent.putExtra("weightMinusX", weightMinusX);
            intent.putExtra("payableValue", zakatPayableValue);
            intent.putExtra("totalZakat", totalZakat);
            startActivity(intent);

            hideKeyboard();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetFields() {
        etWeight.setText("");
        etPrice.setText("");
        etWeight.setError(null);
        etPrice.setError(null);
        rgType.check(R.id.rbKeep);
        etWeight.requestFocus();
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_theme) {
            showThemeSelectionDialog();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showThemeSelectionDialog() {
        String[] themes = {getString(R.string.theme_light), getString(R.string.theme_dark), getString(R.string.theme_system)};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_theme);
        builder.setItems(themes, (dialog, which) -> {
            if (which == 0) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (which == 1) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_share) {
            shareApp();
            return true;
        } else if (item.getItemId() == R.id.menu_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Zakat Gold Calculator: " + getString(R.string.github_url);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}
