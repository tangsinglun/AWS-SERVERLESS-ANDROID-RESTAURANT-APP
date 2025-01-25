package website.programming.androideatitserver;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Database.DatabaseInsert;
import website.programming.androideatitserver.Database.DatabaseSelectServices;
import website.programming.androideatitserver.Database.ParseJsonFoodData;
import website.programming.androideatitserver.Database.ParseJsonOrderData;
import website.programming.androideatitserver.Interface.ItemClickListener;
import website.programming.androideatitserver.Model.Category;
import website.programming.androideatitserver.Model.Food;
import website.programming.androideatitserver.Model.MyResponse;
import website.programming.androideatitserver.Model.Notification;
import website.programming.androideatitserver.Model.Request;
import website.programming.androideatitserver.Model.Sender;
import website.programming.androideatitserver.Model.Token;
import website.programming.androideatitserver.Remote.APIService;
import website.programming.androideatitserver.ViewHolder.FoodAdapter_new;
import website.programming.androideatitserver.ViewHolder.MenuViewHolder;
import website.programming.androideatitserver.ViewHolder.OrderAdapter_new;
import website.programming.androideatitserver.ViewHolder.OrderViewHolder;


public class OrderStatus extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference requests;

    RecyclerView recylerview;
    RecyclerView.LayoutManager layoutManager;

    String CatergoryId = "";
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    SwipeRefreshLayout swipeRefreshLayout;

    MaterialSpinner spinner;

    APIService mService;


    OrderAdapter_new adapter_new;
    String parse_json = null, method;
    ArrayList<Request> jsondata;
    ArrayList<Request> Emptyjsondata = null;
    String BufferNoData = "Nodata";
    DatabaseSelectServices databaseSelectServices = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init
        mService = Common.getFCMService();

        //Load Menu
        recylerview = (RecyclerView) findViewById(R.id.listOrders);
        recylerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recylerview.setLayoutManager(layoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    loadOrders();
                } else {
                    Toast.makeText(getBaseContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    loadOrders();
                } else {
                    Toast.makeText(getBaseContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


    }

    private void loadOrders() {

        try {
            method = "AllOrderStatus";
            DatabaseSelectServices databaseSelectServices = new DatabaseSelectServices();
            parse_json = databaseSelectServices.execute(method).get();
            //parse_json = databaseSelectServices.parse_json;
            Log.i("parse_json", parse_json);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (parse_json.matches(BufferNoData)) {
            //adapter_new = new OrderAdapter_new(Emptyjsondata, this);
            // adapter_new.notifyDataSetChanged();
            // recylerview.invalidate();
            // recylerview.setAdapter(adapter_new);
            Toast.makeText(OrderStatus.this, "No Order!", Toast.LENGTH_SHORT).show();
        } else {
            ParseJsonOrderData parseJsonOrderData = new ParseJsonOrderData(parse_json, getApplicationContext());
            jsondata = parseJsonOrderData.getRequestData();

            if (jsondata.size() > 0) {
                adapter_new = new OrderAdapter_new(jsondata, this) {

                    @Override
                    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        LayoutInflater inflater = LayoutInflater.from(OrderStatus.this);
                        View itemView = inflater.inflate(R.layout.order_layout, parent, false);
                        return new OrderViewHolder(itemView);
                    }

                    @Override
                    public void onBindViewHolder(OrderViewHolder holder, final int position) {
                        super.onBindViewHolder(holder, position);

                        holder.txtOrderId.setText(jsondata.get(position).getId());
                        holder.txtOrderStatus.setText(Common.convertCodeToStatus(jsondata.get(position).getStatus()));
                        holder.txtOrderAddress.setText(jsondata.get(position).getAddress());
                        holder.txtOrderPhone.setText(jsondata.get(position).getPhone());

                        //New event
                        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showUpdateDialog(jsondata.get(position).getId(),
                                        jsondata.get(position));
                            }
                        });

                        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteCategory(jsondata.get(position).getId());
                                jsondata.remove(position);
                            }
                        });

                        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.currentRequest = jsondata.get(position);
                                Intent orderDetail = new Intent(OrderStatus.this, OrderDetail.class);
                                orderDetail.putExtra("OrderId", jsondata.get(position).getId());
                                startActivity(orderDetail);
                                Log.i("OrderId", jsondata.get(position).getId());
                            }
                        });

                        holder.btnDirection.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //just implement it to fix crash when click to this item
                                Common.currentRequest = jsondata.get(position);
                                Intent trackingOrder = new Intent(OrderStatus.this, TrackingOrder.class);
                                startActivity(trackingOrder);
                            }
                        });
                    }
                };

                adapter_new.notifyDataSetChanged();
                recylerview.setAdapter(adapter_new);
            } else {
                Toast.makeText(OrderStatus.this, "Load Order Failure!", Toast.LENGTH_SHORT).show();
            }

        }
        swipeRefreshLayout.setRefreshing(false);
    }




    private void deleteCategory(String key) {
        try {
            String method="DeleteOrder";
            DatabaseInsert DBinsert = new DatabaseInsert(OrderStatus.this);
            String result = DBinsert.execute(method, key).get();
            adapter_new.notifyDataSetChanged();
            //finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteOrder(adapter.getRef(item.getOrder()).getKey());

        }
        else if(item.getTitle().equals(Common.DETAIL))
        {
            Intent orderDetail = new Intent(OrderStatus.this,OrderDetail.class);
            orderDetail.putExtra("OrderId",adapter.getRef(item.getOrder()).getKey());
            startActivity(orderDetail);
            Log.i("OrderId",adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }*/

 /*  private void deleteOrder(String key) {
        requests.child(key).removeValue();
        adapter.notifyDataSetChanged();
    }*/



    private void showUpdateDialog(final String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");

        LayoutInflater inflater =  this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout,null);

        spinner = (MaterialSpinner) view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","On my way","Shipped");

        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                try {
                    method = "OrderStaus";
                    DatabaseInsert DBinsert = new DatabaseInsert(OrderStatus.this);
                    String result = DBinsert.execute(method,String.valueOf(spinner.getSelectedIndex()),key).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                adapter_new.notifyDataSetChanged();

                //sendOrderStatusToUser(localKey, item);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();

    }

    private void sendOrderStatusToUser(final String key , final Request item) {
        DatabaseReference tokens = database.getReference("Tokens");
        tokens.orderByKey().equalTo(item.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                        {
                            Token token = postSnapShot.getValue(Token.class);

                            //Make raw payload
                            Notification notification = new Notification("Eat It Server", "Your order "+key+" was updated");
                            Sender content = new Sender(token.getToken(),notification);

                            mService.sendNotification(content)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if(response.body().success == 1) {
                                                Toast.makeText(OrderStatus.this, "Order was updated", Toast.LENGTH_SHORT).show();
                                                //finish();
                                            }
                                            else {
                                                Toast.makeText(OrderStatus.this, "Order was updated but failed to send notification !!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR", t.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


}