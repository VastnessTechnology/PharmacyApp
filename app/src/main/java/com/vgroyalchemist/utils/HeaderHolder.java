package com.vgroyalchemist.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.unnamed.b.atv.model.TreeNode;


public class HeaderHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {

    //    private PrintView arrowView;
    private ImageView arrowView;
    private LayoutInflater inflater;
    private View view;
    private TextView tvValue;
    private TextView txtEmpty;
    private int level;
    MainActivity mainActivity;
//    CategoryVO categoryVO;


    public HeaderHolder(Context context, int level) {
        super(context);
        mainActivity = (MainActivity) context;
        this.level = level;
    }

    @Override
    public View createNodeView(TreeNode node, IconTreeItemHolder.IconTreeItem value) {

        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_header_node, null, false);
        tvValue = view.findViewById(R.id.header_node_value_textview);
        txtEmpty = view.findViewById(R.id.txtHeaderEmpty);

        tvValue.setText(value.text);
        arrowView = view.findViewById(R.id.arrow_icon);

        if (node.isLeaf()) {
            arrowView.setVisibility(View.GONE);
        }

//        view.setTag(categoryVO);
//        tvValue.setTag(categoryVO);
        setPadding(level);


//        tvValue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                CategoryVO tempVO = (CategoryVO) v.getTag();
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
//        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
        arrowView.setImageDrawable(context.getResources().getDrawable(active ? R.drawable.ic_minus : R.drawable.ic_add));
    }

    public void setPadding(int level) {
        if (txtEmpty != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            params.setMargins(level * 25, 0, 0, 0);
            txtEmpty.setLayoutParams(params);
        }
    }
}
