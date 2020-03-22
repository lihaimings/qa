package com.haiming.paper.adapter;
import android.view.View;

import com.haiming.paper.Utils.UIUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuefu_Du on 2017/7/9.
 */
public abstract class RecyclerViewAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<M> mDataList = new ArrayList<>();
    private HeaderAndFooterAdapter mHeaderAndFooterAdapter;
    private String mSourcePage;//埋点-源页面
//    private GlideRequests mRequestManager;//Glide加载图片

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

    public String getSourcePageNotNull() {
        if (mSourcePage == null) {
            return "";
        }
        return mSourcePage;
    }

    public String getSourcePage() {
        return mSourcePage;
    }

    public void setSourcePage(String sourcePage) {
        this.mSourcePage = sourcePage;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * @return 获取指定索引位置的数据模型
     */
    public M getItem(int position) {
        if (mDataList != null && position >= 0 && mDataList.size() > position) {
            return mDataList.get(position);
        } else {
            return null;
        }
    }

    /**
     * @return 获取数据集合
     */
    public List<M> getDataList() {
        return mDataList;
    }

    /**
     * @return 数据列表是否为空
     */
    public boolean isDataListEmpty() {
        return (mDataList == null || mDataList.size() <= 0);
    }

    /**
     * @param data 在指定位置添加新的数据
     */
    public void addData(M data, int position) {
        if (data != null && mDataList.size() >= position) {
            mDataList.add(position, data);
            notifyDataSetChangedWrapper();
        }
    }

    /**
     * @param data 在集合尾部添加新的数据
     */
    public void addMoreData(M data) {
        if (data != null) {
            mDataList.add(data);
            notifyDataSetChangedWrapper();
        }
    }

    /**
     * @param list 在集合尾部添加更多数据集合
     */
    public void addMoreData(List<M> list) {
        if (!UIUtil.isListBlank(list)) {
            int positionStart = mDataList.size();
            mDataList.addAll(mDataList.size(), list);
            notifyItemRangeInsertedWrapper(positionStart, list.size());
        }
    }

    /**
     * @param list 在集合头部部添加更多数据集合
     */
    public void addAheadData(List<M> list) {
        if (!UIUtil.isListBlank(list)) {
            mDataList.addAll(0, list);
            notifyItemRangeInsertedWrapper(0, list.size());
        }
    }

    /**
     * @param data 设置全新的数据集合，如果传入空（null或空表），则清空数据列表
     *             （第一次从服务器加载数据，或者下拉刷新当前界面数据表）
     */
    public void setData(List<M> data) {
        mDataList.clear();
        if (!UIUtil.isListBlank(data)) {
            mDataList.addAll(data);
        }
        notifyDataSetChangedWrapper();
    }

    /**
     * 清空数据列表
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChangedWrapper();
    }

    private void notifyItemRangeInsertedWrapper(int positionStart, int itemCount) {
        if (mHeaderAndFooterAdapter == null) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else {
            mHeaderAndFooterAdapter.notifyItemRangeInserted(mHeaderAndFooterAdapter.getHeadersCount() + positionStart, itemCount);
        }
    }

    private void notifyItemRangeChangedWrapper(int positionStart, int itemCount) {
        if (mHeaderAndFooterAdapter == null) {
            notifyItemRangeChanged(positionStart, itemCount);
        } else {
            mHeaderAndFooterAdapter.notifyItemRangeChanged(mHeaderAndFooterAdapter.getHeadersCount() + positionStart, itemCount);
        }
    }

    //刷新数据区域，即不刷Header和Footer
    protected void notifyDataItemsChanged() {
        try {
            notifyItemRangeChangedWrapper(0, getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void notifyItemChangedWrapper(int position) {
        if (mHeaderAndFooterAdapter == null) {
            notifyItemChanged(position);
        } else {
            mHeaderAndFooterAdapter.notifyItemChanged(mHeaderAndFooterAdapter.getHeadersCount() + position);
        }
    }

    public final void notifyDataSetChangedWrapper() {
        if (mHeaderAndFooterAdapter == null) {
            notifyDataSetChanged();
        } else {
            mHeaderAndFooterAdapter.notifyDataSetChanged();
        }
    }

    public void addHeaderView(View headerView) {
        getHeaderAndFooterAdapter().addHeaderView(headerView);
    }

    public void addFooterView(View footerView) {
        getHeaderAndFooterAdapter().addFooterView(footerView);
    }

    public int getHeadersCount() {
        return mHeaderAndFooterAdapter == null ? 0 : mHeaderAndFooterAdapter.getHeadersCount();
    }

    public int getFootersCount() {
        return mHeaderAndFooterAdapter == null ? 0 : mHeaderAndFooterAdapter.getFootersCount();
    }

    public HeaderAndFooterAdapter getHeaderAndFooterAdapter() {
        if (mHeaderAndFooterAdapter == null) {
            synchronized (RecyclerViewAdapter.this) {
                if (mHeaderAndFooterAdapter == null) {
                    mHeaderAndFooterAdapter = new HeaderAndFooterAdapter(this);
                }
            }
        }
        return mHeaderAndFooterAdapter;
    }

}
