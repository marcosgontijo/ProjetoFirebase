package com.example.androidfirebaseproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androidfirebaseproject.MainActivity;
import com.example.androidfirebaseproject.Model.UserModel;
import com.example.androidfirebaseproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Register extends AppCompatActivity {


    private EditText edt_email_register;
    private EditText edt_nome_register;
    private EditText edt_sobrenome_register;
    private EditText edt_senha_register;
    private EditText edt_confirmar_senha_register;
    private CheckBox ckb_mostrar_senha_register;
    private Button btn_registrar_register;
    private Button btn_login_register;
    private ProgressBar loginProgressBar_register;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        edt_email_register = findViewById(R.id.edt_email_register);
        edt_nome_register = findViewById(R.id.edt_nome_register);
        edt_sobrenome_register = findViewById(R.id.edt_sobrenome_register);
        edt_senha_register = findViewById(R.id.edt_senha_register);
        edt_confirmar_senha_register = findViewById(R.id.edt_confirmar_senha_register);
        ckb_mostrar_senha_register = findViewById(R.id.ckb_mostrar_senha_register);
        btn_registrar_register = findViewById(R.id.btn_registrar_register);
        btn_login_register = findViewById(R.id.btn_login_register);
        loginProgressBar_register = findViewById(R.id.loginProgressBar_register);

        ckb_mostrar_senha_register.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_senha_register.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edt_confirmar_senha_register.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    edt_senha_register.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_confirmar_senha_register.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btn_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btn_registrar_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userModel = new UserModel();

                userModel.setEmail(edt_email_register.getText().toString());
                userModel.setNome(edt_nome_register.getText().toString());
                userModel.setSobrenome(edt_sobrenome_register.getText().toString());

                //String registerEmail = edt_email_register.getText().toString();
                String senha = edt_senha_register.getText().toString();
                String confirmarSenha = edt_confirmar_senha_register.getText().toString();

                if (!TextUtils.isEmpty(userModel.getNome()) && !TextUtils.isEmpty(userModel.getSobrenome()) && !TextUtils.isEmpty(userModel.getEmail()) && TextUtils.isEmpty(senha) && TextUtils.isEmpty(confirmarSenha)) {
                    if (senha.equals(confirmarSenha)) {
                        loginProgressBar_register.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(userModel.getEmail(), senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    userModel.setId(mAuth.getUid());
                                    userModel.salvar();
                                    abrirTelaPrincipal();
                                } else {
                                    String error;
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        error = "Atenção: A senha deve conter no mínimo 6 caracteres!!";
                                    } catch (FirebaseAuthEmailException e) {
                                        error = "Email invalido.";
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        error = "Atenção: Usuario já existe!!";
                                    } catch (Exception e) {
                                        error = "Efetuar o cadastro!! Erro genérico";
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(Register.this, error, Toast.LENGTH_LONG).show();
                                }
                                loginProgressBar_register.setVisibility(View.INVISIBLE);

                            }
                        });
                    } else {
                        Toast.makeText(Register.this, "A senha deve ser a mesma em ambos os campos", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Register.this, "Todos os campos devem estar preenchidos corretamente!!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}