package rayfantasy.icode.Ui;

import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import rayfantasy.icode.*;
import rayfantasy.icode.Ui.Activity.*;
import rayfantasy.icode.Ui.Fragment.*;
import rayfantasy.icode.Data.Var;

import android.support.design.widget.*;
import android.support.v4.view.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;

import com.tencent.bugly.crashreport.*;
import cn.bmob.v3.*;
import cn.bmob.v3.update.*;
import com.amulyakhare.textdrawable.*;
import java.util.*;
import android.net.*;
import java.io.*;
import de.hdodenhof.circleimageview.CircleImageView;
import android.support.v4.widget.*;
import rayfantasy.icode.Bmob.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;
import android.content.res.*;
import cn.bmob.v3.datatype.*;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
	private Toolbar toolbar;
    
	private DrawerLayout drawerLayout;
	private NavigationView navigationView;
	
	private MyApplication myApplication;
	//用户名称
	private TextView userName;
	//获取本地登录的bmob信息
	private User bmobUser;
	//圆形头像
	private TextDrawable drawableBuilder;
	private CircleImageView mCircleImageView;
	private int drawerLayoutCheck = GravityCompat.START;
	
	private RelativeLayout HeadLayout;
	private int HeadColor;
	private String About;
	private ColorStateList csl;
	private Window window;
	private String path=Environment.getExternalStorageDirectory().getPath()+"/.iCode";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		init();
        setContentView(R.layout.activity_main);
		initView();
		
		if(myApplication.isNetwork(this)){
			initData();
		}else{
			showDialog("无网络","请检查网络状态！","设置","取消","Network");
		}
    }

	private void init()
	{
		myApplication=(MyApplication)getApplication();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			window = getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		BmobUpdateAgent.setUpdateOnlyWifi(false);//在任何环境下提示更新
		BmobUpdateAgent.update(this);//调用更新
	}
	
	private void initView()
	{
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		HeadLayout=(RelativeLayout)findViewById(R.id.headerRelativeLayout1);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem)
				{
					if(menuItem.isChecked()) menuItem.setChecked(false);
					else menuItem.setChecked(true);
					closeDrawer();
					switch (menuItem.getItemId()){
						case R.id.drawer_home:
							setTabInt(1);
							return true;
						case R.id.drawer_material:
							setTabInt(2);
							return true;
						case R.id.drawer_bug:
							return true;
						case R.id.drawer_usertheme:
							//主题选择
							return true;
						case R.id.drawer_setting:
							return true;
						case R.id.drawer_about:
							return true;
					}
					return false;
				}
			});
		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();
		userName=(TextView)findViewById(R.id.main_username);
		mCircleImageView=(CircleImageView)findViewById(R.id.icode_user);
		mCircleImageView.setOnClickListener(this);
	}

	private void initUser()
	{
		bmobUser = BmobUser.getCurrentUser(User.class);
		//如果本地用户不为null证明登录了
		if(bmobUser != null){
			HeadColor=myApplication.getHeadColor((String)bmobUser.getObjectByKey("HeadColor"));
			About=(String)bmobUser.getObjectByKey("About");
			userName.setText(bmobUser.getUsername());
			if(bmobUser.getHeadVersion()!=null&&myApplication.noEquals(bmobUser.getHeadVersion().intValue()+"","0")){
				if(myApplication.isFile("/cache/"+bmobUser.getEmail()+"_"+bmobUser.getHeadVersion()+".png")){
					mCircleImageView.setBackgroundResource(0);
					mCircleImageView.setImageBitmap(BitmapFactory.decodeFile(path+"/cache/"+bmobUser.getEmail()+"_"+bmobUser.getHeadVersion()+".png"));
				}else{
					//未缓存下载
					myApplication.downloadFile(new BmobFile(bmobUser.getEmail(),"",bmobUser.getHeadUri()),bmobUser.getEmail()+"_"+bmobUser.getHeadVersion(),mCircleImageView);
				}
			}else{
				mCircleImageView.setImageResource(0);
				mCircleImageView.setBackground(drawableBuilder.builder().buildRound(bmobUser.getUsername().subSequence(0,1).toString(),HeadColor-1000));
			}
		}else{
			HeadColor=getResources().getColor(R.color.PrimaryColor);
			userName.setText("登录iCode");
			mCircleImageView.setBackgroundResource(0);
			mCircleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle_white));
			About="没有更多了";
		}
		initTheme();
	}
	
	private void initTheme(){
		toolbar.setBackgroundColor(HeadColor);
		HeadLayout.setBackgroundColor(HeadColor);
		csl=myApplication.createSelector(HeadColor,getResources().getColor(R.color.csl_color),HeadColor,getResources().getColor(R.color.csl_color));
		navigationView.setItemTextColor(csl);
		navigationView.setItemIconTintList(csl);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			window.setNavigationBarColor(HeadColor);
		}
	}
	
	private void initData()
	{
		getFragmentManager().beginTransaction().replace(R.id.frame,new dataFragment()).commit();
	}
	
	@Override
	public void onClick(View p1)
	{
		switch(p1.getId()){
			case R.id.icode_user:
				//点击头像跳转
				closeDrawer();
				//这里注意不要跳转空的数值
				Intent i=new Intent(MainActivity.this,userActivity.class);
				i.putExtra("UserName",userName.getText().toString());
				i.putExtra("About",About);
				i.putExtra("HeadColor",HeadColor);
				startActivity(i);
			break;		
		}
	}
	
	//无网络跳转
	public void NetworkIntent(){
		Intent intent = null;
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
		} else {
			intent = new Intent();
			ComponentName component = new ComponentName(
				"com.android.settings",
				"com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}
		startActivity(intent);
		myApplication.showToast("当前无网络");
	}
	
	public int setTabInt(int i){
		return i;
	}
	
	public void closeDrawer(){
		drawerLayout.closeDrawer(drawerLayoutCheck);
	}
	
	public void openDrawer(){
		drawerLayout.openDrawer(drawerLayoutCheck);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		initUser();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			showDialog("退出","你确定要退出？","确定","取消","finish");
		}
		return false;
	}

	private void showDialog(String Title,String Message,String Position,String Negative,final String Listener){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(Title);
		builder.setMessage(Message);
		builder.setPositiveButton(Position, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch(Listener){
						case "finish":
							finish();
						break;
						case "Network":
							NetworkIntent();
						break;
					}
				}
			});

		builder.setNegativeButton(Negative, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		builder.create().show();
	}
	
}
