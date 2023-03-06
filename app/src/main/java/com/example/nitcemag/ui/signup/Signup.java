package com.example.nitcemag.ui.signup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nitcemag.R;
import com.example.nitcemag.ui.signin.Signin;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.example.nitcemag.UserDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends Fragment {

    private SignupViewModel mViewModel;
    EditText email;
    EditText name;
    EditText password;
    Button signUpBtn;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

//        final TextView textView = root.findViewById(R.id.text_Signup);
//        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        name = root.findViewById(R.id.editTextSignUpName);
        email = root.findViewById(R.id.editTextSignUpEmail);
        password = root.findViewById(R.id.editTextSignUpPassword);
        signUpBtn = root.findViewById(R.id.SignUpBtn);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userEmailId = email.getText().toString();
                String userPassword = password.getText().toString();

                if(userEmailId.isEmpty() && userPassword.isEmpty())
                {
                    Toast.makeText(view.getContext(), "Please enter the Email id and Password", Toast.LENGTH_SHORT).show();
                }
                else if(userEmailId.isEmpty())
                {
                    Toast.makeText(view.getContext(), "Please enter a Email id", Toast.LENGTH_SHORT).show();

                }
                else if(userPassword.isEmpty())
                {
                    Toast.makeText(view.getContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
                }
                else if(Patterns.EMAIL_ADDRESS.matcher(userEmailId).matches())
                {
                    boolean check = checkNitcEmail(userEmailId);
                    if(check){
                        signUpBtn.setClickable(false);
                        signUpFirebase(userEmailId, userPassword);

                    }
                    else
                        Toast.makeText(view.getContext(), "Enter NITC Email id", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(view.getContext(), "Please enter a valid email id", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    private Boolean checkNitcEmail(String userEmailId) {
        String email[] = userEmailId.split("@");
        if(email[1].equals("nitc.ac.in"))
            return true;
        return false;
    }

    private void signUpFirebase(String userEmailId, String userPassword) {

        auth.createUserWithEmailAndPassword(userEmailId,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getContext(), "Your account is created successfully", Toast.LENGTH_LONG).show();
                            UserDetails user = new UserDetails(name.getText().toString(), email.getText().toString());
                            reference.child("User").child(auth.getCurrentUser().getUid()).setValue(user);
                            getChildFragmentManager().popBackStack();
//
//                            Signin signinobj= new Signin();
//                            getActivity().getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.nav_signup, signinobj, "findThisFragment")
//                                    .addToBackStack(null)
//                                    .commit();


//                            Signin fragment2=new Signin();
//                            FragmentManager fragmentManager=getActivity().getFragmentManager();
//                            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.nav_signup,fragment2,"tag");
//                            fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
                        }
                        else
                        {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(getContext(), "This email id is already present", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                System.out.println(task.getException());
                                Toast.makeText(getContext(), "There is a problem, Please try after sometime", Toast.LENGTH_SHORT).show();
                            }
                            signUpBtn.setClickable(true);
                        }
                    }
                });
    }

}