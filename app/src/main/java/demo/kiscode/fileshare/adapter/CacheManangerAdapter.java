package demo.kiscode.fileshare.adapter;

import android.text.format.Formatter;

import androidx.annotation.NonNull;

import java.util.List;

import demo.kiscode.fileshare.R;
import demo.kiscode.fileshare.adapter.comman.CommanAdapter;
import demo.kiscode.fileshare.adapter.comman.CommanViewHolder;
import demo.kiscode.fileshare.pojo.CacheModel;

/**
 * Description: 首页缓存分类数据适配器
 * Author:keno
 * Date : 2021/5/14
 **/
public class CacheManangerAdapter extends CommanAdapter<CacheModel> {

    public CacheManangerAdapter(List<CacheModel> dataList) {
        super(R.layout.item_cache_mananger, dataList);
    }

    @Override
    public void onBindViewHolder(@NonNull CommanViewHolder holder, int position) {
        CacheModel cacheModel = getItem(position);
        holder.setText(R.id.tv_name_dir, cacheModel.getPathType().name());
        holder.setText(R.id.tv_path_dir, cacheModel.getPathAbsolute());
        holder.setText(R.id.tv_size_dir, Formatter.formatFileSize(mContext, cacheModel.getTotalSize()));
    }
}