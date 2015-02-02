package com.example.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.domain.Person;
import com.example.services.DBHelper;
import com.example.services.PersonService;

/**
 * @author wesley
 * @version 
 * @date 2015年1月27日 上午10:41:27
 * 
 */
public class DBTest extends AndroidTestCase {
	
	private static final String TAG = "PersonServiceTest";
	
	public void testDBCreate(){
		//创建数据库以及表测试！
		DBHelper dbHelper = new DBHelper(getContext());
		dbHelper.getWritableDatabase();
	}
	
	
	public void testSave() throws Exception{
		PersonService service = new PersonService(this.getContext());
		for(int i = 0 ; i < 20 ; i++){
			Person person = new Person("zhangxx"+ i, "136765765"+ i, 100);
			service.save(person);
		}
	}
	
	public void testDelete() throws Exception{
		PersonService service = new PersonService(this.getContext());
		service.delete(12);
	}
	
	public void testUpdate() throws Exception{
		PersonService service = new PersonService(this.getContext());
		Person person = service.find(1);
		person.setName("zhangxiaoxiao");
		service.update(person);
	}
	
	public void testFind() throws Exception{
		PersonService service = new PersonService(this.getContext());
		Person person = service.find(1);
		Log.i(TAG, person.toString());
	}
	
	public void testScrollData() throws Exception{
		PersonService service = new PersonService(this.getContext());
		List<Person> persons = service.getScrollData(0, 22);
		for(Person person : persons){
			Log.i(TAG, person.toString());
		}
	}
	
	public void testCount() throws Exception{
		PersonService service = new PersonService(this.getContext());
		long result = service.getCount();
		Log.i(TAG, result+"");
	}
	
	public void testPayment() throws Exception{
		PersonService service = new PersonService(this.getContext());
		service.payment();
	}
	
	public void testUpdateAmount() throws Exception{
		PersonService service = new PersonService(this.getContext());
		Person person1 = service.find(1);
		Person person2 = service.find(2);
		person1.setAmount(100);
		person2.setAmount(50);
		service.update(person1);
		service.update(person2);
		
	}
}
