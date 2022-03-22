package com.example.familymapclient.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.familymapclient.server.ServerProxy;
import com.example.familymapclient.task.LoginTask;
import com.example.familymapclient.task.RegisterTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;


public class LoginFragment extends Fragment {
    private final static String FIRST_NAME = "firstname";
    private final static String LAST_NAME = "lastname";
    private final static String SUCCESS_KEY = "success";

    private LoginViewModel mViewModel;

    private final String TAG = "MainActivity";
    private final String KEY_INDEX = "index";

    private ServerProxy server;
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

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
        /**
         * This can be changed later.
         * It's for the testing
         */
        serverHost.setText("10.37.11.53");
        serverPort.setText("8080");
        userName.setText("sheila");
        userPassword.setText("parker");
        firstName.setText("Grace");
        lastName.setText("Grace");
        email.setText("k2289@byu.edu");
        male.toggle();

        setEditTextListeners();

        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.registerButton);

        setLoginClickListener();
        setRegisterButtonListener();

        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        return view;
    }

    private void setEditTextListeners(){
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

    private void addEditTextListenerForLogin(EditText text){
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

    private void addRadioButtonListener(RadioButton button){
        button.setOnClickListener(v -> {
            checkRegister();
        });
    }

    private void addEditTextListenerForRegister(EditText text){
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

    private void setLoginClickListener(){

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

    private void setRegisterButtonListener(){
        registerButton.setOnClickListener(v -> {

            RadioGroup radioGroup;
            RadioButton radioButton;
            radioGroup = (RadioGroup) view.findViewById(R.id.gender);
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            radioButton = (RadioButton) view.findViewById(selectedId);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(LoginViewModel.class);

    }

}

/**
 * String text is the parameter
 * TEXT_KEY is defined in the beginning of the LoginFragment
 * Send Data to Fragment
 * <p>
 * LoginFragment fragment = new LoginFragment();
 * Bundle arguments = new Bundle(); //Map >> Key Value pairs
 * arguments.putString(LoginFragment.TEXT_KEY, text);
 * fragment.setArguments(arguments);
 * <p>
 * return fragment
 * <p>
 * Where Receiving
 * if (getArgument() != null){
 * TextView textView = view.findViewById(R.id.blabla);
 * String receivedText = getArguments().getString(TEXT_KEY);
 * textView.setText(receivedText);
 */