package com.modules.jules.microcredit;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.modules.jules.microcredit.utils.Func;
import com.modules.jules.microcredit.utils.PrefManager;
import com.modules.jules.microcredit.utils.Var;

public class ContratScreen extends AppCompatActivity {


    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrat_screen);

        CheckBox checkBox        = (CheckBox) findViewById(R.id.checkcontrat);
        TextView contrat_content = (TextView) findViewById(R.id.contrat_content);
        final Button   next            = (Button) findViewById(R.id.next_contrat);
        Func.changeTypeFace(this,contrat_content, Var.FONT_ROBOTO_LIGHT);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isChecked){

                    isChecked = false;
                    next.setVisibility(View.INVISIBLE);
                }else{
                    isChecked = true ;
                    next.setVisibility(View.VISIBLE);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isChecked){
                    PrefManager.getInstance(ContratScreen.this)
                            .putBoolean(PrefManager.IS_FIRST_TIME_LAUNCH,false);
                    Intent intent = new Intent(ContratScreen.this,Attente.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }
}
