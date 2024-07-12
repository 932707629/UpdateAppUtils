package com.example.teprinciple.updateappdemo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import constant.UiType;
import listener.Md5CheckResultListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

/**
 * desc: java使用实例
 * time: 2019/6/27
 * @author yk
 */
public class JavaDemoActivity extends AppCompatActivity {
    private String apkUrl = "https://c10490a5350e1a789239074051f764a2.dlied1.cdntips.net/download.mail.qq.com/apk/qqmail_android_6.5.4.10161662.731_55.apk";
//    private String apkUrl = "https://res.126.net/dl/client/androidmail/dashi/100/mail.apk";
//    private String apkUrl = "https://appdownload.mail.10086.cn/event/static/sys/20200723/PE-V10.2.6-9048001.apk";
    private String updateTitle = "发现新版本V2.0.0";
    private String updateContent = "1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_demo);

        UpdateAppUtils.init(this);

        findViewById(R.id.btn_java).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateConfig updateConfig = new UpdateConfig();
                updateConfig.setCheckWifi(true);
                updateConfig.setNeedCheckMd5(true);
                updateConfig.setNotifyImgRes(R.drawable.ic_logo);

                UiConfig uiConfig = new UiConfig();
                uiConfig.setUiType(UiType.PLENTIFUL);
                HashMap<String,String> header = new HashMap<String,String>();
                header.put("token","你的token");

                UpdateAppUtils
                        .getInstance()
                        .apkUrl(apkUrl)
                        .addHeader(header)
                        .updateTitle(updateTitle)
                        .updateContent(updateContent)
                        .uiConfig(uiConfig)
                        .updateConfig(updateConfig)
                        .setMd5CheckResultListener(new Md5CheckResultListener() {
                            @Override
                            public void onResult(boolean result) {

                            }
                        })
                        .setUpdateDownloadListener(new UpdateDownloadListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onDownload(int progress) {

                            }

                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onError(@NotNull Throwable e) {

                            }
                        })
                        .update();
            }
        });
    }
}
