package mobile.zaoren.com.update.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import mobile.zaoren.com.update.R;
import mobile.zaoren.com.update.config.DownloadKey;
import mobile.zaoren.com.update.module.Download;


/**
 * Created by Administrator on 2016/8/10.
 */
public class DownloadingActivity extends Activity {
    private ImageView close;
    public ProgressBar mProgress;
    public TextView count;

    private Context mContext = DownloadKey.fromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_download_dialog);

        close = (ImageView) findViewById(R.id.downloaddialog_close);
        mProgress = (ProgressBar) findViewById(R.id.downloaddialog_progress);
        count = (TextView) findViewById(R.id.downloaddialog_count);

        if (DownloadKey.interceptFlag) DownloadKey.interceptFlag = false;
        new Download(this).start();

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(DownloadingActivity.this,
                        mContext.getClass());
                setResult(3, intent);
                DownloadKey.ToShowDownloadView = DownloadKey.closeDownloadView;
                DownloadKey.interceptFlag = true;
                finish();
            }
        });

    }

}
