package com.haiming.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haiming.paper.R;
import com.haiming.paper.bean.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecycerViewAdapter extends RecyclerBaseAdapter<Note, HomeRecycerViewAdapter.ViewHolder> {

    private boolean noAnswer;

    private final int HAVE_ANSWER = 0x01;
    private final int NO_ANSWER = 0x02;

    public HomeRecycerViewAdapter(Context mContext, List<Note> dataList) {
        super(mContext, dataList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == HAVE_ANSWER) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.answer_question_description_list_item, parent, false);
            return new ViewHolder(view);
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.question_no_answer_title, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (noAnswer) {
            return NO_ANSWER;
        }
        return HAVE_ANSWER;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
