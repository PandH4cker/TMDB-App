package com.tmdbapp.utils.video;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoViewUtils {

    private static final String TAG = VideoViewUtils.class.getSimpleName();

    public static void playRawVideo(Context context, VideoView videoView, String resName) {
        try {
            int id = VideoViewUtils.getRawResIdByName(context, resName);
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + id);
            Log.i(TAG, "Video URI: " + uri);

            videoView.setVideoURI(uri);
            videoView.requestFocus();
        } catch (Exception e) {
            Log.e(TAG, "Error Play Raw Video: " + e.getMessage());
            Toast.makeText(context, "Error Play Raw Video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void playLocalVideo(Context context, VideoView videoView, String localPath) {
        try {
            videoView.setVideoPath(localPath);
            videoView.requestFocus();
        } catch (Exception e) {
            Log.e(TAG, "Error Play Local Video: " + e.getMessage());
            Toast.makeText(context, "Error Play Local Video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void playURLVideo(Context context, VideoView videoView, String videoURL) {
        try {
            Log.i(TAG, "Video URL: " + videoURL);
            Uri uri = Uri.parse(videoURL);

            videoView.setVideoURI(uri);
            videoView.requestFocus();
        } catch (Exception e) {
            Log.e(TAG, "Error Play URL Video: " + e.getMessage());
            Toast.makeText(context, "Error Play URL Video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static int getRawResIdByName(Context context, String resName) {
        String pkgName = context.getPackageName();
        int resId = context.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i(TAG, "Res Name: " + resName + " ==> Res ID = " + resId);
        return resId;
    }

}
