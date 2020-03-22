package com.haiming.paper.adapter;

import android.content.Context;

import com.haiming.paper.Utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

abstract class RecyclerBaseAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<M> mDataList = new ArrayList<>();
//    private GlideRequests mRequestManager;//Glide加载图片
    private OnItemClickedListener<M> mOnItemClickedListener;
    private Context mContext;

//    @NonNull
//    public GlideRequests getRequestManager() {
//        if (mRequestManager == null) {
//            throw new IllegalArgumentException("RequestManager can not be null in RecyclerViewAdapter");
//        }
//        return mRequestManager;
//    }
//
//    public void setRequestManager(GlideRequests requestManager) {
//        mRequestManager = requestManager;
//    }


    public boolean isDataListEmpty() {
        return mDataList.size() <= 0;
    }

    public RecyclerBaseAdapter(Context context,List<M> dataList){
        this.mContext = context;
        this.mDataList = dataList;
    }

    /**
     * @param list 更新List
     */
    public void updateList(List<M> list) {
        mDataList.clear();
        if (!UIUtil.isListBlank(list)) {
            mDataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * @param list 在集合尾部添加更多数据集合
     */
    public void addMoreList(List<M> list) {
        if (!UIUtil.isListBlank(list)) {
            mDataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addList(M data) {
        if (data != null) {
            mDataList.add(data);
            notifyDataSetChanged();
        }
    }

    /**
     * @param model 删除指定数据条目
     */
    public void removeItem(M model) {
        removeItem(mDataList.indexOf(model));
    }

    /**
     * @param position 删除指定索引数据条目
     */
    public void removeItem(int position) {
        mDataList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * @param data 删除指定数据条目
     */
    public void removeList(List<M> data) {
        if (data != null) {
            mDataList.removeAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 清空列表
     */
    public void clearList() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * @return 获取数据集合
     */
    public List<M> getDataList() {
        return mDataList;
    }

    /**
     * @return 获取指定索引位置的数据模型
     */
    public M getItem(int position) {
        if (mDataList == null || position < 0 || position >= mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setOnItemClickedListener(OnItemClickedListener<M> onItemClickedListener) {
        mOnItemClickedListener = onItemClickedListener;
    }

    public OnItemClickedListener<M> getOnItemClickedListener() {
        return mOnItemClickedListener;
    }

    public interface OnItemClickedListener<M> {

        void onItemClicked(M itemData);
    }

    public Context getContext() {
        return mContext;
    }
}

