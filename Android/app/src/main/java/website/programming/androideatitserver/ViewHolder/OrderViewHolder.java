package website.programming.androideatitserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Interface.ItemClickListener;
import website.programming.androideatitserver.R;

/**
 * Created by cokel on 3/3/2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOrderId,txtOrderStatus, txtOrderPhone, txtOrderAddress;

    public Button btnEdit,btnRemove ,btnDetail ,btnDirection;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);

        btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
        btnRemove = (Button)itemView.findViewById(R.id.btnRemove);
        btnDetail = (Button)itemView.findViewById(R.id.btnDetail);
        btnDirection = (Button)itemView.findViewById(R.id.btnDirection);


    }

}
