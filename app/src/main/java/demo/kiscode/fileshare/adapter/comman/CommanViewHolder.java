package demo.kiscode.fileshare.adapter.comman;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description: 通用RecyclerView.ViewHolde
 * Author: keno
 * Date : 2021/5/24 18:10
 **/
public class CommanViewHolder extends RecyclerView.ViewHolder {
    private View mItemView;

    private Context mContext;

    private SparseArray<View> mViews;

    public CommanViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        this.mContext = context;
        this.mItemView = itemView;
    }

    public static CommanViewHolder createViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutId) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new CommanViewHolder(context, itemView);
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = mItemView.findViewById(viewId);
        if (mViews.get(viewId) == null) {
            mViews.put(viewId, view);
        }
        return (T) mViews.get(viewId);
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommanViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public CommanViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }
}