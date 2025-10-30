package com.example.ecommerce.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.adapters.CartAdapter;
import com.example.ecommerce.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerCart;
    private CartAdapter adapter;
    private List<CartItem> cartItemList;
    private TextView txtTotal;

    private Button btnPay;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerCart = root.findViewById(R.id.recyclerCart);
        recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));

        txtTotal = root.findViewById(R.id.txtTotal);
        btnPay = root.findViewById(R.id.btnPay);

        btnPay.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_cartFragment_to_paymentFragment);

            //reinitialize cart
            clearCart();

        });



        cartItemList = new ArrayList<>();
        adapter = new CartAdapter(cartItemList);
        recyclerCart.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        loadCart();

        return root;
    }

    private void loadCart() {
        if (auth.getCurrentUser() == null) return;

        String uid = auth.getCurrentUser().getUid();

        db.collection("carts")
                .document(uid)
                .collection("items")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null || querySnapshot == null) return;

                    cartItemList.clear();
                    int total = 0;
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        CartItem item = doc.toObject(CartItem.class);
                        cartItemList.add(item);
                        total += item.getPrice() * item.getQuantity();
                    }
                    adapter.notifyDataSetChanged();
                    txtTotal.setText("Total: $" + total + " CAD");
                });
    }

    private void clearCart() {
        if (auth.getCurrentUser() == null) return;

        String uid = auth.getCurrentUser().getUid();

        db.collection("carts")
                .document(uid)
                .collection("items")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        Toast.makeText(getContext(), "Cart is already empty.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Delete all items from Firestore
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        doc.getReference().delete();
                    }

                    // Clear local list
                    cartItemList.clear();
                    adapter.notifyDataSetChanged();
                    txtTotal.setText("Total: $0 CAD");

                });

    }

}
