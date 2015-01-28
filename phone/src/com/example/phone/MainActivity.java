package com.example.phone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private Button button;
	private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)this.findViewById(R.id.editText);
        button = (Button)this.findViewById(R.id.button);
        button.setOnClickListener(new ButtonListener());
    }
    
    /**
     * 
     * @Description 点击按钮事件的监听器
     * @author wesley
     * @date 2015年1月21日 下午3:33:40
     *
     */
    private final class ButtonListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		String str = editText.getText().toString();
    		Intent i = new Intent();
    		i.setAction("android.intent.action.CALL");
    		i.setData(Uri.parse("tel:"+str));
    		startActivity(i);
    	}
    }
	
}
