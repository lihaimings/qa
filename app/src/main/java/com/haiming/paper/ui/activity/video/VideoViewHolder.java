package com.haiming.paper.ui.activity.video;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.haiming.paper.R;
import com.youcheyihou.videolib.NiceVideoPlayer;
import com.youcheyihou.videolib.TxVideoPlayerController;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    public TxVideoPlayerController mController;
    public NiceVideoPlayer mVideoPlayer;
    private Context mContext;

    public VideoViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        mContext = context;
        mVideoPlayer = itemView.findViewById(R.id.nice_video_players);
        mVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK);
        mController = new TxVideoPlayerController(mContext);
        //将列表的每个视频设置未默认的16:9
        ViewGroup.LayoutParams params = mVideoPlayer.getLayoutParams();
        params.width = itemView.getResources().getDisplayMetrics().widthPixels;
        params.height = (int) (params.width * 9f / 16f);
        mVideoPlayer.setLayoutParams(params);
    }

    public void setController(TxVideoPlayerController controller) {
        mController = controller;
        mVideoPlayer.setController(mController);
    }

    public void bindData(VideoBean video) {
        Log.d("数据", "" + video.getImageUrl() + "  " + video.getTitle() + "  " + video.getVideoUrl());
        if (mController != null) {
            mController.setTitle(video.getTitle());
            Glide.with(mContext)
                    .load(video.getImageUrl())
                    .into(mController.imageView());
            mVideoPlayer.setController(mController);
            mVideoPlayer.setUp(video.getVideoUrl(), null);
            Log.d("数据", "mController不为空");
        } else {
            Log.d("数据", "mController为空");
        }

    }
}
