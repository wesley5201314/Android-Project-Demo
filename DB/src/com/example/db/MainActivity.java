package com.example.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.adapter.PersonAdapter;
import com.example.domain.Person;
import com.example.services.PersonService;

public class MainActivity extends Activity {
	
	private ListView listView;
    private PersonService personService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		personService = new PersonService(this);
        listView = (ListView) this.findViewById(R.id.listView);
        //创建列表监听器
        listView.setOnItemClickListener(new ItemClickListener());
        //展示数据
        show3();
	}
	
	/**
	 * 列表监听器
	 */
	private final class ItemClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ListView lView = (ListView)parent;
			//自定义适配器
			Person person = (Person) lView.getItemAtPosition(position);
			Toast.makeText(getApplicationContext(), person.getId().toString(), 1).show();
            /*
			Cursor cursor = (Cursor) lView.getItemAtPosition(position);
			int personid = cursor.getInt(cursor.getColumnIndex("_id"));
			Toast.makeText(getApplicationContext(), personid+ "", 1).show();*/
			}
    }
	
	private void show3() {
		List<Person> persons = personService.getScrollData(0, 20);
		PersonAdapter adapter = new PersonAdapter(this, persons, R.layout.item);
		listView.setAdapter(adapter);
	}
	
	private void show2() {
		Cursor cursor = personService.getCursorScrollData(0, 20);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor,
				new String[]{"name", "phone", "amount"}, new int[]{R.id.name, R.id.phone, R.id.amount});
		listView.setAdapter(adapter);
	}
	
	private void show() {
		//获取信息
		List<Person> persons = personService.getScrollData(0, 20);
		//创建数据集合
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		for(Person person : persons){
			//创建元素集合
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("name", person.getName());
			item.put("phone", person.getPhone());
			item.put("amount", person.getAmount());
			item.put("id", person.getId());
			data.add(item);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item,
				new String[]{"name", "phone", "amount"}, new int[]{R.id.name, R.id.phone, R.id.amount});
		
		listView.setAdapter(adapter);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
