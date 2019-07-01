package bao.dev.serverpopeyes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import bao.dev.serverpopeyes.Common.Common;
import bao.dev.serverpopeyes.Model.User;

public class SignIn extends AppCompatActivity {

    MaterialEditText edtPhone,edtPassword;
    TextView txt_btnSignIn;
    DatabaseReference users;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        txt_btnSignIn = findViewById(R.id.signIn_txtSignIn);

//        Init Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        txt_btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(edtPhone.getText().toString(),edtPassword.getText().toString());
//                Toast.makeText(SignIn.this, "SignIn OK", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void signInUser(String phone, String password) {
        final ProgressDialog  mDialog  =  new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please Waiting...");
        mDialog.show();

        final String localPhone = phone;
        final String localPassword = password;

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(localPhone).exists()) {
                    mDialog.dismiss();
                    User user = dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
//                    Toast.makeText(SignIn.this, user.getIfStaff() +"", Toast.LENGTH_LONG).show();
                    if(Boolean.parseBoolean(user.getIfStaff())){
                        if (user.getPassword().equals(localPassword)){
//                            Toast.makeText(SignIn.this, "C", Toast.LENGTH_SHORT).show();
                            //Login OK
                            Common.currentUser = user;
                            Intent homeIntent = new Intent(SignIn.this,Home.class);
                            startActivity(homeIntent);
                            finish();
                        }else {
                            Toast.makeText(SignIn.this, "Your phone & password wrong !!!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(SignIn.this, "Please login with manager account", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this, "Your phone & password was wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

    }
}
