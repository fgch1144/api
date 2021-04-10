package com.example.api;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.frakbot.jumpingbeans.JumpingBeans;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView textView1, textView2, textView3, textView4, textView5, textView6,textView7;
    private EditText editText;
    private String s1;
    private String s2;
    private String s3;
    JumpingBeans jumpingBeans;
    MyViewmodle myViewmodle;
    Handler handler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        textView1 = findViewById(R.id.textView);
        editText = findViewById(R.id.editTextNumber);
        textView2 = findViewById(R.id.textView4);
        textView3 = findViewById(R.id.textView2);
        textView4 = findViewById(R.id.textView6);
        textView5 = findViewById(R.id.textView7);
        textView6 = findViewById(R.id.textView3);
        textView7 =findViewById(R.id.textView5);
        myViewmodle = ViewModelProviders.of(this).get(MyViewmodle.class);
        textView1.setText(myViewmodle.s1);
        textView2.setText(myViewmodle.s5);
        textView3.setText(myViewmodle.s2);
        textView4.setText(myViewmodle.s6);
        textView5.setText(myViewmodle.s5);
        textView6.setText(myViewmodle.s6);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("本产品是尝试版，感谢你的使用！！！")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run();
                handler = new Handler() {
                    @SuppressLint("HandlerLeak")
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        switch (msg.what) {
                            case 1:
                                textView1.setText(myViewmodle.s1 = msg.obj.toString());
                                jumpingBeans = JumpingBeans.with(textView1)
                                        .makeTextJump(0, textView1.getText().length())
                                        .setIsWave(true)
                                        .setLoopDuration(1000)
                                        .build();
                                int[] color = {Color.RED, Color.GREEN, Color.BLUE};
                                float[] floats = {0f, 0.7f, 1.0f};
                                LinearGradient linearGradient = new LinearGradient(0, 0, textView1.getPaint().getTextSize() * textView1.getText().length(), 0, color, floats, Shader.TileMode.CLAMP);
                                textView1.getPaint().setShader(linearGradient);
                                textView1.invalidate();
                            case 2:
                                textView3.setText(myViewmodle.s2 = msg.obj.toString());
                            case 3:
                                textView5.setText(myViewmodle.s3 = msg.obj.toString());
                            case 4:
                                textView7.setText(msg.obj.toString());
                                textView7.setSelected(true);
                        }
                    }
                };
                textView6.setText(myViewmodle.s4 = "开奖号码：");
                textView2.setText(myViewmodle.s5 = "开奖时间: ");
                textView4.setText(myViewmodle.s6 = "兑换时间到：");
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumpingBeans != null) {
                    jumpingBeans.stopJumping();
                }
            }
        });
    }
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ui = "http://apis.juhe.cn/lottery/query?key=2eefead6bf66679717da8e8b2378eb8e&lottery_id=ssq&lottery_no=" + editText.getText().toString();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().get().url(ui).build();
                Call call = client.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                            String s4 =jsonObject1.getString("lottery_prize");
                            s1 = jsonObject1.getString("lottery_res");
                            s2 = jsonObject1.getString("lottery_date");
                            s3 = jsonObject1.getString("lottery_exdate");
                            Gson gson = new Gson();
                            List<User> userList = gson.fromJson(s4,new TypeToken<List<User>>(){}.getType());
                            StringBuilder strings = new StringBuilder();
                            for (int i= 0; i<userList.size();i++){
                                strings.append(userList.get(i));
                            }
                            Message mes1 = new Message();
                            mes1.obj = s1;
                            mes1.what = 1;
                            handler.sendMessage(mes1);
                            Message mes2 = new Message();
                            mes2.obj = s2;
                            mes2.what = 2;
                            handler.sendMessage(mes2);
                            Message mes3 = new Message();
                            mes3.obj = s3;
                            mes3.what = 3;
                            handler.sendMessage(mes3);
                            Message mes4 = new Message();
                            mes4.obj = strings.toString();
                            mes4.what = 4;
                            handler.sendMessage(mes4);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
    @Override
    protected void onPause() {
        if (jumpingBeans != null) {
            jumpingBeans.stopJumping();
        }
        super.onPause();
    }
}