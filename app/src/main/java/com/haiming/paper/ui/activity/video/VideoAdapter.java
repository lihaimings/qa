package com.haiming.paper.ui.activity.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haiming.paper.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    private Context mContext;
    private List<VideoBean> mVideoBeanList;

    public VideoAdapter(Context context, List<VideoBean> videoBeanList) {
        this.mContext = context;
        this.mVideoBeanList = videoBeanList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        VideoViewHolder holder = new VideoViewHolder(view,mContext);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoBean videoBean = mVideoBeanList.get(position);
        holder.bindData(videoBean);
    }

    @Override
    public int getItemCount() {
        return mVideoBeanList.size();
    }
}
