package com.example.ecommerce.ui.payment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ecommerce.R;
import com.example.ecommerce.models.AppNotification;
import com.example.ecommerce.ui.notifications.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;

import java.util.UUID;

public class PaymentFragment extends Fragment {

    private EditText etCardName, etCardNumber, etExpiry, etCVV;
    private TextView tvCardNumberPreview, tvExpiryPreview, tvResponse;
    private Button btnPayNow;

    private NotificationHelper notificationHelper;

    private Stripe stripe;
    private static final String STRIPE_PUBLISHABLE_KEY = "pk_test_51SIDS9ReWpqRBYVgzpWRj0fW4dgf0EtGL8yxbLyBR42e971r5jGARt9CWea6NBIpIDv9JLpSjAPpJ9NuSUlfA3Cu00iNB9fv1e";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        //Initialize Notification
        notificationHelper = new NotificationHelper(requireContext());

        // Initialize Stripe
        PaymentConfiguration.init(requireContext(), STRIPE_PUBLISHABLE_KEY);
        stripe = new Stripe(requireContext(), STRIPE_PUBLISHABLE_KEY);

        // Bind views
        etCardName = view.findViewById(R.id.etCardName);
        etCardNumber = view.findViewById(R.id.etCardNumber);
        etExpiry = view.findViewById(R.id.etExpiry);
        etCVV = view.findViewById(R.id.etCVV);
        tvCardNumberPreview = view.findViewById(R.id.tvCardNumberPreview);
        tvExpiryPreview = view.findViewById(R.id.tvExpiryPreview);
        btnPayNow = view.findViewById(R.id.btnPayNow);


        setupWatchers();
        setupPayButton();

        return view;
    }

    private void setupWatchers() {
        // Card number formatting
        etCardNumber.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().replaceAll("\\s", "");
                if (!input.equals(current)) {
                    current = input;
                    StringBuilder formatted = new StringBuilder();
                    for (int i = 0; i < input.length(); i++) {
                        if (i > 0 && i % 4 == 0) formatted.append(" ");
                        formatted.append(input.charAt(i));
                    }
                    etCardNumber.removeTextChangedListener(this);
                    etCardNumber.setText(formatted.toString());
                    etCardNumber.setSelection(formatted.length());
                    etCardNumber.addTextChangedListener(this);
                    tvCardNumberPreview.setText(formatted.toString());
                }
            }
        });

        // Expiry formatting
        etExpiry.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().replaceAll("[^\\d]", "");
                if (!input.equals(current)) {
                    current = input;
                    StringBuilder formatted = new StringBuilder();
                    for (int i = 0; i < input.length() && i < 4; i++) {
                        if (i == 2) formatted.append("/");
                        formatted.append(input.charAt(i));
                    }
                    etExpiry.removeTextChangedListener(this);
                    etExpiry.setText(formatted.toString());
                    etExpiry.setSelection(formatted.length());
                    etExpiry.addTextChangedListener(this);
                    tvExpiryPreview.setText(formatted.toString());
                }
            }
        });
    }

    private void setupPayButton() {
        btnPayNow.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        String cardName = etCardName.getText().toString().trim();
        String cardNumber = etCardNumber.getText().toString().replaceAll("\\s", "");
        String expiry = etExpiry.getText().toString().trim();
        String cvv = etCVV.getText().toString().trim();

        if (cardName.isEmpty() || cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!expiry.matches("(0[1-9]|1[0-2])\\/\\d{2}")) {
            Toast.makeText(requireContext(), "Expiry format must be MM/YY", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] exp = expiry.split("/");
        String expMonth = exp[0];
        String expYear = "20" + exp[1];

        PaymentMethodCreateParams.Card card;
        try {
            card = new PaymentMethodCreateParams.Card.Builder()
                    .setNumber(cardNumber)
                    .setExpiryMonth(Integer.parseInt(expMonth))
                    .setExpiryYear(Integer.parseInt(expYear))
                    .setCvc(cvv)
                    .build();
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid expiry date format.", Toast.LENGTH_SHORT).show();
            return;
        }

        PaymentMethod.BillingDetails billing = new PaymentMethod.BillingDetails.Builder()
                .setName(cardName)
                .build();

        PaymentMethodCreateParams params = PaymentMethodCreateParams.create(card, billing);

        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod paymentMethod) {
                if (!isAdded()) return; // prevent crash if fragment is detached

                String id = paymentMethod.id;
                String funding = paymentMethod.card.funding;
                String last4 = paymentMethod.card.last4;

                String titleNotification ="✅Payment Complete";
                String messageNotification = "Type: "+funding+"\tCard: "+ last4;

                Log.d("StripeResponse", paymentMethod.toString());
                Toast.makeText(requireContext(),
                        "✅ Payment completed! ID: " + id,
                        Toast.LENGTH_LONG).show();

                //send notification
                notificationHelper.sendNotification(titleNotification,messageNotification,1);

                //send notification to the logs
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String uid = currentUser.getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    String notifId = UUID.randomUUID().toString();
                    AppNotification notif = new AppNotification(
                            notifId,
                            titleNotification,
                            messageNotification,
                            System.currentTimeMillis()
                    );

                    db.collection("users")
                            .document(uid)
                            .collection("notifications")
                            .document(notifId)
                            .set(notif);
                }

                // Navigate back to HomeFragment
                NavHostFragment.findNavController(PaymentFragment.this)
                        .navigate(R.id.navigation_home); // just use the fragment ID

            }

            @Override
            public void onError(@NonNull Exception e) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(),
                        "❌ Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e("StripeError", e.getMessage(), e);
            }
        });
    }
}
