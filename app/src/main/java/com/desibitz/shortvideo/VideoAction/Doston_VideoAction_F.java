package com.desibitz.shortvideo.VideoAction;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daasuu.gpuv.egl.filter.GlWatermarkFilter;
import com.desibitz.shortvideo.BuildConfig;
import com.desibitz.shortvideo.R;
import com.desibitz.shortvideo.SimpleClasses.Doston_Functions;
import com.desibitz.shortvideo.SimpleClasses.Fragment_Callback;
import com.desibitz.shortvideo.SimpleClasses.Variables;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Doston_VideoAction_F extends BottomSheetDialogFragment implements View.OnClickListener {

    View view;
    Context context;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    Fragment_Callback fragment_callback;
    ComponentName componentName;
    String video_id;
    String video_url;
    Doston_VideoSharingApps_Adapter adapter;

    public Doston_VideoAction_F() {
//testBranch11
    }


    @SuppressLint("ValidFragment")
    public Doston_VideoAction_F(String url, String id, Fragment_Callback fragment_callback) {
        video_id = id;
        video_url = url;
        this.fragment_callback = fragment_callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Objects.requireNonNull(this.getDialog().getWindow()).setBackgroundDrawableResource(R.drawable.transperent_view);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video_action, container, false);
        context = getContext();

        progressBar = view.findViewById(R.id.progress_bar);
        view.findViewById(R.id.save_video_layout).setOnClickListener(this);
        view.findViewById(R.id.close_bottom_sheet).setOnClickListener(this);
        view.findViewById(R.id.copy_layout).setOnClickListener(this);
//        setStyle(STYLE_NORMAL, R.style. AppBottomSheetDialogTheme);
//        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Get_Shared_app();

            }
        }, 1000);


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            Toast.makeText(context, "permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    public void Get_Shared_app() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        final GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    PackageManager pm = getActivity().getPackageManager();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "https://google.com");

                    List<ResolveInfo> launchables = pm.queryIntentActivities(intent, 0);

                    for (int i = 0; i < launchables.size(); i++) {

                        if (launchables.get(i).activityInfo.name.contains("SendTextToClipboardActivity")) {
                            launchables.remove(i);
                            break;
                        }

                    }

                    Collections.sort(launchables,
                            new ResolveInfo.DisplayNameComparator(pm));

                    adapter = new Doston_VideoSharingApps_Adapter(context, launchables, new Doston_VideoSharingApps_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int positon, ResolveInfo item, View view) {

                            if (Doston_Functions.Checkstoragepermision(getActivity())) {
                                Open_App(item);
                            }
                        }
                    });

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                } catch (Exception e) {

                }
            }
        }).start();


    }



    public void Open_App(ResolveInfo resolveInfo) {
        try {

            ActivityInfo activity = resolveInfo.activityInfo;
            componentName = new ComponentName(activity.applicationInfo.packageName,
                    activity.name);
//            Log.d("ComponentName_testtetst",""+componentName);
//            Intent i = new Intent(Intent.ACTION_MAIN);
//
//            i.addCategory(Intent.CATEGORY_LAUNCHER);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            i.setComponent(name);

//            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_TEXT, Variables.base_url+"view.php?id="+video_id);
//            intent.setComponent(name);
//            startActivity(intent);

            File f = new File(Environment.getExternalStorageDirectory() + "/DesiBitz/.share/");
            f.mkdirs();
            Doston_Functions.Show_determinent_loader(context, false, false);
            PRDownloader.initialize(context);
            DownloadRequest prDownloader = PRDownloader.download(video_url, Environment.getExternalStorageDirectory() + "/DesiBitz/.share/", video_id + "_to_share_no_watermark.mp4")
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {

                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {

                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {

                            int prog = (int) ((progress.currentBytes * 100) / progress.totalBytes);
                            Doston_Functions.Show_loading_progress(prog / 2);

                        }
                    });


            prDownloader.start(new OnDownloadListener() {
                @Override
                public void onDownloadComplete() {
                    Applywatermark();
                }

                @Override
                public void onError(Error error) {
                    Delete_file_no_watermark();
//                    Log.d("ComponentName_testtetst",""+error.getServerErrorMessage());
//                    Log.d("ComponentName_testtetst",""+error.toString());
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    Doston_Functions.cancel_determinent_loader();
                }


            });


//            Toast.makeText(context, "Clicked_Tiktok_Share", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_video_layout:

                if (Doston_Functions.Checkstoragepermision(getActivity())) {

                    Bundle bundle = new Bundle();
                    bundle.putString("action", "save");
                    dismiss();
                    fragment_callback.Responce(bundle);

                }


                break;

            case R.id.close_bottom_sheet:

                this.getDialog().dismiss();

                break;

            case R.id.copy_layout:
//                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("Copied Text", Variables.base_url + "view.php?id=" + video_id);
//                clipboard.setPrimaryClip(clip);
//                Toast.makeText(context, "Link Copy in clipboard", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(resizedBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 10, 10, new Paint(Paint.FILTER_BITMAP_FLAG));

        return resizedBitmap;
    }

    public void Scan_file() {
        MediaScannerConnection.scanFile(getActivity(),
                new String[]{Environment.getExternalStorageDirectory() + "/DesiBitz/.share/" + video_id + "_to_share" + ".mp4"},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {

                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    public void Applywatermark() {

        Bitmap myLogo = ((BitmapDrawable) getResources().getDrawable(R.drawable.watermark)).getBitmap();
//         Bitmap bitmap_resize=Bitmap.createScaledBitmap(myLogo, 64, 26, true);
        Bitmap bitmap_resize = getResizedBitmap(myLogo, 70, 25);
        GlWatermarkFilter filter = new GlWatermarkFilter(bitmap_resize, GlWatermarkFilter.Position.LEFT_TOP);
        new GPUMp4Composer(Environment.getExternalStorageDirectory() + "/DesiBitz/.share/" + video_id + "_to_share_no_watermark.mp4",
                Environment.getExternalStorageDirectory() + "/DesiBitz/.share/" + video_id + "_to_share" + ".mp4")
                .filter(filter)

                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {

                        //Log.d("resp",""+(int) (progress*100));
                        Doston_Functions.Show_loading_progress((int) ((progress * 100) / 2) + 50);

                    }

                    @Override
                    public void onCompleted() {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Doston_Functions.cancel_determinent_loader();
                                Delete_file_no_watermark();
                                Scan_file();
                                Intent i = new Intent(Intent.ACTION_MAIN);

                                i.addCategory(Intent.CATEGORY_LAUNCHER);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                i.setComponent(componentName);
                                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                intent.setType("video/*");
                                intent.putExtra(Intent.EXTRA_TEXT, "To watch more videos like this download DesiBitz. https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
//                                Uri uri = Uri.parse(Environment.getExternalStorageDirectory() +"/DesiBitz/.share/"+video_id+"_to_share.mp4");
                                Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), getActivity().getPackageName()+".fileprovider", new File(Environment.getExternalStorageDirectory() +"/DesiBitz/.share/"+video_id+"_to_share.mp4"));
                                intent.putExtra(Intent.EXTRA_STREAM, uri);
                                intent.setComponent(componentName);
                                startActivity(intent);
//                                Environment.getExternalStorageDirectory() +"/DesiBitz/.share/"+video_id+"_to_share_no_watermark.mp4"

                            }
                        });


                    }

                    @Override
                    public void onCanceled() {
                        //Log.d("resp", "onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {

                        //Log.d("resp",exception.toString());

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Delete_file_no_watermark();
                                    Doston_Functions.cancel_determinent_loader();
                                    Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {

                                }
                            }
                        });

                    }
                })
                .start();
    }


    public void Delete_file_no_watermark() {
        File file = new File(Environment.getExternalStorageDirectory() + "/DesiBitz/.share/" + video_id + "_to_share_no_watermark.mp4");
        if (file.exists()) {
            file.delete();
        }
    }


}
