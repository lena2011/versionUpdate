package com.zaoren.mobile.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.zaoren.mobile.R;
import com.zaoren.mobile.entity.MapiUpdateVersionResult;

import mobile.zaoren.com.update.UpdateFunGo;
import mobile.zaoren.com.update.config.DebugLog;
import mobile.zaoren.com.update.config.DownloadKey;
import mobile.zaoren.com.update.utils.GetAppInfo;

public class MainActivity extends AppCompatActivity {
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData(true);
            }
        });
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.check_update);

        loadData(false);
    }

    /**
     * @param isManual 是否手动获取更新
     */
    private void loadData(boolean isManual) {
        MapiUpdateVersionResult result = new MapiUpdateVersionResult();
        result.setVersion(2);
        result.setContent("1.本次更新");
        result.setUrl("http://img.bstapp.cn/zaoren/testapp/update.apk");
        checkVersion(result, isManual);
    }

    /**
     * 检查版本，若不是最新版本则显示弹框
     *
     * @param result
     */
    private void checkVersion(MapiUpdateVersionResult result, boolean isManual) {
        if (GetAppInfo.getAppVersionCode(this) < (result.getVersion())) {
            DownloadKey.version = result.getVersion();
            DownloadKey.changeLog = result.getContent();
            DownloadKey.apkUrl = result.getUrl();
            DownloadKey.ToShowDownloadView = DownloadKey.showUpdateView;
            if (!isManual)
                UpdateFunGo.init(this);
            else UpdateFunGo.init(this).showUpdateDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGo.onResume(this);//默认弹框下载
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGo.onStop();
    }
}
