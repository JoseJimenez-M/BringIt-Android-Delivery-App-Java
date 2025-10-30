package com.example.ecommerce;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ecommerce.adapters.CartAdapter;
import com.example.ecommerce.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TequilaFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // Keep cart items in memory (like BeerFragment)
    private List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tequila, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        cartAdapter = new CartAdapter(cartItems);

        Button btnAddToCart = root.findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(v -> addToCart());

        return root;
    }

    private void addToCart() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the tequila item
        CartItem item = new CartItem();
        item.setName("Tequila");
        item.setPrice(55); // or whatever price you want
        item.setQuantity(1);

        boolean found = false;
        for (CartItem c : cartItems) {
            if (c.getName().equals(item.getName())) {
                c.setQuantity(c.getQuantity() + 1);
                cartAdapter.updateQuantityInFirestore(c, c.getQuantity());
                found = true;
                break;
            }
        }

        if (!found) {
            String itemId = UUID.randomUUID().toString();
            item.setId(itemId);
            cartItems.add(item);
            cartAdapter.updateQuantityInFirestore(item, item.getQuantity());
        }

        cartAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Tequila added to cart!", Toast.LENGTH_SHORT).show();
    }
}
