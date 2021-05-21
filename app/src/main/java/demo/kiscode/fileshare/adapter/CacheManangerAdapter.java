package demo.kiscode.fileshare.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import demo.kiscode.fileshare.R;
import demo.kiscode.fileshare.pojo.CacheModel;

/**
 * Description:
 * Author:
 * Date : 2021/5/14 1keno5:11
 **/
public class CacheManangerAdapter extends RecyclerView.Adapter<CacheManangerAdapter.ItemViewHolder> {
    private List<CacheModel> mDatas;
    private Context mContext;
    private OnItemClickListener mItemOnClickListener;

    public CacheManangerAdapter(List<CacheModel> datas) {
        this.mDatas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener mItemOnClickListener) {
        this.mItemOnClickListener = mItemOnClickListener;
    }

    @NonNull
    @Override
    public CacheManangerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_cache_mananger, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CacheModel cacheModel = mDatas.get(position);

        holder.tvDirName.setText(cacheModel.getPathType().name());
        holder.tvDirPath.setText(cacheModel.getPathAbsolute());
        holder.tvSize.setText(Formatter.formatFileSize(mContext, cacheModel.getTotalSize()));

        holder.itemView.setOnClickListener(v -> {
            if (mItemOnClickListener != null) {
                mItemOnClickListener.onClick(cacheModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setNewData(List<CacheModel> newData) {
        if (newData == mDatas) {
            return;
        }
        mDatas = newData;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(CacheModel cacheModel);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvDirName, tvDirPath, tvSize;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDirName = itemView.findViewById(R.id.tv_name_file);
            tvDirPath = itemView.findViewById(R.id.tv_size_file);
            tvSize = itemView.findViewById(R.id.tv_size_dir);
        }
    }
}