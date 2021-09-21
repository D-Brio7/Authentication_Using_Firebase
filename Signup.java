package com.example.admin.brios;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {

    EditText email,password;
    TextView Login;
    Button register;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        Login = findViewById(R.id.text3);
        register = (Button) findViewById(R.id.btn_submit);
        email = (EditText) findViewById(R.id.txt_email);
        password = (EditText) findViewById(R.id.txt_pass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, UserLogin.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_email = email.getText().toString().trim();
                String txt_password = password.getText().toString().trim();

                if (TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(txt_email, txt_password)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Signup.this, "Account Created Successfully : " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Signup.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    Intent i = new Intent(Signup.this,Home.class);
                                    i.putExtra("email",txt_email);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    /*void registeruser()
    {
        boolean isValid = true;
        if (isEmpty(name))
        {
            name.setError("You must enter your name to login!");
            isValid = false;
        }

        if (isEmpty(address))
        {
            address.setError("You must enter your address to login!");
            isValid = false;
        }

        if (isEmpty(email))
        {
            email.setError("You must enter email to login!");
            isValid = false;
        }
        else
        {
            if (!isEmail(email))
            {
                email.setError("Enter valid email!");
                isValid = false;
            }
        }

        if (isEmpty(password))
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

        if (isEmpty(contact))
        {
            contact.setError("You must enter your contact to login!");
            isValid = false;
        } else
        {
            if (!isContact(contact))
            {
                password.setError("Invalid Contact!");
                isValid = false;
            }
        }

        //check email and password
        //IMPORTANT: here should be call to backend or safer function for local check; For example simple check is cool
        //For example simple check is cool
        if (isValid)
        {
            dblog = new LoginDBAdapter(Signup.this);
            String uname = name.getText().toString();
            String uemail = email.getText().toString();
            String ucontact = contact.getText().toString();
            String uaddress = address.getText().toString();
            String upassword = password.getText().toString();
            if (!dblog.checkUser(uemail))
            {
                dblog.addUser(uname,uemail,ucontact,uaddress,md5(upassword));
                //everything checked we open new activity
                Intent i = new Intent(Signup.this, Home.class);
                startActivity(i);
                //we close this activity
                this.finish();
            }
            else
            {
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Email already registered!", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.orange));
                snackbar.show();
            }
        }
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isContact(EditText text) {
        CharSequence contact = text.getText().toString();
        return (!TextUtils.isEmpty(contact) && Patterns.PHONE.matcher(contact).matches());
    }

    boolean isEmpty(EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public static final String md5(final String s)
    {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }*/
}
