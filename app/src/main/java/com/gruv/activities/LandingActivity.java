package app.src.main.java.com.gruv.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gruv.models.Author;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class LandingActivity extends AppCompatActivity {
    protected static ConstraintLayout
            layoutLoginStart, layoutLoginEmail,
            layoutRegister, layoutForgotPassword,
            layoutEnterVerifyCode, layoutProgress,
            layoutResetPassword, layoutAddPicture;
    ImageView imageFacebook;

    private CallbackManager mCallbackManager;
    private FirebaseAuth authenticateObj;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private TextInputLayout layoutEmailText, layoutPasswordText, layoutPasswordTextRegister, layoutEmailRegisterText;
    private TextInputEditText textEmail, textPassword, editTextEmail, editTextPassword, editTextName, editTextConfirmPassword;
    private TextView textSignUp, textForgotPassword, textViewSignUp;
    private MaterialButton buttonEmail, buttonSignIn, buttonRegister, buttonNext1, buttonNext, buttonResetPassword, buttonAddPicture, buttonSkip;
    private FloatingActionButton buttonChoosePicture;
    private CircleImageView imageProfilePicture;
    private ProgressBar progressLanding;
    private Uri filePath;
    private Author thisUser = new Author();
    private final int PICK_IMAGE_REQUEST = 71;
    private boolean emailCorrect, passwordCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        initializeControls();
        //getCurrentUser();

        buttonEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layoutLoginEmail.setVisibility(View.VISIBLE);
                layoutLoginStart.setVisibility(View.GONE);
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutRegister.setVisibility(View.VISIBLE);
                layoutLoginStart.setVisibility(View.GONE);
            }
        });

        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLoginEmail.setVisibility(View.GONE);
                layoutForgotPassword.setVisibility(View.VISIBLE);
            }
        });

        textEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEmail(s, layoutEmailText, buttonSignIn);
            }
        });

        textPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validatePassword(editable, layoutPasswordText, buttonSignIn);
            }
        });



        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                validateEmail(s, layoutEmailRegisterText, buttonRegister);
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validatePassword(editable, layoutPasswordTextRegister, buttonRegister);
            }
        });


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Add sign up authorisation code
                registerUserWithEmailAndPassword();
                //finish();
            }
        });

        buttonNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Add get verification email code
                layoutForgotPassword.setVisibility(View.GONE);
                layoutEnterVerifyCode.setVisibility(View.VISIBLE);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Add verification code match code
                layoutEnterVerifyCode.setVisibility(View.GONE);
                layoutResetPassword.setVisibility(View.VISIBLE);
            }
        });

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Add reset password code
                layoutResetPassword.setVisibility(View.GONE);

                finish();
            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonChoosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        buttonAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (layoutLoginEmail.getVisibility() == View.VISIBLE) {
            layoutLoginEmail.setVisibility(View.GONE);
            layoutLoginStart.setVisibility(View.VISIBLE);
        } else if (layoutRegister.getVisibility() == View.VISIBLE) {
            layoutRegister.setVisibility(View.GONE);
            layoutLoginStart.setVisibility(View.VISIBLE);
        } else if (layoutForgotPassword.getVisibility() == View.VISIBLE) {
            layoutForgotPassword.setVisibility(View.GONE);
            layoutLoginEmail.setVisibility(View.VISIBLE);
        } else if (layoutEnterVerifyCode.getVisibility() == View.VISIBLE) {
            layoutEnterVerifyCode.setVisibility(View.GONE);
            layoutForgotPassword.setVisibility(View.VISIBLE);
        } else if (layoutResetPassword.getVisibility() == View.VISIBLE) {
            layoutResetPassword.setVisibility(View.GONE);
            layoutEnterVerifyCode.setVisibility(View.VISIBLE);
        } else if (layoutAddPicture.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            this.finishAffinity();
        }
    }

    public void imgFacebookLogin(View v) {
        Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
        startActivity(intent);
    }

    public boolean login() {
        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();
        boolean result = false;

        if (connectionAvailable()) {
            showProgressBar();
            authenticateObj = FirebaseAuth.getInstance();
            currentUser = authenticateObj.getCurrentUser();


            if (!email.isEmpty() && !password.isEmpty()) {
                try {
                    authenticateObj.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                currentUser = authenticateObj.getCurrentUser();
                                hideProgressBar();
                                finish();
                            } else {
                                hideProgressBar();
                                showSnackBar("Incorrect email or password", R.id.layoutParent, Snackbar.LENGTH_SHORT);
                            }
                        }
                    });
                } catch (Exception e) {
                    hideProgressBar();
                    showSnackBar("Something went wrong", R.id.layoutParent, Snackbar.LENGTH_SHORT);
                    e.printStackTrace();
                }
                result = true;
            } else {
                hideProgressBar();
                showSnackBar("Enter Email and Password", R.id.layoutParent, Snackbar.LENGTH_SHORT);
            }
        } else {
            hideProgressBar();
            showSnackBar("Check internet connection", R.id.layoutParent, Snackbar.LENGTH_SHORT);
        }

        return result;
    }

    public void imgTwitterGenericLogin(View v) {
        Intent intent = new Intent(getApplicationContext(), GenericIdpActivity.class);
        startActivity(intent);
    }

    private boolean connectionAvailable() {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                connected = true;
            } else {
                connected = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return connected;
    }


    private void getCurrentUser() {
        authenticateObj = FirebaseAuth.getInstance();
        currentUser = authenticateObj.getCurrentUser();

        if (currentUser != null) {
            layoutLoginStart.setVisibility(View.VISIBLE);
            finish();
        }
    }

    private void initializeControls() {
        //controls
        //landing
        buttonEmail = findViewById(R.id.buttonEmail);
        buttonSignIn = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonNext1 = findViewById(R.id.buttonNext1);
        buttonNext = findViewById(R.id.buttonNext);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
        imageFacebook = findViewById(R.id.imageFacebook);
        textForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        progressLanding = findViewById(R.id.progressLanding);

        //login
        textEmail = findViewById(R.id.editTextEmailLogin);
        textPassword = findViewById(R.id.editTextPasswordLogin);
        textSignUp = findViewById(R.id.textViewSignUp);
        layoutEmailText = findViewById(R.id.editTextLayoutEmail);
        layoutPasswordText = findViewById(R.id.editTextLayoutPassword);

        //register
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        editTextName = findViewById(R.id.editTextName);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        layoutEmailRegisterText = findViewById(R.id.editTextLayoutEmailRegister);
        layoutPasswordTextRegister = findViewById(R.id.editTextLayoutPasswordRegister);
        editTextPassword.setInputType(129);
        editTextConfirmPassword.setInputType(129);

        //add image
        imageProfilePicture = findViewById(R.id.imagePicture);
        buttonChoosePicture = findViewById(R.id.fabChoose);
        buttonAddPicture = findViewById(R.id.buttonAddPicture);
        buttonSkip = findViewById(R.id.buttonSkip);

        //layouts
        layoutLoginStart = findViewById(R.id.constraintLayoutLoginStart);
        layoutLoginEmail = findViewById(R.id.constraintLayoutLogin);
        layoutRegister = findViewById(R.id.constraintLayoutRegister);
        layoutForgotPassword = findViewById(R.id.constraintLayoutForgotPassword);
        layoutEnterVerifyCode = findViewById(R.id.constraintLayoutEnterVerifyCode);
        layoutResetPassword = findViewById(R.id.constraintLayoutResetPassword);
        layoutProgress = findViewById(R.id.constraintLayoutProgressBar);
        layoutAddPicture = findViewById(R.id.constraintLayoutAddImage);

        //firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void showSnackBar(String message, Integer layout, Integer length) {
        View contextView = findViewById(layout);
        Snackbar.make(contextView, message, length).show();
    }

    public void validateEmail(Editable s, TextInputLayout layout, MaterialButton button) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-zA-Z._-]+[a-zA-Z._-]";
        // onClick of button perform this simplest code.
        if (!s.toString().trim().matches(emailPattern)) {
            emailCorrect = false;
            layout.setError("Invalid email");
            button.setClickable(false);
            button.setTextColor(ContextCompat.getColor(getApplicationContext(), (R.color.grey)));
        } else {
            emailCorrect = true;
            layout.setError(null);
            if (passwordCorrect) {
                button.setClickable(true);
                button.setTextColor(ContextCompat.getColor(getApplicationContext(), (R.color.colorPrimary)));
            }
        }
    }

    public void validatePassword(Editable editable, TextInputLayout layout, MaterialButton button) {
        if (editable.length() < 6) {
            passwordCorrect = false;
            layout.setError("Password must have more than 6 characters");
            button.setClickable(false);
            button.setTextColor(ContextCompat.getColor(getApplicationContext(), (R.color.grey)));
        } else {
            layout.setError(null);
            passwordCorrect = true;
            if (emailCorrect) {
                button.setClickable(true);
                button.setTextColor(ContextCompat.getColor(getApplicationContext(), (R.color.colorPrimary)));
            }
        }
    }

    public void registerUserWithEmailAndPassword() {

        authenticateObj = FirebaseAuth.getInstance();
        layoutRegister.setVisibility(View.VISIBLE);

        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        showProgressBar();
        if (connectionAvailable()) {
            if (name.length() == 0
                    || email.length() == 0
                    || password.length() == 0
                    || confirmPassword.length() == 0) {
                hideProgressBar();
                showSnackBar("Enter all Fields", R.id.layoutParent, Snackbar.LENGTH_SHORT);
            } else {
                if (!confirmPassword.equals(password)) {
                    hideProgressBar();
                    showSnackBar("Password must match", R.id.layoutParent, Snackbar.LENGTH_SHORT);
                    //Toast.makeText(getApplicationContext(), "Password must match", Toast.LENGTH_LONG).show();
                } else {
                    authenticateObj.createUserWithEmailAndPassword(email, password).
                            addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        currentUser = authenticateObj.getCurrentUser();
                                        showSnackBar("Account created!", R.id.layoutParent, Snackbar.LENGTH_LONG);

                                        thisUser.setId(currentUser.getUid());
                                        thisUser.setName(name);
                                        thisUser.setEmail(email);

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(thisUser.getName())
                                                .build();

                                        currentUser.updateProfile(profileUpdates);
                                        authenticateObj.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    hideProgressBar();
                                                    currentUser = authenticateObj.getCurrentUser();
                                                } else {
                                                    hideProgressBar();
                                                    showSnackBar("Something went wrong", R.id.layoutParent, Snackbar.LENGTH_SHORT);
                                                }
                                            }
                                        });
                                        layoutRegister.setVisibility(View.GONE);
                                        layoutAddPicture.setVisibility(View.VISIBLE);

                                    } else {
                                        hideProgressBar();
                                        // If sign in fails, display a message to the user.
                                        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        showSnackBar("Something went wrong", R.id.layoutParent, Snackbar.LENGTH_SHORT);
                                    }
                                }
                            });
                }
            }
        } else {
            hideProgressBar();
            showSnackBar("Check internet connection", R.id.layoutParent, Snackbar.LENGTH_SHORT);
        }
    }

    private void showProgressBar() {
        progressLanding.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressLanding.setVisibility(View.INVISIBLE);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                showSnackBar("Something went wrong", R.id.layoutParent, Snackbar.LENGTH_LONG);
                e.printStackTrace();
            }
        }

    }


    private void uploadImage() {

        if (filePath != null) {
            final ProgressBar progressDialog = findViewById(R.id.progressLandingPercentage);
            progressDialog.setVisibility(View.VISIBLE);
            String path = currentUser.getUid() + "/profilePicture/picture";
            StorageReference ref = storageReference.child(path);

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.setVisibility(View.INVISIBLE);
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    updateUser(uri, progressDialog);
                                }
                            });



                            showSnackBar("Done!", R.id.layoutParent, Snackbar.LENGTH_LONG);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            progressDialog.setVisibility(View.INVISIBLE);
                            showSnackBar("Something went wrong", R.id.layoutParent, Snackbar.LENGTH_SHORT);
                            e.printStackTrace();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setProgress((int) progress);
                        }
                    });
        }
    }

    private void updateUser(Uri uri, ProgressBar progressDialog) {
        thisUser.setAvatar(uri.toString());
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.setVisibility(View.INVISIBLE);
                        if (!task.isSuccessful()) {
                            showSnackBar("Couldn't update picture", R.id.layoutParent, Snackbar.LENGTH_SHORT);
                        }
                    }
                });
    }


}

