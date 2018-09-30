package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity {

    EditText edPass;
    EditText edEmail;
    ConstraintLayout loginPage;
    ConstraintLayout registerPage;
    EditText inputEmail;
    EditText inputPassword;
    EditText secondInputPassword;


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Check if there is an active session with firebase and user is logged in:
        if(mUser!=null){
            startActivity(new Intent(MainActivity.this,MainScreen.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
        }
        //If no user is logged in, show login screen:
        else {

            //Initialize elements:
            edEmail = findViewById(R.id.mainA_loginEmail_editText);
            edPass = findViewById(R.id.mainA_loginPass_editText);
            loginPage = findViewById(R.id.mainA_loginLayout_cLayoutLogin);
            registerPage = findViewById(R.id.mainA_registrerLayout_cLayoutLogin);
            inputEmail = findViewById(R.id.mainA_registrerEmail_editText);
            inputPassword = findViewById(R.id.mainA_registrerPass_editText);
            secondInputPassword = findViewById(R.id.mainA_registrerPassConfirm_editText);
            registerPage = findViewById(R.id.mainA_registrerLayout_cLayoutLogin);

            //TODO 1.4 Make horizontal layout (Non priority)

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        getSupportActionBar().setTitle("RusleTur");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO 1.0: Add button action for actionbar

        switch(item.getItemId()) {
            //TODO 1.1: Add action for home button
            case R.id.action_home:
                //Intent intent = new Intent(this, home.class);
                //startActivity(intent);
                //return true;
                writeMessageToUser("Home clicked");

                //TODO 1.2: Add action for settings button
            case R.id.action_settings:
                //Intent intent = new Intent(this, settings.class);
                //startActivity(intent);
                //return true;
                writeMessageToUser("Settings clicked");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Login user from input values:
    public void loginUser(View view){
        //TODO Animate login process

        if(checkForValidUserInput(1)){
            //Login in user with input values:
            mAuth.signInWithEmailAndPassword(edEmail.getText().toString()
                    ,edPass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Send user to second screen
                                startActivity(new Intent(MainActivity.this,MainScreen.class));
                            }else{
                                //If the login process is interrupted: warn user:
                                writeMessageToUser(task.getException().toString());
                            }
                        }
                    });
        }
    }

    //When registration is pushed:
    public void registerUser(View view){
        //Hide login page
        if(checkForValidUserInput(2)){
            mAuth.createUserWithEmailAndPassword(inputEmail.getText().toString(),
                    inputPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                writeMessageToUser("Du er registrert :)");
                                registerPage.setVisibility(View.INVISIBLE);
                                loginPage.setVisibility(View.VISIBLE);
                                edEmail.setText(inputEmail.getText());
                            }else{
                                writeMessageToUser(task.getException().toString());
                            }
                        }
                    });
        }

    }
    public void goToRegistration(View view){
        loginPage.setVisibility(View.INVISIBLE);
        registerPage.setVisibility(View.VISIBLE);
    }
    public void cancleRegistration(View view) {
        loginPage.setVisibility(View.VISIBLE);
        registerPage.setVisibility(View.INVISIBLE);
    }

    //Check if field values entered are correct:
    //Option 1 checks loginpage for valid input
    //Option 2 checks registerpage for valid input
    private boolean checkForValidUserInput(int option){

        if(option==1) {
            if ((!edEmail.getText().toString().isEmpty() && edEmail.toString().length() > 0)
                    && (edPass.getText().toString() != "")) {
                if (edPass.getText().toString().length() > 5) {
                    return true;
                } else {
                    //TODO Write static variable
                    writeMessageToUser("Passordet er for kort.");
                    return false;
                }
            } else {
                //TODO Write static variable
                writeMessageToUser("Kontroller hva du har skrevet inn");
                return false;
            }
        }else if(option == 2){
            if ((!inputEmail.getText().toString().isEmpty() && inputEmail.toString().length() > 0)){
                if((!inputPassword.getText().toString().isEmpty())
                        &&inputPassword.getText().toString().equals(secondInputPassword.getText().toString())){
                    return true;
                }
            }else{
                writeMessageToUser("Ugyldig input");
                return false;
            }


        }

        //Input is faulty!
        return false;
    }

    //Send message to user:
    private void writeMessageToUser(String messageToUser){
        Toast.makeText(this,messageToUser.toString(),Toast.LENGTH_SHORT).show();
    }

}
