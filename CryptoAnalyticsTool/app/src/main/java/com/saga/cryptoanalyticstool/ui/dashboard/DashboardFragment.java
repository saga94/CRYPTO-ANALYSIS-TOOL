package com.saga.cryptoanalyticstool.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.ExtractedText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.saga.cryptoanalyticstool.R;
import com.saga.cryptoanalyticstool.SignUpActivity;
import com.saga.cryptoanalyticstool.databinding.FragmentDashboardBinding;

import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private Button submit;


    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.transactionId;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        submit = binding.submitButton;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResults();
            }
        });
        return root;
    }
    private String getResults() {
        RequestQueue queue = Volley.newRequestQueue(binding.getRoot().getContext());
        String url = "https://api.blockchair.com/";

        TextInputEditText coin = binding.coin;
        TextInputEditText transaction_id = binding.transactionId;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "/" + coin.getText().toString() + "/raw/transaction/" + transaction_id.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(binding.getRoot().getContext(), "Successfully go the results", Toast.LENGTH_SHORT).show();
                        TextView textView = binding.textView;
                        textView.setText("Bit coin transaction exists");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        TextView textView = binding.textView;
                        textView.setText("Provide valid details");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-CoinAPI-Key", "6A8EBBD4-64A9-4E90-8292-8F44F69C979D");
                return headers;
            }
        };
        queue.add(stringRequest);
        return "OK";
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}