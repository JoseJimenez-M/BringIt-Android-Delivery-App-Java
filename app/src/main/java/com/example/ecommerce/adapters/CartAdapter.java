package com.example.ecommerce.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> cartItems;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.itemName.setText(item.getName());
        holder.itemPrice.setText("$" + item.getPrice() + " CAD");
        holder.itemQty.setText(String.valueOf(item.getQuantity()));

        int subtotal = item.getPrice() * item.getQuantity();
        holder.itemSubtotal.setText("$" + subtotal + " CAD");

        // + Button
        holder.btnPlus.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            item.setQuantity(newQty);
            holder.itemQty.setText(String.valueOf(newQty));
            holder.itemSubtotal.setText("$" + (item.getPrice() * newQty) + " CAD");
            updateQuantityInFirestore(item, newQty);
        });

        // - Button
        holder.btnMinus.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            CartItem cItem = cartItems.get(pos);

            if (cItem.getQuantity() > 1) {
                int newQty = cItem.getQuantity() - 1;
                cItem.setQuantity(newQty);
                holder.itemQty.setText(String.valueOf(newQty));
                holder.itemSubtotal.setText("$" + (cItem.getPrice() * newQty) + " CAD");
                updateQuantityInFirestore(cItem, newQty);
            } else {
                removeItemFromFirestore(cItem);
                cartItems.remove(pos);
                notifyItemRemoved(pos);
            }
        });

    }

    private void removeItemFromFirestore(CartItem item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("carts")
                .document(userId)
                .collection("items")
                .document(item.getId())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("CartAdapter", "Item removed"))
                .addOnFailureListener(e -> Log.e("CartAdapter", "Error removing item", e));
    }

    public void updateQuantityInFirestore(CartItem item, int newQty) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> data = new HashMap<>();
        data.put("id", item.getId());
        data.put("name", item.getName());
        data.put("price", item.getPrice());
        data.put("quantity", newQty);

        db.collection("carts")
                .document(userId)
                .collection("items")
                .document(item.getId())
                .set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("CartAdapter", "Quantity updated"))
                .addOnFailureListener(e -> Log.e("CartAdapter", "Error updating quantity", e));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQty, itemPrice, itemSubtotal, btnPlus, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemQty = itemView.findViewById(R.id.itemQty);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemSubtotal = itemView.findViewById(R.id.itemSubtotal);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}
