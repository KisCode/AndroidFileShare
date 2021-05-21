package demo.kiscode.fileshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import demo.kiscode.fileshare.R;
import demo.kiscode.fileshare.biz.FileMananger;
import demo.kiscode.fileshare.contants.PathType;

/**
 * Description:
 * Author:
 * Date : 2021/5/14 1keno5:11
 **/
public class ReceivePathAdapter extends RecyclerView.Adapter<ReceivePathAdapter.ItemViewHolder> {
    private List<PathType> mDatas;
    private Context mContext;
    private OnItemClickListener mItemOnClickListener;

    public ReceivePathAdapter(List<PathType> datas) {
        this.mDatas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener mItemOnClickListener) {
        this.mItemOnClickListener = mItemOnClickListener;
    }

    @NonNull
    @Override
    public ReceivePathAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_receive_path, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PathType pathType = mDatas.get(position);
        holder.tvDirName.setText(pathType.name());

        File dir = FileMananger.getDirByCode(mContext, pathType);
        if (dir != null) {
            holder.tvDirPath.setText(dir.getAbsolutePath());
        } else {
            holder.tvDirPath.setText("");
        }

        holder.itemView.setOnClickListener(v -> {
            if (mItemOnClickListener != null) {
                mItemOnClickListener.onClick(pathType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public interface OnItemClickListener {
        void onClick(PathType pathType);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvDirName, tvDirPath;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDirName = itemView.findViewById(R.id.tv_name_file);
            tvDirPath = itemView.findViewById(R.id.tv_size_file);
        }
    }
}