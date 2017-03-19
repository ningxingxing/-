package com.example.startallplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button startAllPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        startAllPlayer = (Button)findViewById(R.id.startAllPlayer);
        startAllPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // intent.setDataAndType(Uri.parse("http://cctv13.vtime.cntv.wscdns.com/live/no/23_/seg0/index.m3u8?uid=default&AUTH=6+sb7H/DDgZ9MYff0mJ1rpMUyksw8zC6nQhOykNIpXaTZdEDg6huYnsWRW7KsatosnIXEVhU2Yr6gZJ5V8xEUw=="),"video/*");
                // intent.setDataAndType(Uri.parse("http://tv.cctv.com/live"),"video/*");
               // intent.setDataAndType(Uri.parse("http://vfx.mtime.cn/Video/2016/07/19/mp4/160719095812990469"),"video/*");
                Intent intent = new Intent();
                intent.setDataAndType(Uri.parse("http://data.vod.itc.cn/?rb=1&prot=1&key=jbZhEJhlqlUN-Wj_HEI8BjaVqKNFvDrn&prod=flash&pt=1&new=/86/168/RUXzy059JIvGRrrXQvOkNE.mp4"), "video/*");
                startActivity(intent);
            }
        });
    }



}
