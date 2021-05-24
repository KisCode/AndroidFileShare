package demo.kiscode.fileshare.adapter;

import android.text.format.Formatter;

import androidx.annotation.NonNull;

import java.util.List;

import demo.kiscode.fileshare.R;
import demo.kiscode.fileshare.adapter.comman.CommanAdapter;
import demo.kiscode.fileshare.adapter.comman.CommanViewHolder;
import demo.kiscode.fileshare.pojo.FileModel;
import demo.kiscode.fileshare.util.FileIcons;

/**
 * Description: 文件列表内容适配器
 * Author:keno
 * Date : 2021/5/21
 **/
public class FileAdapter extends CommanAdapter<FileModel> {


    public FileAdapter(List<FileModel> dataList) {
        super(R.layout.item_file, dataList);
    }

    @Override
    public void onBindViewHolder(@NonNull CommanViewHolder holder, int position) {
        FileModel fileModel = mDataList.get(position);
        holder.setText(R.id.tv_name_file, fileModel.getName());
        holder.setText(R.id.tv_size_file, Formatter.formatFileSize(mContext, fileModel.getSize()));
        holder.setImageResource(R.id.iv_icon_file, FileIcons.getFileIconRes(mContext, fileModel.getName()));
    }
}