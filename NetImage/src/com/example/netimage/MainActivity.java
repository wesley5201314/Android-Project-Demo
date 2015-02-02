package com.example.netimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.services.ImageService;

public class MainActivity extends Activity {

	private EditText imagePath;
	private Button button;
	private ImageView imageView;
	private Bitmap bitmap = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imagePath = (EditText) this.findViewById(R.id.editText1);
		button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(new ButtonListenerEvent2());
		imageView = (ImageView) this.findViewById(R.id.imageView1);

		//安卓在android2.3之后 在主线程中必须使用另一个线程  如handler机制，或者异步任务获取网络数据
		//如果你访问网络的操作 必须放在主线程中执行，那么 在oncreate()中添加以下代码
		/*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());*/
	}

	//第一种获取网络资源的方式 （在主线程访问网络）
	private final class ButtonListenerEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
			String path = imagePath.getText().toString();
			// 获取图像资源
			try {
				byte[] data = ImageService.getImage(path);
				// 设置图像的位图
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
						data.length);
				imageView.setImageBitmap(bitmap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), R.string.error, 1)
						.show();
			}

		}

	}
	
	//使用handler操作获取网络数据
	private final class ButtonListenerEvent2 implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			final String path = imagePath.getText().toString();
			
			final Handler handler = new Handler(){
				public void handleMessage(Message msg){
					switch (msg.what) {
					case 0:
						imageView.setImageBitmap(bitmap);
						break;
					default:
						Toast.makeText(getApplicationContext(), R.string.error, 1)
						.show();
						break;
					}
				}
			};
			
			new Thread(new Runnable(){
				
				@Override
				public void run(){
					try {
						byte[] data = ImageService.getImage(path);
						// 设置图像的位图
						bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						handler.sendEmptyMessage(0);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), R.string.error, 1)
								.show();
					}
				}
			}).start();
		}
		
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
