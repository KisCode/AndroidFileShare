package demo.kiscode.fileshare.adapter;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.List;

import demo.kiscode.fileshare.R;
import demo.kiscode.fileshare.adapter.comman.CommanAdapter;
import demo.kiscode.fileshare.adapter.comman.CommanViewHolder;
import demo.kiscode.fileshare.biz.FileMananger;
import demo.kiscode.fileshare.contants.PathType;

/**
 * Description: 接收文件列表內容適配器
 * Author:keno
 * Date : 2021/5/14
 **/
public class ReceivePathAdapter extends CommanAdapter<PathType> {
    public ReceivePathAdapter(List<PathType> dataList) {
        super(R.layout.item_receive_path, dataList);
    }

    @Override
    public void onBindViewHolder(@NonNull CommanViewHolder holder, int position) {
        PathType pathType = getItem(position);

        File dir = FileMananger.getDirByCode(mContext, pathType);
        String path = "";
        if (dir != null) {
            path = dir.getAbsolutePath();
        }
        holder.setText(R.id.tv_name_file, pathType.name());
        holder.setText(R.id.tv_path_file, path);
    }

}