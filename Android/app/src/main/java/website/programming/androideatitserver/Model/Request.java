package website.programming.androideatitserver.Model;


import android.content.Context;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import website.programming.androideatitserver.Database.DatabaseInsert;
import website.programming.androideatitserver.FoodList;

/**
 * Created by cokel on 3/2/2018.
 */

public class Request {
    private String id;
    private String phone;
    private String name;
    private String address;
    private String total;
    private String status;
    private String Comment;
    private String paymentState;
    private List<Order> foods;
    private Context context;

    public Request() {
    }

    public Request(Context context,String id, String phone, String name, String address, String total, String status, String comment, String paymentState) {
        this.context = context;
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        Comment = comment;
        this.paymentState = paymentState;
        //this.foods = foods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

 /*   public void DelOrder(String id) {
        try {
            String method="DeleteOrder";
            DatabaseInsert DBinsert = new DatabaseInsert(context);
            String result = DBinsert.execute(method, id).get();

            //finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }*/

}
