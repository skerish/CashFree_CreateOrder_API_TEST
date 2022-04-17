package com.example.cashfree_single_api_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static String clientId = "$YOUR_UNIQUE_CLIENT_ID_HERE";
    public static String clientSecret = "$YOUR_UNIQUE_CLIENT_SECRET_KEY_HERE";

    public static String contentType = "application/json";
    public static String apiVersion = "2022-01-01";

    // TEST URL
    public static String URL = "https://sandbox.cashfree.com/pg/orders";

    EditText orderId, orderAmount, orderCurrency, customerId, customerEmail, customerPhone;
    Button createClient;
    LinearLayout linearLayout;
    TextView apiResponseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        orderId = findViewById(R.id.orderId);
        orderAmount = findViewById(R.id.orderAmount);
        orderCurrency = findViewById(R.id.orderCurrency);
        customerId = findViewById(R.id.customerId);
        customerEmail = findViewById(R.id.customerEmail);
        customerPhone = findViewById(R.id.customerPhone);
        createClient = findViewById(R.id.create_client);
        linearLayout = findViewById(R.id.layout1);
        apiResponseText = findViewById(R.id.layout2);

        createClient.setOnClickListener((view) -> {
            try {
                Response response = createNewOrder(
                        orderId.getText().toString(),
                        orderAmount.getText().toString(),
                        orderCurrency.getText().toString(),
                        customerId.getText().toString(),
                        customerEmail.getText().toString(),
                        customerPhone.getText().toString()
                );
                showResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showResponse(Response response) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(response.body().string());
        String jsonString = gson.toJson(je);

        System.out.println("$$$$$$$$$$JSON RESPONSE$$$$$$$$$" + jsonString);
        apiResponseText.setText(jsonString);
        linearLayout.setVisibility(View.GONE);
        apiResponseText.setVisibility(View.VISIBLE);
    }

    private Response createNewOrder(String orderId, String amount, String currency,
                                String customerId, String email, String phone) throws IOException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse(contentType);

        RequestBody body = RequestBody.create(mediaType, "{\"customer_details\":{\"customer_id\":\"" + customerId +  "\",\"customer_email\":\"" + email +  "\",\"customer_phone\":\"" + phone + "\"},\"order_id\":\"" + orderId + "\",\"order_amount\":" + amount + ",\"order_currency\":\"" + currency + "\"}");

        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("Accept", contentType)
                .addHeader("x-client-id", clientId)
                .addHeader("x-client-secret", clientSecret)
                .addHeader("x-api-version", apiVersion)
                .addHeader("Content-Type", contentType)
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }
}