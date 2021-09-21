package com.example.admin.brios;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserLogin extends AppCompatActivity
{
    TextView signup,forgotpass;
    RelativeLayout relativeLayout;
    EditText email,password;
    Button submit;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    LoginDBAdapter dblog;
    CheckBox rememberme;
    String message;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_userlogin);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(UserLogin.this, Home.class));
            finish();
        }

        relativeLayout = (RelativeLayout)findViewById(R.id.relativelayout);
        forgotpass = (TextView)findViewById(R.id.text6);
        signup = (TextView)findViewById(R.id.text3);
        email = (EditText)findViewById(R.id.et1);
        password = (EditText)findViewById(R.id.et2);
        submit = (Button)findViewById(R.id.b1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rememberme = (CheckBox)findViewById(R.id.rememberme);
        auth = FirebaseAuth.getInstance();
        /*loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            email.setText(loginPreferences.getString("uemail", ""));
            password.setText(loginPreferences.getString("password", ""));
            rememberme.setChecked(true);
        }
        rememberme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUsername();
            }
        });*/
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(UserLogin.this,Signup.class);
                startActivity(i);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                final String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(txt_email, txt_password)
                        .addOnCompleteListener(UserLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        password.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(UserLogin.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent i = new Intent(UserLogin.this,Home.class);
                                    i.putExtra("email",txt_email);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
            }
        });
        SpannableString content = new SpannableString("Forgot Password?");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        forgotpass.setText(content);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(UserLogin.this,ForgotPassword.class);
                startActivity(i);
            }
        });
    }

    /*void checkUsername()
    {
        boolean isValid = true;
        if (isEmpty(email))
        {
            email.setError("You must enter email/contact to login!");
            isValid = false;
        }
        else
        {
            Pattern mobilepattern = Pattern.compile("^[7-9][0-9]{9}$");
            String input = email.getText().toString();
            if(Patterns.EMAIL_ADDRESS.matcher(input).matches()||mobilepattern.matcher(input).matches())
            {
                isValid = true;
            }
        }

        /*if (isEmpty(email))
        {
            email.setError("You must enter email/contact to login!");
            isValid = false;
        }
        else
        {
            if (!isContact(email))
            {
                email.setError("Enter valid email/contact!");
                isValid = false;
            }
        }*/

        /*if (isEmpty(password))
        {
            password.setError("You must enter password to login!");
            isValid = false;
        } else
        {
            if (password.getText().toString().length() < 5)
            {
                password.setError("Password must be at least 5 chars long!");
                isValid = false;
            }
        }

        //check email and password
        //IMPORTANT: here should be call to backend or safer function for local check; For example simple check is cool
        //For example simple check is cool
        if (isValid)
        {
            dblog = new LoginDBAdapter(UserLogin.this);
            String uemail = email.getText().toString();
            String upassword = password.getText().toString();
            String dbpass = dblog.getSinlgeEntry(uemail);
            upassword = md5(upassword);
            if (dblog.checkUser(uemail))
            {
                if(upassword.equals(dbpass))
                {
                    donext(uemail,upassword);
                }
                else
                {
                    message="Incorrect email/contact or password!";
                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                            message, Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.orange));
                    snackbar.show();
                }
            }
            else
            {
                message = "Hey, your aren't Registered?";
                showsnackbarregister(message);
            }
        }
    }

    public static final String md5(final String s)
    {
        final String MD5 = "MD5";
        try
        {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
            {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public void doSomethingElse()
    {
        startActivity(new Intent(UserLogin.this, Home.class));
        UserLogin.this.finish();
    }

    boolean isEmail(EditText text)
    {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isContact(EditText text)
    {
        Pattern mobilepattern = Pattern.compile("^[7-9][0-9]{9}$");
        CharSequence contact = text.getText().toString();
        return (mobilepattern.matcher(contact).matches());
    }

    boolean isEmpty(EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void showsnackbarregister(String msg)
    {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
                .setAction("SIGNUP", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(UserLogin.this,Signup.class);
                        startActivity(i);
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.orange));
        snackbar.show();
    }

    public void donext(String uemail, String upassword)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
        if (rememberme.isChecked())
        {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("uemail", uemail);
            loginPrefsEditor.putString("password", upassword);
            loginPrefsEditor.commit();
        }
        else
        {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
        doSomethingElse();
        //everything checked we open new activity
        Intent i = new Intent(UserLogin.this, Home.class);
        startActivity(i);
        //we close this activity
        this.finish();
    }*/
}



