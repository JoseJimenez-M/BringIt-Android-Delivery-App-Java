package com.example.ecommerce.ui.notifications;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce.adapters.NotificationsAdapter;
import com.example.ecommerce.databinding.FragmentNotificationsBinding;
import com.example.ecommerce.models.AppNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private List<AppNotification> notifications;
    private NotificationsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 1️⃣ Inflate the layout using ViewBinding
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);

        // 2️⃣ Initialize your data list
        notifications = new ArrayList<>();
        // Example data (you can remove this later)
        // notifications.add("New message from Alice");

        // 3️⃣ Setup RecyclerView
        adapter = new NotificationsAdapter(notifications);
        binding.recyclerNotifications.setAdapter(adapter);
        binding.recyclerNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        // 4️⃣ Show empty message if list is empty
        toggleEmptyView();

        return binding.getRoot();
    }

    // Helper method to toggle empty view visibility
    private void toggleEmptyView() {
        if (notifications.isEmpty()) {
            binding.txtEmpty.setVisibility(View.VISIBLE);
            binding.recyclerNotifications.setVisibility(View.GONE);
        } else {
            binding.txtEmpty.setVisibility(View.GONE);
            binding.recyclerNotifications.setVisibility(View.VISIBLE);
        }
    }

    // Optional: Call this whenever you add/remove notifications
    private void addNotification(String message) {
        String id = UUID.randomUUID().toString();
        notifications.add(new AppNotification(id,"Notification", message, System.currentTimeMillis()));
        adapter.notifyItemInserted(notifications.size() - 1);

        toggleEmptyView();
    }
}
