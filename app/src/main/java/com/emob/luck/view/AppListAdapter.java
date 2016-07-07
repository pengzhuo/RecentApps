package com.emob.luck.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emob.lib.util.Utils;
import com.emob.luck.model.PackageElement;

public class AppListAdapter extends BaseAdapter {
    private ArrayList<PackageElement> mList;
//    private LayoutInflater inflater; 
    private Context mContext;

    AppListAdapter(Context context, ArrayList<PackageElement> list){ 
//        inflater = LayoutInflater.from(context); 
        mList = list;
        this.mContext = context;
    } 
    
	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder; 
		 if (convertView == null) 
	        { 
				viewHolder = new ViewHolder(); 
				convertView = initView(parent, viewHolder);
	            convertView.setTag(viewHolder); 
	        } else { 
	            viewHolder = (ViewHolder) convertView.getTag(); 
	        }
	        
		PackageElement pkg = mList.get(position);
		viewHolder.appIcon.setBackgroundDrawable(pkg.getmIcon());
		viewHolder.appName.setText(pkg.getLabel());
		if(pkg.ismIsNative()) {
			viewHolder.noticeImg.setVisibility(View.VISIBLE);
		} else {
			viewHolder.noticeImg.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private View initView(ViewGroup parent,ViewHolder viewHolder) {
		viewHolder.itemRl = new RelativeLayout(mContext);
		AbsListView.LayoutParams gvLp = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		viewHolder.itemRl.setLayoutParams(gvLp);
		
		viewHolder.iconRl = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams iconRlLp = new RelativeLayout.LayoutParams(
				Utils.getDip(mContext, 60), Utils.getDip(mContext, 60));
		iconRlLp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		iconRlLp.topMargin = Utils.getDip(mContext, 16);
		viewHolder.iconRl.setLayoutParams(iconRlLp);
		viewHolder.iconRl.setId(0);
		
		viewHolder.appIcon = new ImageView(mContext);
		RelativeLayout.LayoutParams iconLp = new RelativeLayout.LayoutParams(
				Utils.getDip(mContext, 56), Utils.getDip(mContext, 56));
		viewHolder.appIcon.setLayoutParams(iconLp);
		iconRlLp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		iconRlLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		//viewHolder.appIcon.setPadding(0, Utils.getDip(mContext, 4), 0, 0);
		iconLp.topMargin = Utils.getDip(mContext, 4);
		viewHolder.iconRl.addView(viewHolder.appIcon);
		
		viewHolder.noticeImg = new Button(mContext);
		RelativeLayout.LayoutParams noticeLp = new RelativeLayout.LayoutParams(
				Utils.getDip(mContext, 23), Utils.getDip(mContext, 23));
		noticeLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		noticeLp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//		noticeLp.setMargins(0, 0, 0, 0);
		viewHolder.noticeImg.setLayoutParams(noticeLp);
		viewHolder.noticeImg.setFocusable(false);
		viewHolder.noticeImg.setClickable(false);
		viewHolder.noticeImg.setPadding(0, 0, 0, 0);
		viewHolder.noticeImg.setGravity(Gravity.CENTER);
		viewHolder.noticeImg.setBackgroundDrawable(Utils.getBitmapdDrawable("eyu_icon_notice.png"));
		viewHolder.noticeImg.setText("1");
		viewHolder.noticeImg.setTextColor(0xffffffff);
		viewHolder.noticeImg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
		viewHolder.noticeImg.setVisibility(View.GONE);
		viewHolder.iconRl.addView(viewHolder.noticeImg);
		
		viewHolder.appName = new TextView(mContext);
		RelativeLayout.LayoutParams appNameLP = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		appNameLP.addRule(RelativeLayout.BELOW, 0);
		appNameLP.setMargins(0, Utils.getDip(mContext, 80), 0, 0);
//		appNameLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		appNameLP.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		viewHolder.appName.setLayoutParams(appNameLP);
		viewHolder.appName.setGravity(Gravity.CENTER_HORIZONTAL);
		viewHolder.appName.setGravity(Gravity.CENTER_VERTICAL);
		viewHolder.appName.setSingleLine();
		viewHolder.appName.setEllipsize(TruncateAt.END);
		viewHolder.noticeImg.setTextColor(0xffffffff);
		viewHolder.noticeImg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
		
		viewHolder.itemRl.addView(viewHolder.iconRl);
		viewHolder.itemRl.addView(viewHolder.appName);
		return viewHolder.itemRl;
		
	}
	
	class ViewHolder { 
	    public TextView appName; 
	    public ImageView appIcon;
	    public Button noticeImg;
	    public RelativeLayout itemRl;
	    public RelativeLayout iconRl;
	} 
}
