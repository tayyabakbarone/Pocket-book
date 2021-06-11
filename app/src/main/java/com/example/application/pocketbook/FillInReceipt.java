
package com.example.application.pocketbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Field;

public class FillInReceipt extends AppCompatActivity {

    EditText FieldT;
    EditText ItemT;
    EditText priceI;
    Button Add;
    TextView Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_in_receipt);

        FieldT = findViewById(R.id.Field);
        ItemT = findViewById(R.id.Item);
        priceI = findViewById(R.id.Price);
        Add = findViewById(R.id.Add);
        Text = findViewById(R.id.detText);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Field = FieldT.getText().toString();
                String Item = ItemT.getText().toString();
                String price = priceI.getText().toString();

                if (Field.isEmpty()) {
                    FieldT.setError("Pleaser enter Email");
                    FieldT.requestFocus();
                } else if (Item.isEmpty()) {
                    ItemT.setError("Pleaser enter Password");
                    ItemT.requestFocus();
                } else if (price.isEmpty()) {
                    priceI.setError("Pleaser enter Password");
                    priceI.requestFocus();
                } else {
                    Text.setText("Field:" + Field + "\nItem:" + Item + "\nPrice:" + price);
                }


            }
        });
    }
}