package mobile.zaoren.com.update;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import mobile.zaoren.com.update.activity.DownloadingActivity;
import mobile.zaoren.com.update.activity.UpdateDialogActivity;
import mobile.zaoren.com.update.config.DebugLog;
import mobile.zaoren.com.update.config.DownloadKey;
import mobile.zaoren.com.update.config.UpdateKey;
import mobile.zaoren.com.update.module.Download;
import mobile.zaoren.com.update.utils.GetAppInfo;


/**
 * Created by Administrator on 2016/8/10.
 */
public class UpdateFunGo {
    private Context mContext;
    private static NotificationManager notificationManager;
    private static Notification.Builder builder;
    private static volatile UpdateFunGo sInst = null;
    private static Download mDownload;

    public static UpdateFunGo init(Context context) {
        UpdateFunGo inst = sInst;
        if (inst == null) {
            synchronized (UpdateFunGo.class) {
                inst = sInst;
                if (inst == null) {
                    inst = new UpdateFunGo(context);
                    sInst = inst;
                }
            }
        }
        return inst;
    }

    public UpdateFunGo(Context context) {
        mContext = context;
        DownloadKey.fromActivity = context;
        showUpdateDialog();
    }

    /**
     * 在检查版本的时候，弹出版本更新框
     */
    public void showUpdateDialog() {
        if (DownloadKey.ToShowDownloadView == DownloadKey.showUpdateView && DownloadKey.version > GetAppInfo.getAppVersionCode(mContext)) {
            showNoticeDialog(mContext);//显示更新弹框
        }
    }


    /**
     * 显示弹框
     *
     * @param context
     */
    private static void showNoticeDialog(Context context) {
        Intent intent = new Intent(context, UpdateDialogActivity.class);
        ((Activity) context).startActivityForResult(intent, 100);
    }

    /**
     * 关闭弹框页面返回，显示下载弹框
     *
     * @param context
     */
    public static void onResume(Context context) {
        if (DownloadKey.ToShowDownloadView == DownloadKey.showDownloadView)
            showDownloadView(context);
    }

    /**
     * 通知栏或者弹框下载
     *
     * @param context
     */
    private static void showDownloadView(Context context) {
        switch (UpdateKey.DialogOrNotification) {
            case UpdateKey.WITH_DIALOG:
                Intent intent = new Intent(context, DownloadingActivity.class);
                ((Activity) context).startActivityForResult(intent, 0);
                break;
            case UpdateKey.WITH_NOTIFICATION:
                notificationInit(context);
                mDownload = new Download(builder, context);
                mDownload.start();
                break;
        }
    }

    private static void notificationInit(Context context) {
        Intent intent = new Intent(context, context.getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setTicker("开始下载")
                .setContentTitle(GetAppInfo.getAppName(context))
                .setContentText("正在更新")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis());
    }

    public static void onStop() {
        if (DownloadKey.ToShowDownloadView == DownloadKey.showDownloadView && UpdateKey.DialogOrNotification == UpdateKey.WITH_NOTIFICATION) {
            mDownload.interrupt();//关闭下载
        }
    }
}
