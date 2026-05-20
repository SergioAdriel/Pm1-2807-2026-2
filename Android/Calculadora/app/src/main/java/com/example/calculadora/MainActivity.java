package com.example.calculadora;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
//Jesus Eduardo Diaz Pompa
    EditText pantalla;
    double op1 = 0, op2 = 0, res = 0;
    String operacion = "";
    boolean punto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantalla = findViewById(R.id.pantalla);

        if (pantalla == null) {
            Toast.makeText(this, "Error en layout", Toast.LENGTH_LONG).show();
            return;
        }

        int[] numeros = {
                R.id.btn0,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,
                R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9
        };

        for(int id : numeros){
            Button btn = findViewById(id);
            if(btn != null){
                btn.setOnClickListener(v -> pantalla.append(btn.getText()));
            }
        }

        Button btnPunto = findViewById(R.id.btnPunto);
        if(btnPunto != null){
            btnPunto.setOnClickListener(v -> {
                if(punto){
                    pantalla.append(".");
                    punto = false;
                }
            });
        }

        setOp(R.id.btnSuma, "+");
        setOp(R.id.btnResta, "-");
        setOp(R.id.btnMulti, "*");
        setOp(R.id.btnDiv, "/");

        Button btnIgual = findViewById(R.id.btnIgual);
        if(btnIgual != null){
            btnIgual.setOnClickListener(v -> calcular());
        }

        Button btnClear = findViewById(R.id.btnClear);
        if(btnClear != null){
            btnClear.setOnClickListener(v -> limpiar());
        }
    }

    private void setOp(int id, String op){
        Button btn = findViewById(id);
        if(btn != null){
            btn.setOnClickListener(v -> guardar(op));
        }
    }

    private void calcular(){
        String texto = pantalla.getText().toString();

        if(texto.isEmpty() || operacion.equals("")){
            return;
        }

        try{
            op2 = Double.parseDouble(texto);

            switch(operacion){
                case "+": res = op1 + op2; break;
                case "-": res = op1 - op2; break;
                case "*": res = op1 * op2; break;
                case "/":
                    if(op2 == 0){
                        pantalla.setText("Error");
                        return;
                    }
                    res = op1 / op2;
                    break;
            }

            pantalla.setText(String.valueOf(res));
            operacion = "";
            punto = true;

        }catch(Exception e){
            pantalla.setText("Error");
        }
    }

    private void limpiar(){
        pantalla.setText("");
        op1 = op2 = res = 0;
        operacion = "";
        punto = true;
    }

    private void guardar(String op){
        String texto = pantalla.getText().toString();

        if(texto.isEmpty()){
            return;
        }

        try{
            op1 = Double.parseDouble(texto);
            operacion = op;
            pantalla.setText("");
            punto = true;
        }catch(Exception e){
            pantalla.setText("Error");
        }
    }
}