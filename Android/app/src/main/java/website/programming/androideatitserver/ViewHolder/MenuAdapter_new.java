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
import website.programming.androideatitserver.R;

/**
 * Created by cokel on 3/30/2018.
 */

class MenuViewHolder_new extends RecyclerView.ViewHolder implements
        View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView txtMenuName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public MenuViewHolder_new(View itemView) {
        super(itemView);

        txtMenuName = (TextView)itemView.findViewById(R.id.menu_name);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
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

public class MenuAdapter_new extends RecyclerView.Adapter<MenuViewHolder_new>  {

    Context context;
    ArrayList<Category> listdata = new ArrayList<Category>();

    public MenuAdapter_new(ArrayList<Category> data, Context context) {
        this.listdata = data;
        this.context = context;
    }

    @Override
    public MenuViewHolder_new onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.menu_item, parent, false);
        return  new MenuViewHolder_new(itemView);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder_new holder, int position) {
        holder.txtMenuName.setText(listdata.get(position).getName());

        Picasso.with(context).load(listdata.get(position).getImage())
                .into(holder.imageView);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent foodlist = new Intent(context, FoodList.class);
                foodlist.putExtra("CategoryId", listdata.get(position).getCategoryid());
                context.startActivity(foodlist);
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