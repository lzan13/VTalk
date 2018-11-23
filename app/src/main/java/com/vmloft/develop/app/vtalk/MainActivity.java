package com.vmloft.develop.app.vtalk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vmloft.develop.app.vtalk.bean.TalkBean;
import com.vmloft.develop.app.vtalk.common.Callback;
import com.vmloft.develop.app.vtalk.network.TalkHelper;
import com.vmloft.develop.library.tools.utils.VMLog;
import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.utils.bitmap.VMBitmap;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MainActivity mActivity;
    private View talkLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView talkContentView;
    private TextView talkAuthorView;

    private TalkBean talkBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;

        initView();
        initData();
    }

    /**
     * 初始化 UI
     */
    private void initView() {
        talkLayout = findViewById(R.id.layout_talk);
        mRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        talkContentView = findViewById(R.id.text_talk_content);
        talkAuthorView = findViewById(R.id.text_talk_author);

        findViewById(R.id.btn_share).setOnClickListener(viewListener);
        findViewById(R.id.btn_qr_code).setOnClickListener(viewListener);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRandomTalk();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mRefreshLayout.setRefreshing(true);
        getRandomTalk();
    }

    /**
     * 控件点击事件
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_share:
                    shareSingleImage();
                    break;
                case R.id.btn_qr_code:
                    startQRCode();
                    break;
            }
        }
    };

    /**
     * 获取随机的一句话
     */
    private void getRandomTalk() {
        TalkHelper.getInstance().getRandomTalk(new Callback() {
            @Override
            public void onDone(Object object) {
                mRefreshLayout.setRefreshing(false);
                talkBean = (TalkBean) object;
                VMLog.i("获取的一言数据 %s", talkBean.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showTalk(talkBean);
                    }
                });
            }

            @Override
            public void onError(int code, String desc) {
                mRefreshLayout.setRefreshing(false);
                VMLog.e("请求错误 %d, %s", code, desc);
            }
        });
    }

    /**
     * 显示一句话
     */
    private void showTalk(TalkBean bean) {
        talkContentView.setText(bean.getHitokoto());
        String author;
        if (VMStr.isEmpty(bean.getFrom())) {
            author = bean.getCreator();
        } else {
            author = bean.getFrom();
        }
        talkAuthorView.setText(String.format(VMStr.strByResId(R.string.author), author));
    }


    /**
     * 分享文字
     */
    public void shareText(TalkBean bean) {
        if (talkBean == null) {
            return;
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String shareStr = String.format("%s \n -- %s --", bean.getHitokoto(), bean.getFrom());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareStr);
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 分享单张图片
     */
    public void shareSingleImage() {
        String imagePath = Environment.getExternalStorageDirectory() + File.separator + "test.jpg";

        talkLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = talkLayout.getDrawingCache();
        try {
            VMBitmap.saveBitmapToSDCard(bitmap, imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        talkLayout.setDrawingCacheEnabled(false);

        Intent shareIntent = new Intent();

        File file = new File(imagePath);
        //由文件得到uri
        Uri imageUri;
        //判断是否是7.0以上的设备
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName() + ".provider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            // 还用以前的就好了
            imageUri = Uri.fromFile(file);
        }

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));

        VMLog.i("uri: %s", imageUri);  //输出：file:///storage/emulated/0/test.jpg
    }

    //    //分享多张图片
    //    public void shareMultipleImage(View view) {
    //        ArrayList<Uri> uriList = new ArrayList<>();
    //
    //        String path = Environment.getExternalStorageDirectory() + File.separator;
    //        uriList.add(Uri.fromFile(new File(path+"australia_1.jpg")));
    //        uriList.add(Uri.fromFile(new File(path+"australia_2.jpg")));
    //        uriList.add(Uri.fromFile(new File(path+"australia_3.jpg")));
    //
    //        Intent shareIntent = new Intent();
    //        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
    //        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
    //        shareIntent.setType("image/*");
    //        startActivity(Intent.createChooser(shareIntent, "分享到"));
    //    }

    private void startQRCode() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "取消扫描", Toast.LENGTH_LONG).show();
                VMLog.i("取消二维码扫描");
            } else {
                Toast.makeText(this, "识别二维码: " + result.getContents(), Toast.LENGTH_LONG).show();
                VMLog.i("识别二维码记过：%s", result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
