package com.example.electronics_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.electronics_store.Admin.AdminMaintainProductsActivity;
import com.example.electronics_store.Model.Products;
import com.example.electronics_store.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private FloatingActionButton floatingCartButton;

    private DatabaseReference productReference;
    private RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Only if you're coming from Admin will this run
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().get("Admin").toString();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //Toolbar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                // To display  icon in toolbar
                toggle.syncState();
            }
        });
        //Makes menu clickable
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_view);

        productReference = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerMenu = findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);

        floatingCartButton = findViewById(R.id.cart_button);
        floatingCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add to cart
                Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productReference, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        //Display data on the recyclerview
                        holder.productTitle.setText(model.getTitle());
                        holder.productManufacturer.setText(model.getManufacturer());
                        holder.productPrice.setText("Price: €" + model.getPrice());

                        Picasso.get().load(model.getImage()).into(holder.productImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (type.equals("Admin")) {
                                    //If it's admin send to maintain products activity
                                    Intent intent = new Intent(MainActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("productID", model.getProductID());
                                    startActivity(intent);
                                } else {
                                    //If it's user send to product details activity
                                    Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("productID", model.getProductID());
                                    //intent.putExtra("orderID", orderID);
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerMenu.setAdapter(adapter);
        adapter.startListening();
    }


    //To avoid closing the application on back press
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent menuIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(menuIntent);

        } else if (id == R.id.nav_search) {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(MainActivity.this, SearchProductsActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_cart) {
            if (!type.equals("Admin")) {
                Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }

        } else if (id == R.id.nav_orders) {
            if (!type.equals("Admin")) {
                Intent orderIntent = new Intent(MainActivity.this, OrderHistoryActivity.class);
                startActivity(orderIntent);
            }
        } else if (id == R.id.nav_logout) {
            Intent mainActivity = new Intent(MainActivity.this, LoginActivity.class);
            mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivity);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}