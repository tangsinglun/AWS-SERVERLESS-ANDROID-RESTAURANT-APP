package website.programming.androideatitserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Database.DatabaseSelectServices;
import website.programming.androideatitserver.Database.ParseJsonOrderData;
import website.programming.androideatitserver.Database.ParseJsonOrderDetail;
import website.programming.androideatitserver.Model.Order;
import website.programming.androideatitserver.Model.Request;
import website.programming.androideatitserver.ViewHolder.OrderAdapter_new;
import website.programming.androideatitserver.ViewHolder.OrderDetailAdapter;

public class OrderDetail extends AppCompatActivity {

    TextView order_id, order_phone, order_address, order_total, order_comment;
    String order_id_value = "";
    RecyclerView lstFoods;
    RecyclerView.LayoutManager layoutManager;

    String parse_json = null, method;
    ArrayList<Order> jsondata;
    ArrayList<Order> Emptyjsondata = null;
    String BufferNoData = "Nodata";
    DatabaseSelectServices databaseSelectServices = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id = (TextView)findViewById(R.id.order_id);
        order_phone = (TextView)findViewById(R.id.order_phone);
        order_address = (TextView)findViewById(R.id.order_address);
        order_total = (TextView)findViewById(R.id.order_total);
        order_comment = (TextView)findViewById(R.id.order_comment);

        lstFoods = (RecyclerView)findViewById(R.id.lstFoods);
        lstFoods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstFoods.setLayoutManager(layoutManager);

        if(getIntent()!=null)
            order_id_value = getIntent().getStringExtra("OrderId");

        //Set Value
        order_id.setText(order_id_value);
        order_phone.setText(Common.currentRequest.getPhone());
        order_address.setText(Common.currentRequest.getAddress());
        order_total.setText(Common.currentRequest.getTotal());
        order_comment.setText(Common.currentRequest.getComment());


        try {
            method = "AllOrderDetail";
            DatabaseSelectServices databaseSelectServices = new DatabaseSelectServices();
            parse_json = databaseSelectServices.execute(method,order_id_value).get();
            //parse_json = databaseSelectServices.parse_json;
            Log.i("parse_json", parse_json);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (parse_json.matches(BufferNoData)) {
            Toast.makeText(OrderDetail.this, "No Order Detail!", Toast.LENGTH_SHORT).show();
        } else {
            ParseJsonOrderDetail parseJsonOrderDetail = new ParseJsonOrderDetail(parse_json,order_id_value);
            jsondata = parseJsonOrderDetail.getOrderDetailData();
            Log.i("Jsondata count:", String.valueOf(jsondata.size()));
            OrderDetailAdapter adapter = new OrderDetailAdapter(jsondata);
            adapter.notifyDataSetChanged();
            lstFoods.setAdapter(adapter);
        }




    }
}
