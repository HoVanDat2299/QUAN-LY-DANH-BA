package com.example.doanmon;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObservable;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import ClassDoiTuon.DanhBa;

import static com.example.doanmon.MainActivity.database;


/**
 * A simple {@link Fragment} subclass.
 */
public class DanhbaFragment extends Fragment  {

    private View rootView;
    private RecyclerView listView;
    private ArrayList<DanhBa> arrayList;
    ImageView buttonThem;
    ImageView updateSDT;
    SearchView searchView;
    private ListUsersAdapter customListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    final int REQUEST_READ_CONTACT=123;





    public DanhbaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_danhba, container, false);
        buttonThem = rootView.findViewById(R.id.buttonThem);
        searchView =rootView.findViewById(R.id.search_danhba);
        updateSDT=rootView.findViewById(R.id.updateSDT);
        listView=(RecyclerView)rootView.findViewById(R.id.list_danhba);
        mLayoutManager = new LinearLayoutManager(getContext());

//        indexBar = rootView.findViewById(R.id.side_bar);
//        indexBar.setListView(listView);
        buttonThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThemDanhBa_Activity.class);
                startActivity(intent);

            }
        });
        updateSDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACT);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onResume() {
        super.onResume();
        arrayList = new ArrayList<>();
        customListAdapter = new ListUsersAdapter(arrayList);
        listView.setLayoutManager(mLayoutManager);
        listView.setAdapter(customListAdapter);
        Cursor cursor = database.GetData("SELECT * FROM DanhBa  ORDER BY Ten ASC");
        while (cursor.moveToNext()) {
            arrayList.add(new DanhBa(
                    cursor.getString(1),
                    cursor.getInt(0),
                    cursor.getBlob(3)
            ));

        }
        customListAdapter.notifyDataSetChanged();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;             }
            @Override
            public boolean onQueryTextChange(String newText) {
                customListAdapter.filter(newText.toString());
                if(newText!=""&&newText!=null) {
                    ListUsersAdapter listUsersAdapter = new ListUsersAdapter(customListAdapter.getHistories());
                    listView.setAdapter(listUsersAdapter);
                }
                return false;
            }
        });
    }


    }
