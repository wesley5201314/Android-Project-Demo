package com.qianyan.chat.share;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianyan.chat.R;

public class AppAdapter extends BaseAdapter {

	private Context context = null;
	private LayoutInflater inflater = null;
	private ArrayList<PackageInfo> infoList = null;

	@SuppressWarnings("static-access")
	public AppAdapter(Context context, ArrayList<PackageInfo> infoList) {
		this.context = context;
		this.infoList = infoList;
		this.inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return this.infoList.size();
	}

	public Object getItem(int position) {
		return this.infoList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// ��ֹ�ڴ�й©
		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.app_item, null);
		}
		// ��ʼ�� app_item.xml
		// View view = this.inflater.inflate(R.layout.app_item, null);
		ImageView img = (ImageView) convertView.findViewById(R.id.img_app_icon);
		TextView txtName = (TextView) convertView
				.findViewById(R.id.txt_app_name);
		// TextView txtPkg = (TextView)
		// convertView.findViewById(R.id.txt_app_package);
		// ͼƬ
		img.setImageDrawable(this.infoList.get(position).applicationInfo
				.loadIcon(context.getPackageManager()));
		// ����
		txtName.setText(context.getResources()
				.getString(R.string.app_soft)
				+ this.infoList.get(position).applicationInfo.loadLabel(context
						.getPackageManager()));
		// ����
		// txtPkg.setText(context.getResources().getString(R.string.app_item_pkg)
		// + this.infoList.get(position).applicationInfo.packageName);
		return convertView;
	}

}
