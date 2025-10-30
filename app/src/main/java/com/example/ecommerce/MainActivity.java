package com.example.ecommerce;
import android.os.Bundle;

import com.example.ecommerce.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.View;


import com.example.ecommerce.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.itemFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // BottomNavigationView Visibility
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.startMenu || destination.getId() == R.id.registerFragment) {
                navView.setVisibility(View.GONE); // Hide in Login and Starting
            } else {
                navView.setVisibility(View.VISIBLE); // Show
            }
        });


        //STOCK

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Add initial stock if not exists
        addInitialStock();

    }


    //NAME, QUANTITY, PRICE
    private void addInitialStock() {
        db.collection("products").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().isEmpty()) {
                Product beer = new Product("Beer", 100, 40);
                Product tequila = new Product("Tequila", 100, 40);
                Product vodka = new Product("Vodka", 100, 40);

                db.collection("products").document("Be01").set(beer);
                db.collection("products").document("Te01").set(tequila);
                db.collection("products").document("Vo01").set(vodka);

                Log.d("Firestore", "Initial stock added.");
            } else if (task.isSuccessful()) {
                Log.d("Firestore", "Stock already exists.");
            } else {
                Log.e("Firestore", "Error checking stock", task.getException());
            }
        });
    }



}