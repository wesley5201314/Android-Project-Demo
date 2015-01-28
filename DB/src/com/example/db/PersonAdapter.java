package com.example.db;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.domain.Person;

/**
 * @author wesley
 * @version 
 * @date 2015年1月27日 下午3:45:40
 * 
 */
public class PersonAdapter extends BaseAdapter{
	
	private List<Person> persons;//在绑定的数据
	private int resource;//绑定的条目界面
	private LayoutInflater inflater; //布局填充对象
	
	public PersonAdapter(Context context, List<Person> persons, int resource) {
		this.persons = persons;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 数据总数
	 */
	@Override
	public int getCount() {
		return persons.size();//数据总数
	}

	/**
	 * 获取某个条目的对象
	 */
	@Override
	public Object getItem(int position) {
		return persons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取视图 以及绑定数据
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView nameView = null;
		TextView phoneView = null;
		TextView amountView = null;
		if(convertView==null){
			convertView = inflater.inflate(resource, null);//生成条目界面对象
			nameView = (TextView) convertView.findViewById(R.id.name);
			phoneView = (TextView) convertView.findViewById(R.id.phone);
			amountView = (TextView) convertView.findViewById(R.id.amount);
			
			ViewCache cache = new ViewCache();//创建缓存对象
			cache.nameView = nameView;
			cache.phoneView = phoneView;
			cache.amountView = amountView;			
			convertView.setTag(cache);//设置缓存标签
		} else {
			ViewCache cache = (ViewCache) convertView.getTag();//得到缓存的对象
			nameView = cache.nameView;
			phoneView = cache.phoneView;
			amountView = cache.amountView;
		}
		Person person = persons.get(position);//得到某个条目的对象
		//下面代码实现数据绑定
		nameView.setText(person.getName());
		phoneView.setText(person.getPhone());
		amountView.setText(person.getAmount().toString());
		
		return convertView;
	}
	/**
	 * 缓存对象
	 * 
	 */
	private final class ViewCache{
		public TextView nameView;
		public TextView phoneView;
		public TextView amountView;
	}
}
