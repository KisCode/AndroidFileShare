package demo.kiscode.fileshare.adapter.comman;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: keno
 * Date : 2021/5/24 18:10
 **/
public abstract class CommanAdapter<T> extends RecyclerView.Adapter<CommanViewHolder> {
    protected Context mContext;
    protected List<T> mDataList;
    private @LayoutRes
    int mLayoutResId;

    private OnItemClickListener<T> mOnItemClickListener;
    private OnItemLongClickListener<T> mOnItemLongClickListener;


    public CommanAdapter(@LayoutRes int mLayoutResId, List<T> datalist) {
        this.mLayoutResId = mLayoutResId;
        this.mDataList = datalist;
    }

    public CommanAdapter(@LayoutRes int mLayoutResId) {
        this(mLayoutResId, null);
    }

    public CommanAdapter(List<T> dataList) {
        this(0, dataList);
    }

    @NonNull
    @Override
    public CommanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        CommanViewHolder viewHolder = CommanViewHolder.createViewHolder(parent, mLayoutResId);
        bindViewClickListener(viewHolder);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public T getItem(int position) {
        return mDataList.get(position);
    }

    protected void bindViewClickListener(CommanViewHolder viewHolder) {

        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(CommanAdapter.this, viewHolder.getLayoutPosition()));
        }

        if (mOnItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(v -> {
                mOnItemLongClickListener.onItemLongClick(CommanAdapter.this, viewHolder.getLayoutPosition());
                return true;
            });
        }

    }

    public void setNewData(List<T> newData) {
        this.mDataList = newData == null ? new ArrayList<>() : newData;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<T> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(CommanAdapter<T> adapter, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(CommanAdapter<T> adapter, int position);
    }
}