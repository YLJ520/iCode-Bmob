package rayfantasy.icode.Adapter;

import android.graphics.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.amulyakhare.textdrawable.*;
import java.util.*;
import rayfantasy.icode.*;
import rayfantasy.icode.Bmob.*;
import de.hdodenhof.circleimageview.*;
import rayfantasy.icode.R;
import android.support.v7.widget.RecyclerView.ViewHolder;

public class dataRecyclerViewHolder extends RecyclerView.Adapter<ViewHolder>
{
	private List<Data> dataList;
	private TextDrawable drawableBuilder;
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_FOOTER = 1;
	
	public dataRecyclerViewHolder(List<Data> dataList){
		this.dataList=dataList;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		//View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_text, viewGroup, false);
		//return new MyViewHolder(itemView);
		if (viewType == TYPE_ITEM) {
			View view = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.
				cardview_text, null);
			return new MyViewHolder(view);
		}else {
			View view = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.loadmore, null);
			return new FooterViewHolder(view);
		}

		
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int i)
	{
		Data data = dataList.get(i);
		if(holder instanceof MyViewHolder){
		
			MyViewHolder itemview=(MyViewHolder)holder;
			itemview.title.setText(data.getTitle());
			itemview.bg.setElevation(5);
			itemview.message.setText(data.getMessage());
			itemview.time.setText(data.getCreatedAt());
			itemview.user.setText(data.getUser());
			//根据用户名称的第一位字符设置头像
			itemview.userimage.setBackground(drawableBuilder.builder().buildRound(itemview.user.getText().toString().subSequence(0,1).toString(),
																				  getUserRandomColor()));
																				  
		}
	}

	@Override
	public int getItemCount()
	{
		return dataList.size();
	}
	
	public int getUserRandomColor(){
		return Color.rgb((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, message, time, user;
        public CardView bg;
		public CircleImageView userimage;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_title);
            message = (TextView) itemView.findViewById(R.id.list_message);
            time = (TextView) itemView.findViewById(R.id.list_time);
			user = (TextView) itemView.findViewById(R.id.list_user);
			userimage=(CircleImageView)itemView.findViewById(R.id.cardviewtextImageView1);
			
            bg = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
	
	class FooterViewHolder extends ViewHolder {

		public FooterViewHolder(View view) {
			super(view);
		}

	}
}
