package com.example.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreapp.provider.BookItem;
import com.example.bookstoreapp.provider.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.StringTokenizer;
import androidx.lifecycle.ViewModelProvider;


public class MainActivity extends AppCompatActivity{
    EditText inputBookID,inputBookTitle,inputBookISBN,inputBookAuthor,inputBookDesc,inputBookPrice;
    String bookID,bookTitle,bookISBN,bookAuthor,bookDesc,bookPrice;
//    ArrayList<BookItem> myList = new ArrayList<>();
    DrawerLayout drawer;
//    RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
//    MyRecyclerViewAdapter adapter;
    private BookViewModel mBookViewModel;
    DatabaseReference myRef;
    int x_down;
    int y_down;

    GestureDetector gestureDetector;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetector = new GestureDetector(this, new MyGestureDetector());

        // initializing firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Books");
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("Books");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Toast.makeText(getApplicationContext(), "Book added to firebase", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Toast.makeText(getApplicationContext(), "Books removed firebase", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setContentView(R.layout.drawer);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, // requesting to access sms
                android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

        addListner();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // setting toolbar as action bar

        drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer ,toolbar ,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

//        recyclerView = findViewById(R.id.recyclerView);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        adapter = new MyRecyclerViewAdapter();
//        adapter.setData(myList);
//        recyclerView.setAdapter(adapter);

        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        mBookViewModel.getAllItems().observe(this, newData -> {
//            adapter.setCustomers(newData);
//            adapter.notifyDataSetChanged();
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frame1,new RecyclerViewFragment()).commit();


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            String bookAdd = String.valueOf(inputBookTitle.getText());
            double priceAdd = Double.parseDouble(inputBookPrice.getText().toString());
            Toast.makeText(this, "Book (" + bookAdd + ") and the price (" + priceAdd + ")", Toast.LENGTH_LONG).show();

            // saving input values
            SharedPreferences sharedPreferences = getSharedPreferences("loading", 0); // 0 means private mode
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String BookId = inputBookID.getText().toString();
            String BookTitle = inputBookTitle.getText().toString();
            String BookISBN = inputBookISBN.getText().toString();
            String BookAuthor = inputBookAuthor.getText().toString();
            String BookDesc = inputBookDesc.getText().toString();
            double BookPrice = Double.parseDouble(inputBookPrice.getText().toString());

            editor.putString("inputBookID", String.valueOf(BookId));
            editor.putString("inputBookTitle", BookTitle);
            editor.putString("inputBookISBN", BookISBN);
            editor.putString("inputBookAuthor", BookAuthor);
            editor.putString("inputBookDesc", BookDesc);
            editor.putString("inputBookPrice", String.valueOf(BookPrice));
            editor.apply();

            BookItem bookItem = new BookItem(BookId,BookTitle,BookISBN,BookAuthor,BookDesc,BookPrice);
//            myList.add(bookItem);
//            adapter.notifyDataSetChanged();
            myRef.push().setValue(bookItem);
            mBookViewModel.insert(bookItem);
        });

        View frameGest =findViewById(R.id.frameLayoutGesture);
        frameGest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
                }
        });
    }
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            inputBookISBN.setText(RandomString.generateNewRandomString(13));
            return true;
        }
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            inputBookID.setText("");
            inputBookTitle.setText("");
            inputBookISBN.setText("");
            inputBookAuthor.setText("");
            inputBookDesc.setText("");
            inputBookPrice.setText("");
            return true;
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            double currentPrice = Double.parseDouble(inputBookPrice.getText().toString());
            double newPrice = currentPrice;
            if (e2.getX() > e1.getX()) {
                newPrice = currentPrice + Math.abs(distanceX);
            } else {
                newPrice = currentPrice - Math.abs(distanceX);
                if (newPrice < 0) newPrice = 0;
            }
            inputBookPrice.setText(String.format("%.2f",newPrice));

            float minSwipeDistanceY = 10.0f;

            if (Math.abs(distanceY) > minSwipeDistanceY) {
                if (e2.getY() > e1.getY()) {
                    inputBookTitle.setText("Untitled");
                } else if (e2.getY() < e1.getY()) {
                    inputBookTitle.setText("Untitled");
                }
            }
            return true;
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if(velocityX>1000 || velocityY>1000){
            moveTaskToBack(true);
            }
            return true;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            SharedPreferences sharedPreferences = getSharedPreferences("loading", 0); // 0 means private mode
            EditText editTextBookID = findViewById(R.id.BookID);
            EditText editTextBookTitle = findViewById(R.id.BookTitle);
            EditText editTextBookISBN = findViewById(R.id.BookISBN);
            EditText editTextBookAuthor = findViewById(R.id.BookAuthor);
            EditText editTextBookDesc = findViewById(R.id.BookDescription);
            EditText editTextBookPrice = findViewById(R.id.BookPrice);

            String inputBookID = sharedPreferences.getString("inputBookID", "");
            String inputBookTitle = sharedPreferences.getString("inputBookTitle", "");
            String inputBookISBN = sharedPreferences.getString("inputBookISBN", "");
            String inputBookAuthor = sharedPreferences.getString("inputBookAuthor", "");
            String inputBookDesc = sharedPreferences.getString("inputBookDesc", "");
            String inputBookPrice = sharedPreferences.getString("inputBookPrice", "");

            editTextBookID.setText(inputBookID);
            editTextBookTitle.setText(inputBookTitle);
            editTextBookISBN.setText(inputBookISBN);
            editTextBookAuthor.setText(inputBookAuthor);
            editTextBookDesc.setText(inputBookDesc);
            editTextBookPrice.setText(inputBookPrice);
        }
    }



    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if(id == R.id.addBook){
                String bookAdd = String.valueOf(inputBookTitle.getText());
                double priceAdd = Double.parseDouble(inputBookPrice.getText().toString());
                Toast.makeText(getApplicationContext(), "Book (" + bookAdd + ") and the price (" + priceAdd + ")", Toast.LENGTH_LONG).show();

                // saving input values
                SharedPreferences sharedPreferences = getSharedPreferences("loading", 0); // 0 means private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String BookId = inputBookID.getText().toString();
                String BookTitle = inputBookTitle.getText().toString();
                String BookISBN = inputBookISBN.getText().toString();
                String BookAuthor = inputBookAuthor.getText().toString();
                String BookDesc = inputBookDesc.getText().toString();
                double BookPrice = Double.parseDouble(inputBookPrice.getText().toString());

                editor.putString("inputBookID", String.valueOf(BookId));
                editor.putString("inputBookTitle", BookTitle);
                editor.putString("inputBookISBN", BookISBN);
                editor.putString("inputBookAuthor", BookAuthor);
                editor.putString("inputBookDesc", BookDesc);
                editor.putString("inputBookPrice", String.valueOf(BookPrice));
                editor.apply();

                BookItem bookItem = new BookItem(BookId,BookTitle,BookISBN,BookAuthor,BookDesc,BookPrice);
//                myList.add(bookItem);
//                adapter.notifyDataSetChanged();
                mBookViewModel.insert(bookItem);
                myRef.push().setValue(bookItem);

            }else if (id == R.id.RemoveLast){
//                myList.remove(myList.size()-1);
//                adapter.notifyDataSetChanged();
                mBookViewModel.deleteLast();
            }
            else if (id == R.id.RemoveAll){
//                myList.clear();
//                adapter.notifyDataSetChanged();
                mBookViewModel.deleteAll();
                myRef.removeValue();
            }
            else if(id==R.id.listAll){
                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(i);
            }
            else if (id==R.id.closeApp){
                finish();
            } else if (id == R.id.signoutDrawer) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }

            // close drawer
            drawer.closeDrawers();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.clearFields){
            inputBookID.setText("");
            inputBookTitle.setText("");
            inputBookISBN.setText("");
            inputBookAuthor.setText("");
            inputBookDesc.setText("");
            inputBookPrice.setText("");

        }else if (id == R.id.loadData){
            SharedPreferences sharedPreferences = getSharedPreferences("loading", 0); // 0 means private mode
            EditText editTextBookID = findViewById(R.id.BookID);
            EditText editTextBookTitle = findViewById(R.id.BookTitle);
            EditText editTextBookISBN = findViewById(R.id.BookISBN);
            EditText editTextBookAuthor = findViewById(R.id.BookAuthor);
            EditText editTextBookDesc = findViewById(R.id.BookDescription);
            EditText editTextBookPrice = findViewById(R.id.BookPrice);

            String inputBookID = sharedPreferences.getString("inputBookID", "");
            String inputBookTitle = sharedPreferences.getString("inputBookTitle", "");
            String inputBookISBN = sharedPreferences.getString("inputBookISBN", "");
            String inputBookAuthor = sharedPreferences.getString("inputBookAuthor", "");
            String inputBookDesc = sharedPreferences.getString("inputBookDesc", "");
            String inputBookPrice = sharedPreferences.getString("inputBookPrice", "");

            editTextBookID.setText(inputBookID);
            editTextBookTitle.setText(inputBookTitle);
            editTextBookISBN.setText(inputBookISBN);
            editTextBookAuthor.setText(inputBookAuthor);
            editTextBookDesc.setText(inputBookDesc);
            editTextBookPrice.setText(inputBookPrice);
        } else if (id == R.id.totalBooks) {
//            int totalBooks = myList.size();
            LiveData<Integer> totalBooks = mBookViewModel.totalCount();
            Toast.makeText(getApplicationContext(), "Total books: " + totalBooks, Toast.LENGTH_LONG).show();
        } else if (id == R.id.deletePrice){
            mBookViewModel.deletePrice();
        }
        return super.onOptionsItemSelected(item);
    }


    class MyBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

            // parsing the incoming messages, and seperate them by "|"
            StringTokenizer recievedMSG = new StringTokenizer(msg, "|");
            String bookId = recievedMSG.nextToken();
            String bookTitle = recievedMSG.nextToken();
            String bookISBN = recievedMSG.nextToken();
            String bookAuthor = recievedMSG.nextToken();
            String bookDesc = recievedMSG.nextToken();
            Double bookPrice  = Double.valueOf(recievedMSG.nextToken());
            Boolean checkDouble = Boolean.valueOf(recievedMSG.nextToken());
            if(checkDouble==true){
                bookPrice +=100;
            } else {
                bookPrice +=5;
            }

            // setting the received test to its right location
            inputBookID.setText(bookId);
            inputBookTitle.setText(bookTitle);
            inputBookISBN.setText(bookISBN);
            inputBookAuthor.setText(bookAuthor);
            inputBookDesc.setText(bookDesc);
            inputBookPrice.setText(String.valueOf(bookPrice));
        }
    }

    public void addListner() {
        inputBookID = findViewById(R.id.BookID);
        inputBookTitle = findViewById(R.id.BookTitle);
        inputBookISBN = findViewById(R.id.BookISBN);
        inputBookAuthor = findViewById(R.id.BookAuthor);
        inputBookDesc = findViewById(R.id.BookDescription);
        inputBookPrice = findViewById(R.id.BookPrice);
    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences myID = getSharedPreferences("f1",0);// 0 means private mode
        SharedPreferences myTittle = getSharedPreferences("f2",0);
        SharedPreferences myISBN = getSharedPreferences("f3",0);
        SharedPreferences myAuthor = getSharedPreferences("f4",0);
        SharedPreferences myDesc = getSharedPreferences("f5",0);
        SharedPreferences myPrice = getSharedPreferences("f6",0);

        bookID = myID.getString("id","");
        bookTitle = myTittle.getString("tittle","");
        bookISBN = myISBN.getString("isbn","");
        bookAuthor = myAuthor.getString("author","");
        bookDesc = myDesc.getString("description","");
        bookPrice = myPrice.getString("price","");

        inputBookID.setText(bookID);
        inputBookTitle.setText(bookTitle);
        inputBookISBN.setText(bookISBN);
        inputBookAuthor.setText(bookAuthor);
        inputBookDesc.setText(bookDesc);
        inputBookPrice.setText(bookPrice);

    }

    @Override
    protected void onStop() {
        super.onStop();
        bookID = inputBookID.getText().toString();
        bookTitle = inputBookTitle.getText().toString();
        bookISBN = inputBookISBN.getText().toString();
        bookAuthor = inputBookAuthor.getText().toString();;
        bookDesc = inputBookDesc.getText().toString();;
        bookPrice = inputBookPrice.getText().toString();;

        SharedPreferences myID = getSharedPreferences("f1",0); // 0 means private mode
        SharedPreferences myTittle = getSharedPreferences("f2",0);
        SharedPreferences myISBN = getSharedPreferences("f3",0);
        SharedPreferences myAuthor = getSharedPreferences("f4",0);
        SharedPreferences myDesc = getSharedPreferences("f5",0);
        SharedPreferences myPrice = getSharedPreferences("f6",0);

        SharedPreferences.Editor idEditor = myID.edit();
        SharedPreferences.Editor tittleEditor = myTittle.edit();
        SharedPreferences.Editor isbnEditor = myISBN.edit();
        SharedPreferences.Editor authorEditor = myAuthor.edit();
        SharedPreferences.Editor descEditor = myDesc.edit();
        SharedPreferences.Editor priceEditor = myPrice.edit();

        idEditor.putString("id", bookID);
        tittleEditor.putString("tittle", bookTitle);
        isbnEditor.putString("isbn", bookISBN);
        authorEditor.putString("author", bookAuthor);
        descEditor.putString("description", bookDesc);
        priceEditor.putString("price", bookPrice);

        idEditor.commit();
        tittleEditor.commit();
        isbnEditor.commit();
        authorEditor.commit();
        descEditor.commit();
        priceEditor.commit();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
        EditText bookTitleSave = findViewById(R.id.BookTitle);
        EditText bookISBNSave = findViewById(R.id.BookISBN);

        String bookTittle = bookTitleSave.getText().toString();
        String bookISBN = bookISBNSave.getText().toString();

        outState.putString("key1", bookTittle);
        outState.putString("key2", bookISBN);


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
        String bookTittleSave = savedInstanceState.getString("key1");
        String bookISBNSave = savedInstanceState.getString("key2");

        inputBookID.setText("");
        inputBookAuthor.setText("");
        inputBookDesc.setText("");
        inputBookPrice.setText("");
    }
}
