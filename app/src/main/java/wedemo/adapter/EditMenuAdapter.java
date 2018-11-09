package wedemo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sven.huinews.international.R;

import java.util.List;

import wedemo.shot.bean.ToolBean;

public class EditMenuAdapter extends RecyclerView.Adapter<EditMenuAdapter.VH>{
    private List<ToolBean> mToolDatas;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public EditMenuAdapter(List<ToolBean> mToolDatas){
        this.mToolDatas = mToolDatas;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_menu, parent, false);
        return new VH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {
        ToolBean toolBean = mToolDatas.get(position);
        holder.iv.setImageResource(toolBean.getmIconRes());
        holder.tv_name.setText(toolBean.getToolName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClick != null){
                    onItemClick.onClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mToolDatas.size();
    }

    static class VH extends RecyclerView.ViewHolder{

        private ImageView iv;
        private TextView tv_name;

        public VH(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    public interface OnItemClick{
        void onClick(View view,int pos);
    }
}
