package website.programming.androideatitserver.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.FoodList;
import website.programming.androideatitserver.Interface.ItemClickListener;
import website.programming.androideatitserver.Model.Category;
import website.programming.androideatitserver.Model.Food;
import website.programming.androideatitserver.R;

/**
 * Created by cokel on 3/31/2018.
 */


class FoodViewHolder_new extends RecyclerView.ViewHolder implements
        View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView food_name;
    public ImageView food_image;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder_new(View itemView) {
        super(itemView);


        food_name = (TextView)itemView.findViewById(R.id.food_name);
        food_image = (ImageView) itemView.findViewById(R.id.food_image);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the Action");

        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}

public class FoodAdapter_new extends RecyclerView.Adapter<FoodViewHolder_new>  {

    Context context;
    ArrayList<Category> listdata = new ArrayList<Category>();

    public FoodAdapter_new(ArrayList<Category> data, Context context) {
        this.listdata = data;
        this.context = context;
    }

    @Override
    public FoodViewHolder_new onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.food_item, parent, false);
        return  new FoodViewHolder_new(itemView);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder_new holder, int position) {
        holder.food_name.setText(listdata.get(position).getName());

        Picasso.with(context).load(listdata.get(position).getImage())
                .into(holder.food_image);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

            }
        });

    }

    public  ArrayList<Category> getListdata(){
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