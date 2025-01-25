package website.programming.androideatitserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Database.DatabaseInsert;
import website.programming.androideatitserver.Database.DatabaseSelectServices;
import website.programming.androideatitserver.Database.ParseJsonCategoryData;
import website.programming.androideatitserver.Database.ParseJsonFoodData;
import website.programming.androideatitserver.Database.PostServices;
import website.programming.androideatitserver.Interface.ItemClickListener;
import website.programming.androideatitserver.Model.Category;
import website.programming.androideatitserver.Model.Food;
import website.programming.androideatitserver.ViewHolder.FoodViewHolder;
import website.programming.androideatitserver.ViewHolder.FoodAdapter_new;
import website.programming.androideatitserver.ViewHolder.MenuAdapter_new;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    String categoryId="";

    String strFoodId = "";
    Integer intFoodId;

    Category newFood;

    TextView txtFullName;
    EditText edtName, edtDescription, edtPrice, edtDiscount;
    Button btnSelect, btnUpLoad;

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    DrawerLayout drawer;

    private final int PICK_IMAGE_REQUEST = 71;

    Uri saveUri;
    Bitmap imagebitmap;
    FoodAdapter_new adapter_new;
    String parse_json = null, method;
    ArrayList<Category> jsondata;
    ArrayList<Category> Emptyjsondata = null;
    String BufferNoData = "Nodata";
    ArrayList<Category> CategoryArrayLists = null;

    Category newCategory;
    String CategoryId = "";
    Integer intCategoryId;
    String imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Code Late
                showAddFoodDialog();
            }
        });

        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty())
            loadListFood(categoryId);
        //createId();
    }



    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Add new Food");
        alertDialog.setMessage("Please Fill in the Details");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_food_layout = inflater.inflate(R.layout.add_new_food_layout,null);

        edtName = add_food_layout.findViewById(R.id.edtName);
        edtDescription = add_food_layout.findViewById(R.id.edtDescription);
        edtPrice = add_food_layout.findViewById(R.id.edtPrice);
        edtDiscount = add_food_layout.findViewById(R.id.edtDiscount);
        btnSelect = add_food_layout.findViewById(R.id.btnSelect);
        btnUpLoad = add_food_layout.findViewById(R.id.btnUpLoad);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadImage();
            }
        });

        alertDialog.setView(add_food_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                String itemId = createId();

                try {
                    method="Category";
                    String type="Create";
                    PostServices ps = new PostServices(FoodList.this);
                    Category newCategory = new Category(itemId);
                    newCategory.setImage(imageId);
                    newCategory.setMenuid(categoryId);
                    newCategory.setName(edtName.getText().toString());
                    newCategory.setDescription(edtDescription.getText().toString());
                    newCategory.setPrice(Integer.parseInt(edtPrice.getText().toString()));
                    newCategory.setDiscount(Integer.parseInt(edtDiscount.getText().toString()));
                    String result = ps.execute(method, Common.currentUser,newCategory, type).get();
                    loadListFood(categoryId);
                    //finish();
                    Toast.makeText(FoodList.this, "New Food " + newCategory.getName() + " was added.",Toast.LENGTH_LONG)
                            .show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private String createId() {

        int maxId = 0;
        ArrayList<Category> MaxCategoryArrayLists = null;

        try {
            method = "GetMaximumID";
            PostServices ps = new PostServices(FoodList.this);
            parse_json = ps.execute(method, Common.currentUser).get();
//                parse_json = databaseSelectServices.parse_json;
            Log.i("parse_json", parse_json);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(parse_json)) {
            ParseJsonCategoryData parseJsonCategoryData= new ParseJsonCategoryData(parse_json);
            MaxCategoryArrayLists = parseJsonCategoryData.getCategoryData();
        }

        for(Category category:MaxCategoryArrayLists)
        {
            String newCategoryId = category.getCategoryid().toString();

            if (newCategoryId.substring(0,1).matches("0"))
                intCategoryId = Integer.parseInt(newCategoryId.substring(1));
            else
                intCategoryId = Integer.parseInt(newCategoryId);

            if (intCategoryId > maxId) {
                maxId = intCategoryId;
            }

            // Log.i("CategoryId Lists = " , CategoryId);
        }
        intCategoryId = maxId + 1;

        Log.i("Latest CategoryId = " , String.valueOf(intCategoryId));

        if (intCategoryId < 10) {
            return "0" + intCategoryId;
        }

        return intCategoryId.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            saveUri = data.getData();

            try {

                imagebitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), saveUri);

                btnSelect.setText("Image Selected !");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void upLoadImage() {
        if(saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading.....");
            mDialog.show();

            Boolean uploadStatus = false;
            //String imageName=String.valueOf(System.currentTimeMillis());
//            String PHP_URL = Common.SERVER_URL + "SavePicture.php";
//            String IMAGE_URL = Common.SERVER_URL + "pictures/" + imageName + ".JPG";

//            Log.i("PHP URL:" , PHP_URL);
//            Log.i("Image URL:" , IMAGE_URL);

            try {
                int flags = Base64.NO_WRAP | Base64.URL_SAFE;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] ImageByteArray = baos.toByteArray();
                String encodedImage = android.util.Base64.encodeToString(ImageByteArray, flags);
                String jsonString = new UploadImage().execute(encodedImage, imageId, Common.currentUser).get();
                mDialog.dismiss();

                try {
                    JSONObject jsonObj = new JSONObject(jsonString);
                    uploadStatus = jsonObj.getBoolean("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (uploadStatus) {
                    Toast.makeText(FoodList.this, "Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(FoodList.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
                }
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(FoodList.this, "Please Select the Image First.", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseImage() {
        //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(intent, 1);
        imageId = UUID.randomUUID().toString();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    private void loadListFood(String categoryId) {
        Log.i("Load List Food 1","Entered");
      //  if (databaseSelectServices == null) {
            try {
                method = "DisplayCategory";
                PostServices ps = new PostServices(FoodList.this);
                parse_json = ps.execute(method, Common.currentUser, categoryId).get();
                //parse_json = databaseSelectServices.parse_json;
                Log.i("parse_json", parse_json);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(parse_json.matches(BufferNoData)){
                adapter_new = new FoodAdapter_new(Emptyjsondata,this);
                adapter_new.notifyDataSetChanged();
                recyclerView.invalidate();
                recyclerView.setAdapter(adapter_new);
                Toast.makeText(FoodList.this, "No Food For Under this Category!", Toast.LENGTH_SHORT).show();
            }
            else {
                ParseJsonCategoryData parseJsonCategoryData= new ParseJsonCategoryData(parse_json);
                CategoryArrayLists = parseJsonCategoryData.getCategoryData();

                if (CategoryArrayLists.size() > 0) {
                    adapter_new = new FoodAdapter_new(CategoryArrayLists,this);
                    adapter_new.notifyDataSetChanged();
                    recyclerView.invalidate();
                    recyclerView.setAdapter(adapter_new);
                } else {
                    Toast.makeText(FoodList.this, "Load Food Failure!", Toast.LENGTH_SHORT).show();
                }

            }
        //}
       // adapter_new.notifyDataSetChanged();
       // recyclerView.setAdapter(adapter_new);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE))
        {
            //showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
            showUpdateDialog(adapter_new.getListdata().get(item.getOrder()));

        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteFood(adapter_new.getListdata().get(item.getOrder()).getCategoryid());
            loadListFood(categoryId);

        }

        return super.onContextItemSelected(item);
    }


    private void deleteFood(String key) {
        //First , we need get all food in category

        try {
            method="DeleteCategory";
            PostServices ps = new PostServices(FoodList.this);
            String result = ps.execute(method, Common.currentUser, key).get();
            //finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"Item deleted!!!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final Category item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Update new Food");
        alertDialog.setMessage("Please Fill in the Details");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_food_layout = inflater.inflate(R.layout.add_new_food_layout,null);

        edtName = add_food_layout.findViewById(R.id.edtName);
        edtDescription = add_food_layout.findViewById(R.id.edtDescription);
        edtPrice = add_food_layout.findViewById(R.id.edtPrice);
        edtDiscount = add_food_layout.findViewById(R.id.edtDiscount);
        btnSelect = add_food_layout.findViewById(R.id.btnSelect);
        btnUpLoad = add_food_layout.findViewById(R.id.btnUpLoad);

        edtName.setText(item.getName());
        edtDescription.setText(item.getDescription());
        edtPrice.setText(String.valueOf(item.getPrice()));
        edtDiscount.setText(String.valueOf(item.getDiscount()));

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadImage();
            }
        });


        alertDialog.setView(add_food_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                try {
                    method="Category";
                    String type="Update";
                    PostServices ps = new PostServices(FoodList.this);
                    Category newCategory = new Category(item.getCategoryid());
                    newCategory.setImage(imageId);
                    newCategory.setMenuid(categoryId);
                    newCategory.setName(edtName.getText().toString());
                    newCategory.setDescription(edtDescription.getText().toString());
                    newCategory.setPrice(Integer.parseInt(edtPrice.getText().toString()));
                    newCategory.setDiscount(Integer.parseInt(edtDiscount.getText().toString()));
                    String result = ps.execute(method, Common.currentUser,newCategory, type).get();
                    loadListFood(categoryId);
                    //finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

}