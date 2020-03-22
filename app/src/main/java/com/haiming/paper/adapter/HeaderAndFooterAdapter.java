package com.haiming.paper.adapter;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 带Header和Footer Item；
 * 参考：https://github.com/hongyangAndroid/baseAdapter
 * Created by Xuefu_Du on 2017/7/9.
 */
public class HeaderAndFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 实际数据项的getItemViewType要在[0,2048)这个区间；
     */
    private static final int BASE_ITEM_TYPE_HEADER = 2048;
    private static final int BASE_ITEM_TYPE_FOOTER = 4096;
    @NonNull
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    @NonNull
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();
    @NonNull
    private RecyclerView.Adapter mInnerAdapter;

    HeaderAndFooterAdapter(@NonNull RecyclerView.Adapter innerAdapter) {
        mInnerAdapter = innerAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            // header 类型
            return new RecyclerView.ViewHolder(mHeaderViews.get(viewType)) {
            };
        } else if (mFootViews.get(viewType) != null) {
            // footer 类型
            return new RecyclerView.ViewHolder(mFootViews.get(viewType)) {
            };
        } else {
            // 真实 item 类型
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            // 返回 header 的 itemType
            return mHeaderViews.keyAt(position);
        } else if (isFooterView(position)) {
            // 返回 footer 的 itemType
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        } else {
            // 返回真实 item 的 itemType
            return mInnerAdapter.getItemViewType(getRealItemPosition(position));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 如果是 header 或 footer 就不绑定数据
        if (isHeaderViewOrFooterView(position)) {
            return;
        }

        mInnerAdapter.onBindViewHolder(holder, getRealItemPosition(position));
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeaderViewOrFooterView(position)) {
                        // header 或 footer 时宽度占满父控件
                        return gridLayoutManager.getSpanCount();
                    } else {
                        // 真实的 item
                        if (spanSizeLookup != null) {
                            return spanSizeLookup.getSpanSize(position);
                        }
                        return 1;
                    }
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewOrFooterView(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    /**
     * @return 获取除去 header 和 footer 后真实的 item 总数
     */
    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    /**
     * @return 获取真实 item 的索引
     */
    private int getRealItemPosition(int position) {
        return position - getHeadersCount();
    }

    /**
     * @return 索引为 position 的 item 是否是 header
     */
    private boolean isHeaderView(int position) {
        return position < getHeadersCount();
    }

    /**
     * @return 索引为 position 的 item 是否是 footer
     */
    private boolean isFooterView(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    /**
     * @return 索引为 position 的 item 是否是 header 或 footer
     */
    private boolean isHeaderViewOrFooterView(int position) {
        return isHeaderView(position) || isFooterView(position);
    }

    /**
     * @param view 添加 header
     */
    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    /**
     * @param view 添加 footer
     */
    public void addFooterView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    /**
     * @return 获取 header 的个数
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    /**
     * @return 获取 footer 的个数
     */
    public int getFootersCount() {
        return mFootViews.size();
    }
}
