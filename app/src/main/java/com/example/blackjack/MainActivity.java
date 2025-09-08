package com.example.blackjack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button btn1,btn2;
    private EditText et1,et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        et1=(EditText)findViewById(R.id.edit_text1);
        et2=(EditText)findViewById(R.id.edit_text2);

        btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //你要执行的代码
                String Administrator, Passwd;
                Administrator = "scg";
                Passwd = "123456";
                if (et1.getText().toString().equals(Administrator) && et2.getText().toString().equals(Passwd)){
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "用户名或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
            }

        });

        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //你要执行的代码
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确定退出吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                System.exit(0);
                            }
                        })
                        .show();

            }
        });






    }
}