package demo.kiscode.fileshare.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import demo.kiscode.fileshare.R;
import demo.kiscode.fileshare.pojo.FileModel;
import demo.kiscode.fileshare.util.FileIcons;

/**
 * Description:
 * Author:keno
 * Date : 2021/5/21
 **/
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ItemViewHolder> {
    private List<FileModel> mDatas;
    private Context mContext;
    private OnItemClickListener mItemOnClickListener;

    public FileAdapter(List<FileModel> datas) {
        this.mDatas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener mItemOnClickListener) {
        this.mItemOnClickListener = mItemOnClickListener;
    }

    @NonNull
    @Override
    public FileAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        FileModel fileModel = mDatas.get(position);

        holder.tvName.setText(fileModel.getName());
        holder.tvSize.setText(Formatter.formatFileSize(mContext, fileModel.getSize()));
        holder.ivIcon.setImageResource(FileIcons.getFileIconRes(mContext, fileModel.getName()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemOnClickListener != null) {
                    mItemOnClickListener.onClick(fileModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setNewData(List<FileModel> newData) {
        if (newData != mDatas) {
            mDatas = newData;
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onClick(FileModel fileModel);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvSize;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon_file);
            tvName = itemView.findViewById(R.id.tv_name_file);
            tvSize = itemView.findViewById(R.id.tv_size_file);
        }
    }
}