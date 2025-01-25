package website.programming.androideatitserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Database.DatabaseInsert;
import website.programming.androideatitserver.Database.ParseJsonCategoryData;
import website.programming.androideatitserver.Database.PostServices;
import website.programming.androideatitserver.Interface.ItemClickListener;
import website.programming.androideatitserver.Model.Category;
import website.programming.androideatitserver.Model.Food;
import website.programming.androideatitserver.Model.Token;
import website.programming.androideatitserver.Model.User;
import website.programming.androideatitserver.ViewHolder.FoodAdapter_new;
import website.programming.androideatitserver.ViewHolder.MenuAdapter_new;
import website.programming.androideatitserver.ViewHolder.MenuViewHolder;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    RecyclerView recyler_menu;
    RecyclerView.LayoutManager layoutManager;

    TextView txtFullName;
    EditText edtName;
    Button btnSelect, btnUpLoad;

    Category newCategory;
    String CategoryId = "";
    Integer intCategoryId;

    private final int PICK_IMAGE_REQUEST = 71;

    DrawerLayout drawer;

    Uri saveUri;
    Bitmap imagebitmap;
    MenuAdapter_new adapter_new;
    String parse_json = null, method;
    ArrayList<Category> CategoryArrayLists;
    String BufferNoData = "Nodata";
    ArrayList<Category> EmptyCategoryArrayLists = null;
    String signedUploadUrl = null;
    String imageId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Menu For Server");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Name for User
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

        //Load Menu
        recyler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recyler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyler_menu.setLayoutManager(layoutManager);
        loadMenu();
        //createId();

//        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

//    private void updateToken(String token) {
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference tokens = db.getReference("Tokens");
//        Token data = new Token(token,true);//false because this token send from client app
//        tokens.child(Common.currentUser.getPhone()).setValue(data);
//    }

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

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Add new Category");
        alertDialog.setMessage("Please Fill in the Details");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpLoad = add_menu_layout.findViewById(R.id.btnUpLoad);


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

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String itemId = createId();

                try {
                    method="Category";
                    String type="Create";
                    PostServices ps = new PostServices(Home.this);
                    Category newCategory = new Category(itemId);
                    newCategory.setImage(imageId);
                    newCategory.setMenuid("00");
                    newCategory.setName(edtName.getText().toString());
                    String result = ps.execute(method, Common.currentUser,newCategory, type).get();
                    loadMenu();
                    //finish();
                    Toast.makeText(Home.this, "New category " + newCategory.getName() + " was added.", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();

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
        //adapter_new.notifyDataSetChanged();

        alertDialog.show();

    }

    private String createId() {

        int maxId = 0;

        for(Category category:CategoryArrayLists)
        {
            CategoryId = category.getCategoryid().toString();

            if (CategoryId.substring(0,1).matches("0"))
                intCategoryId = Integer.parseInt(CategoryId.substring(1));
            else
                intCategoryId = Integer.parseInt(CategoryId);

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
                    Toast.makeText(Home.this, "Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                    Log.i("Upload Result", "Successfully Uploaded!");
                }
                else {
                    Toast.makeText(Home.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Home.this, "Please Select the Image First.", Toast.LENGTH_SHORT).show();
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

    private void loadMenu() {

       // if (databaseSelectServices == null) {
            try {
                method = "DisplayCategory";
                String menuid = "00";
                PostServices ps = new PostServices(Home.this);
                parse_json = ps.execute(method, Common.currentUser, menuid).get();
//                parse_json = databaseSelectServices.parse_json;
                Log.i("parse_json", parse_json);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(parse_json.matches(BufferNoData)){
                adapter_new = new MenuAdapter_new(EmptyCategoryArrayLists,this);
                adapter_new.notifyDataSetChanged();
                recyler_menu.invalidate();
                recyler_menu.setAdapter(adapter_new);
                Toast.makeText(Home.this, "No Category!", Toast.LENGTH_SHORT).show();
            }
            else {
                if (!TextUtils.isEmpty(parse_json)) {
                    try {
                        ParseJsonCategoryData parseJsonCategoryData = new ParseJsonCategoryData(parse_json);
                        CategoryArrayLists = parseJsonCategoryData.getCategoryData();
                        if (adapter_new != null) {
                            while (adapter_new.getItemCount() > 0) {
                                adapter_new.getListdata().remove(adapter_new.getItemCount() - 1);
                            }
//                            for (Category category : CategoryArrayLists) {
//                                adapter_new.getListdata().add(category);
//                            }
                        }
                        adapter_new = new MenuAdapter_new(CategoryArrayLists, this);
                        adapter_new.notifyDataSetChanged();
                        recyler_menu.setAdapter(adapter_new);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(Home.this, "Load Category Failure!", Toast.LENGTH_SHORT).show();
                }

            }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {
            //Intent cartIntent = new Intent(Home.this, Cart.class);
            //startActivity(cartIntent);

        } else if (id == R.id.nav_orders) {
            Intent OrderIntent = new Intent(Home.this, OrderStatus.class);
            startActivity(OrderIntent);
        } else if (id == R.id.nav_log_out) {
            Intent signIn = new Intent(Home.this,MainActivity.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDialog(adapter_new.getListdata().get(item.getOrder()));
            //loadMenu();
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteCategory(adapter_new.getListdata().get(item.getOrder()).getCategoryid());
            loadMenu();
        }

        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        //First , we need get all food in category

        try {
            method="DeleteCategory";
            PostServices ps = new PostServices(Home.this);
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Update new Category");
        alertDialog.setMessage("Please Fill in the Details");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpLoad = add_menu_layout.findViewById(R.id.btnUpLoad);

        edtName.setText(item.getName());

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

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                item.setName(edtName.getText().toString());
                item.setImage(imageId);
                item.setMenuid("00");

                try {
                    method="Category";
                    String type = "Update";
                    PostServices ps = new PostServices(Home.this);
                    String result = ps.execute(method, Common.currentUser,item,type).get();
                    loadMenu();
                    //finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

//                try {
//                    method="CategoryUpdate";
//                    DatabaseInsert DBinsert = new DatabaseInsert(Home.this);
//                    String result = DBinsert.execute(method,item.getCategoryid(),item.getName(),item.getImage()).get();
//                    Toast.makeText(Home.this, result, Toast.LENGTH_SHORT).show();
//                    loadMenu();
//                    //finish();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
                //categories.child(key).setValue(item);

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

//    private void ChangeImage(final Category item) {
//
//        if(saveUri != null)
//        {
//            final ProgressDialog mDialog = new ProgressDialog(this);
//            mDialog.setMessage("Uploading.....");
//            mDialog.show();
//
//            //String imageName=String.valueOf(System.currentTimeMillis());
//            String PHP_URL = Common.SERVER_URL + "SavePicture.php";
//            String IMAGE_URL = Common.SERVER_URL + "pictures/" + imageId + ".JPG";
//
//            Log.i("Image Url", IMAGE_URL);
//
//            try {
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//                byte[] ImageByteArray = baos.toByteArray();
//                String encodedImage = android.util.Base64.encodeToString(ImageByteArray, android.util.Base64.DEFAULT);
//                method="Category";
//                String result = new UploadImage().execute(method,imageId,PHP_URL,encodedImage).get();
//                mDialog.dismiss();
//                Toast.makeText(Home.this, result, Toast.LENGTH_SHORT).show();
//                Log.i("Upload Result", result);
//                item.setImage(IMAGE_URL);
//                baos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
