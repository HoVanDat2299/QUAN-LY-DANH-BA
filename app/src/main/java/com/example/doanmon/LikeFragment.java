package com.example.doanmon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ClassDoiTuon.DanhBa;


/**
 * A simple {@link Fragment} subclass.
 */
public class LikeFragment extends Fragment {

    private View rootView;
    ListView listView;
    private ArrayList<DanhBa> arrayList;
    CustomListAdapter customListAdapter;
    public LikeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_like,container,false);
        listView=rootView.findViewById(R.id.list_like);
        return rootView;
    }
    public void onResume() {
        super.onResume();
        arrayList=new ArrayList<>();
        customListAdapter =new CustomListAdapter(arrayList,R.layout.itemgridviewdanhba,getActivity());
        listView.setAdapter(customListAdapter);
        Cursor cursor= MainActivity.database.GetData("SELECT * FROM DanhBa WHERE YeuThich=1 ORDER BY Ten ASC");
        while(cursor.moveToNext()){
            arrayList.add(new DanhBa(
                    cursor.getString(1),
                    cursor.getInt(0),
                    cursor.getBlob(3)
            ));

        }

        customListAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DanhBa d=arrayList.get(position);
                Intent intent=new Intent(getActivity(),ChiTiet_Activity.class);
                intent.putExtra("id",d.getId());
                startActivity(intent);
            }
        });

    }
}
