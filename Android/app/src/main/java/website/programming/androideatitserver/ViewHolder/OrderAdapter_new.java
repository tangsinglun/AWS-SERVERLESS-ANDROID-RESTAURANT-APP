package website.programming.androideatitserver.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Interface.ItemClickListener;
import website.programming.androideatitserver.Model.Food;
import website.programming.androideatitserver.Model.Order;
import website.programming.androideatitserver.Model.Request;
import website.programming.androideatitserver.OrderDetail;
import website.programming.androideatitserver.OrderStatus;
import website.programming.androideatitserver.R;
import website.programming.androideatitserver.TrackingOrder;

/**
 * Created by cokel on 4/1/2018.
 */
public class OrderAdapter_new extends RecyclerView.Adapter<OrderViewHolder>  {

    Context context;
    ArrayList<Request> listdata = new ArrayList<Request>();
    Request RequestDel;
    //Activity activity;

    public OrderAdapter_new(ArrayList<Request> data, Context context) {
        this.listdata = data;
        this.context = context;
        //this.activity = activity;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.order_layout, parent, false);
        return  new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder,final int position) {


    }

    public  ArrayList<Request> getListdata(){
        return listdata;
    }


    @Override
    public int getItemCount() {
        if (listdata!=null)
            return listdata.size();
        else
            return 0;
    }
}


