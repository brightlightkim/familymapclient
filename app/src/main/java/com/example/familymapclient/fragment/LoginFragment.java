package com.example.familymapclient.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.familymapclient.R;
import com.example.familymapclient.task.LoginTask;
import com.example.familymapclient.task.RegisterTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.LoginRequest;
import Request.RegisterRequest;


public class LoginFragment extends Fragment {
    private final static String FIRST_NAME = "firstname";
    private final static String LAST_NAME = "lastname";
    private final static String SUCCESS_KEY = "success";

    private EditText serverHost;
    private EditText serverPort;
    private EditText userName;
    private EditText userPassword;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private RadioButton male;
    private RadioButton female;
    private Button loginButton;
    private Button registerButton;

    private Listener listener;

    private View view;

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void notifyDone();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        serverHost = view.findViewById(R.id.serverHost);
        serverPort = view.findViewById(R.id.serverPost);
        userName = view.findViewById(R.id.userName);
        userPassword = view.findViewById(R.id.password);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.emailAddress);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);

        setEditTextListeners();

        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.registerButton);

        setLoginClickListener();
        setRegisterButtonListener();

        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLogin();
        checkRegister();
    }

    private void setEditTextListeners() {
        addEditTextListenerForLogin(serverHost);
        addEditTextListenerForLogin(serverPort);
        addEditTextListenerForLogin(userName);
        addEditTextListenerForLogin(userPassword);
        addEditTextListenerForRegister(firstName);
        addEditTextListenerForRegister(lastName);
        addEditTextListenerForRegister(email);
        addRadioButtonListener(male);
        addRadioButtonListener(female);
    }

    private void addEditTextListenerForLogin(EditText text) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkLogin();
                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addRadioButtonListener(RadioButton button) {
        button.setOnClickListener(v -> checkRegister());
    }

    private void addEditTextListenerForRegister(EditText text) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setLoginClickListener() {

        loginButton.setOnClickListener(v -> {
            LoginRequest request = new LoginRequest(
                    userName.getText().toString(),
                    userPassword.getText().toString());

            Handler uiThreadMessageHandler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();

                    if (bundle.getBoolean(SUCCESS_KEY)) {
                        Toast.makeText(view.getContext(),
                                bundle.getString(FIRST_NAME) + " " + bundle.getString(LAST_NAME),
                                Toast.LENGTH_SHORT).show();
                        listener.notifyDone();
                    } else {
                        Toast.makeText(view.getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            };

            LoginTask login = new LoginTask(uiThreadMessageHandler, request
                    , serverHost.getText().toString(), serverPort.getText().toString());
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(login);
        });
    }

    private void setRegisterButtonListener() {
        registerButton.setOnClickListener(v -> {

            RadioGroup radioGroup;
            RadioButton radioButton;
            radioGroup = view.findViewById(R.id.gender);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = view.findViewById(selectedId);
            String gender = radioButton.getText().toString();

            RegisterRequest request = new RegisterRequest(
                    userName.getText().toString(),
                    userPassword.getText().toString(),
                    email.getText().toString(),
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    gender
            );

            Handler uiThreadMessageHandler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();

                    if (bundle.getBoolean(SUCCESS_KEY)) {
                        Toast.makeText(view.getContext(),
                                bundle.getString(FIRST_NAME) + " " + bundle.getString(LAST_NAME),
                                Toast.LENGTH_SHORT).show();
                        listener.notifyDone();
                    } else {
                        Toast.makeText(view.getContext(), "Register Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            };

            RegisterTask register = new RegisterTask(uiThreadMessageHandler, request
                    , serverHost.getText().toString(), serverPort.getText().toString());
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(register);
        });
    }

    private void checkLogin() {
        loginButton.setEnabled(serverHost.getText().toString().length() != 0 &&
                serverPort.getText().toString().length() != 0 &&
                userName.getText().toString().length() != 0 &&
                userPassword.getText().toString().length() != 0);
    }

    private void checkRegister() {
        registerButton.setEnabled(serverHost.getText().toString().length() != 0 &&
                serverPort.getText().toString().length() != 0 &&
                userName.getText().toString().length() != 0 &&
                userPassword.getText().toString().length() != 0 &&
                email.getText().toString().length() != 0 &&
                firstName.getText().toString().length() != 0 &&
                lastName.getText().toString().length() != 0 &&
                (!male.isSelected() || !female.isSelected()));
    }

}
