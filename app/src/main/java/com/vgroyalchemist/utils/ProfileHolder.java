package com.vgroyalchemist.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.unnamed.b.atv.model.TreeNode;

public class ProfileHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {

    private TextView txtEmpty;
    private View view;
    private LayoutInflater inflater;
    private TextView tvValue;
    private int level;
    MainActivity mainActivity;
//    private CategoryVO categoryVO;
    CommonHelper mCommonHelper;




    public ProfileHolder(Context context, int level) {
        super(context);
        this.level = level;
        mainActivity = (MainActivity) context;
        mCommonHelper = new CommonHelper();

    }

    @Override
    public View createNodeView(TreeNode node, final IconTreeItemHolder.IconTreeItem value) {
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_profile_node, null, false);



        tvValue = view.findViewById(R.id.node_value);
        tvValue.setText(value.text);
        txtEmpty = view.findViewById(R.id.txtProfileEmpty);

        setPadding(level);
//        tvValue.setTag(categoryVO);
//        view.setTag(categoryVO);

//        Log.v("log_tag", "Name " + value.text);
//        Log.v("log_tag", "Image URL " + value.imageuRL);;


//        tvValue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!mCommonHelper.check_Internet(mainActivity)) {
//                    Snackbar.with(mainActivity).text(mainActivity.getResources().getString(R.string.interneterror)).show(mainActivity);
//                    return;
//                }
//
//
//                FragmentProductListing mFragmentListData = new FragmentProductListing();
//                Bundle args = new Bundle();
//                mFragmentListData.setArguments(args);
//                mainActivity.addFragment(mFragmentListData, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentListData.getClass().getName());
//
//            }
//        });

        return view;
    }

    @Override
    public void toggle(boolean active) {
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }

    public void setPadding(int level) {
        if (txtEmpty != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            params.setMargins(level * 25, 0, 0, 0);
//            params.setMargins(0, 0, 0, 0);
            txtEmpty.setLayoutParams(params);
        }
    }
}