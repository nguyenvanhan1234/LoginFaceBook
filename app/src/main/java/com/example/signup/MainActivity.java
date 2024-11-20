package com.example.signup;


import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.common.AccountPicker;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class MainActivity extends AppCompatActivity {
    private EditText emailEditText;
    private ImageView facebookIcon;
    private CallbackManager callbackManager;
    private static final int REQUEST_CODE_PICK_ACCOUNT = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Facebook SDK
        callbackManager = CallbackManager.Factory.create();

        // Find views
        emailEditText = findViewById(R.id.emailEditText);
        facebookIcon = findViewById(R.id.facebookIcon);
        findViewById(R.id.googleIcon).setOnClickListener(v -> chooseAccount());
        // Facebook Login Button Click Listener
        findViewById(R.id.continueButton).setOnClickListener(v -> openNextActivity());
        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFacebook();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Dữ liệu mẫu
        List<WordItem> wordList = new ArrayList<>();
        wordList.add(new WordItem("Elated", "Phấn khích, hân hoan, rất vui mừng."));
        wordList.add(new WordItem("Ecstatic", "Cực kỳ vui sướng, ngây ngất."));

        WordAdapter adapter = new WordAdapter(this, wordList);
        recyclerView.setAdapter(adapter);
    }
    private void chooseAccount() {
        Intent intent = AccountPicker.newChooseAccountIntent(
                null, null, new String[]{"com.google"}, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    private void loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Get the email from Facebook
                getUserEmail();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserEmail() {
        // Fetch user data from Facebook's Graph API
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String email = object.getString("email");
                    emailEditText.setText(email);  // Fill the emailEditText with the fetched email
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error fetching email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Xử lý phản hồi của AccountPicker cho Google
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT && resultCode == RESULT_OK && data != null) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            emailEditText.setText(accountName);  // Điền email vào EditText
        }
    }
    private void openNextActivity() {
        Intent intent = new Intent(MainActivity.this, NextActivity.class);
        startActivity(intent);
    }
}
