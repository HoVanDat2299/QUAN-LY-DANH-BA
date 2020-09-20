package com.example.doanmon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

import ClassDoiTuon.Database;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    public static Database database;
    TextView edtName,edtEmail,edtUser,searchView;
    boolean status = false;
    MenuItem menuItem;
//    ArrayList<DanhBa>arrayList;
    final int REQUEST_READ_CONTACT=123;
    int id_user=-1;

    ImageView imfg;
    TextView name;
    TextView number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        AnhXa();
        CreateDatabase();
        loadFragment(new DanhbaFragment());
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_Lichsu);

    }
    private void AnhXa() {
        navView = findViewById(R.id.nav_view);
        searchView = findViewById(R.id.search_vew);

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch(item.getItemId()){
                case R.id.navigation_Lichsu:
                    //   getSupportActionBar().setTitle("Home");
                    fragment = new HistoryFragment();
                    loadFragment(fragment);
                    overridePendingTransition(R.anim.anim_enter,R.anim.anim_enter);
                    //Toast.makeText(MainActivity.this, "Đây là home", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_danhBa:
                    //   getSupportActionBar().setTitle("Home");
                    fragment = new DanhbaFragment();
                    loadFragment(fragment);
                    overridePendingTransition(R.anim.anim_enter,R.anim.anim_enter);
                    //Toast.makeText(MainActivity.this, "Đây là home", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_yeuthich:
                    //   getSupportActionBar().setTitle("Home");
                    fragment = new LikeFragment();
                    loadFragment(fragment);
                    overridePendingTransition(R.anim.anim_enter,R.anim.anim_enter);
                    //Toast.makeText(MainActivity.this, "Đây là home", Toast.LENGTH_SHORT).show();
                    return true;


            }
            return false;
        }
    };
    public void CreateDatabase()
    {
        // Tạo database
        database=new Database(this,"danhba",null,1);
        //Tạo bảng
        database.QueryData("CREATE TABLE IF NOT EXISTS DanhBa(Id INTEGER PRIMARY KEY AUTOINCREMENT, Ten VARCHAR(10), SoDienThoai TEXT,HinhAnh BLOB, NgaySinh DATE, Email VARCHAR(100), DiaChi VARCHAR(200) ,YeuThich BOOLEAN)");
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_READ_CONTACT:
            {
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.user);
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 5, byteArrayOutputStream);
                    byte[] hinh = byteArrayOutputStream.toByteArray();
                    ContentResolver contentResolver=getContentResolver();
                    Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                    while (cursor.moveToNext())
                    {
                        String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        int hasPhoneNumber=Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                        String sodienthoai="";
                        if(hasPhoneNumber==1)
                        {
                            Cursor Phone=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                            Phone.moveToFirst();
                            sodienthoai=Phone.getString(Phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        Cursor cursorCheck= database.GetData("SELECT * FROM DanhBa WHERE Ten='"+cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))+"' AND SoDienThoai='"+sodienthoai+"'");
                        if(cursorCheck.moveToFirst()==false)
                        {
                            database.insertDataDanhBa(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                                    sodienthoai,
                                    hinh,"","","");
                        }
                    }
                    Toast.makeText(this,"Cập nhật thành công ",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this,"Chưa cấp quyền truy cập danh bạ ",Toast.LENGTH_SHORT).show();
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
