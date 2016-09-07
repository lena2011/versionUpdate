package com.zaoren.mobile.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.zaoren.mobile.R;
import com.zaoren.mobile.entity.MapiUpdateVersionResult;

import mobile.zaoren.com.update.UpdateFunGo;
import mobile.zaoren.com.update.config.DownloadKey;
import mobile.zaoren.com.update.utils.GetAppInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        MapiUpdateVersionResult result = new MapiUpdateVersionResult();
        result.setVersion(2);
        result.setContent("1.本次更新");
        result.setUrl("http://img.bstapp.cn/ws/apk/app-release.apk");
        checkVersion(result);
    }

    /**
     * 检查版本，若不是最新版本则显示弹框
     *
     * @param result
     */
    private void checkVersion(MapiUpdateVersionResult result) {
        if (!GetAppInfo.getAppVersionCode(this).equals(result.getVersion())) {
            DownloadKey.version = result.getVersion();
            DownloadKey.changeLog = result.getContent();
            DownloadKey.apkUrl = result.getUrl();
            DownloadKey.ToShowDownloadView = DownloadKey.showUpdateView;
            UpdateFunGo.init(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGo.onResume(this);//现在只能弹框下载
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGo.onStop();
    }
}
