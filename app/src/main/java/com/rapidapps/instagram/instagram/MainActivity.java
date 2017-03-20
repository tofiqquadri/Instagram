package com.rapidapps.instagram.instagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rapidapps.instagram.instagram.instagramposthome.InstagramHome;

public class MainActivity extends AppCompatActivity {

    //FIELDS FOR VIEWS AND WIDGETS
    EditText userEmailEditText, userPasswordEditText;
    LinearLayout loginLayoutBtn, createAccountLayoutBtn;

    //FIREBASE AUTHENTICATION FIELDS
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //PROGRESS DIALOG
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ASSIGN ID
        userEmailEditText = (EditText) findViewById(R.id.emailLoginEditText);
        userPasswordEditText = (EditText) findViewById(R.id.passwordLoginEditText);

        loginLayoutBtn = (LinearLayout) findViewById(R.id.loginButtonMain);
        createAccountLayoutBtn = (LinearLayout) findViewById(R.id.createAccountButtonMain);

        //PROGRESS DIALOG CONTEXT
        mProgressDialog = new ProgressDialog(this);

        //FIREBASE AUTHENTICATION INSTANCES
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //CHECKING USER PRESENCE
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if( user != null )
                {

                    Intent moveToHome = new Intent(MainActivity.this, InstagramHome.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);

                }

            }
        };

        mAuth.addAuthStateListener(mAuthListener);


        //SET ON CLICK LISTENER ON OUR CREATE ACCOUNT LAYOUT BUTTON
        createAccountLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, RegisterUserActivity.class));

            }
        });

        //SET ON CLICK LISTENER FOR LOGIN BTN
        loginLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Loging in the user");
                mProgressDialog.setMessage("Please wait....");
                mProgressDialog.show();
                loginUser();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void loginUser() {

       String userEmail, userPassword;

        userEmail = userEmailEditText.getText().toString().trim();
        userPassword = userPasswordEditText.getText().toString().trim();

        if( !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword))
        {

            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if( task.isSuccessful())
                    {

                        mProgressDialog.dismiss();
                        Intent moveToHome = new Intent(MainActivity.this, InstagramHome.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);

                    }else
                    {

                        Toast.makeText(MainActivity.this, "Unable to login user", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();

                    }

                }
            });

        }else
        {

            Toast.makeText(MainActivity.this, "Please enter user email and password", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
