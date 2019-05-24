package dev.rui9426.opensource_URLimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
public class MySmartImageView extends android.support.v7.widget.AppCompatImageView {
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap= (Bitmap) msg.obj;
            MySmartImageView.this.setImageBitmap(bitmap);
        }
    };
    public MySmartImageView(Context context) {
        super(context);
    }

    public MySmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySmartImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setImageFromURL(final String urlpath){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(urlpath);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(5000);
                    int responseCode = urlConnection.getResponseCode();
                    if(responseCode==200){
                        //请求成功
                        System.out.println("请求成功");
                        InputStream inputStream = urlConnection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Message obtain = Message.obtain();
                        obtain.obj=bitmap;
                        handler.sendMessage(obtain);
                        inputStream.close();
                    }else{
                        System.out.println("请求失败");
                        //HTTP状态码错误
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
