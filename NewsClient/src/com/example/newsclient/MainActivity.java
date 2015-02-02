package com.example.newsclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.domain.Video;
import com.example.services.VideoService;

public class MainActivity extends Activity {
	private ListView listView;
	private SimpleAdapter adapter =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ListView)this.findViewById(R.id.listView);
		//handler 控制UI的改变
		final Handler handler = new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					listView.setAdapter(adapter);
					break;
				default:
					Toast.makeText(getApplicationContext(), R.string.error, 1)
					.show();
					break;
				}
			}
		};
		//使用线程加载数据
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				  try {
						//List<Video> videos = VideoService.getAll();//需修改成你本机的Http请求路径
						List<Video> videos = VideoService.getJSONAll();//需修改成你本机的Http请求路径
						List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
						for(Video video : videos){
							HashMap<String, Object> item = new HashMap<String, Object>();
							item.put("id", video.getId());
							item.put("name", video.getName());
							item.put("time", getResources().getString(R.string.timelength)
									+ video.getTimelength()+ getResources().getString(R.string.min));
							data.add(item);
						}
						adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.items,
								new String[]{"name", "time"}, new int[]{R.id.name, R.id.time});
						handler.sendEmptyMessage(0);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}).start();
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
