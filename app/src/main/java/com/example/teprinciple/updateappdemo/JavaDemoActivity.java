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

//    private String apkUrl = "http://118.24.148.250:8080/yk/update_signed.apk";
    private String apkUrl = "https://cos.pgyer.com/0ff1f8a89ab274a3fd46602efea74386.apk?sign=081cab71c2405a3bd5ac9082af6249a7&t=1687183383&response-content-disposition=attachment%3Bfilename%3DLibChecker_2.1.4.dc4f3a6d.apk";

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
