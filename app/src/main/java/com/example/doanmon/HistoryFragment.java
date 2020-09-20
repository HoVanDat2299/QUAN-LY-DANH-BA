package com.example.doanmon;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.CallLog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import ClassDoiTuon.CuocGoi;



public  class HistoryFragment extends Fragment {
    private View rootView;
    SearchView searchView;
    ListView listView;
    ArrayList<CuocGoi> arrayList;
    CallLogAdapter callLogAdapter;
    final int REQUEST_READ_LOG_CALL=111;
    final int REQUEST_CALL=222;
    final int REQUEST_MESSEAAGE=333;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_history, container, false);
        listView=rootView.findViewById(R.id.listhistory);

        listView.setVisibility(View.VISIBLE);
        searchView = (SearchView)rootView.findViewById(R.id.search_vew);
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

                callLogAdapter.filter(newText.toString());
                if(newText!=""&&newText!=null) {
                    CallLogAdapter callLogAdapter2 = new CallLogAdapter(callLogAdapter.getHistories(), R.layout.itemlistviewcuocgoi, getActivity());
                    listView.setAdapter(callLogAdapter2);
                }else {
                    CallLogAdapter callLogAdapter1 = new CallLogAdapter(arrayList,R.layout.itemlistviewcuocgoi, getActivity());

                    listView.setAdapter(callLogAdapter1);
                }
                return false;
            }
        });
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG )!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_READ_LOG_CALL);
        }
        return rootView;
    }
    public void onResume() {

        super.onResume();
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG )==PackageManager.PERMISSION_GRANTED)
        {
            arrayList=new ArrayList<>();
            callLogAdapter=new CallLogAdapter(arrayList,R.layout.itemlistviewcuocgoi,getActivity());
            listView.setAdapter(callLogAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    intentCall(arrayList.get(position).getSodienthoai());
                }
            });

            Cursor cursor=getActivity().managedQuery(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.DATE + " DESC");
            int count=0;
            while (cursor.moveToNext()&& count!=50) {
                count++;
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String calltype = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                Date Strdate = new Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
                String date = (Strdate.getDay() >= 10 ? Strdate.getDay() : "0" + Strdate.getDay()) + "/" +
                        (Strdate.getMonth() + 1 >= 10 ? (Strdate.getMonth() + 1) : "0" + (Strdate.getMonth() + 1)) + "/" +
                        (Strdate.getYear() + 1900) + ", " +
                        (Strdate.getHours() >= 10 ? Strdate.getHours() : "0" + Strdate.getHours()) + ":" +
                        (Strdate.getMinutes() >= 10 ? Strdate.getMinutes() : "0" + Strdate.getMinutes());

                String Name = number.toString();
                String icon = "@string/fa_phone_solid";
                byte[] Avatar = {};
                Cursor search = MainActivity.database.GetData("SELECT Ten,HinhAnh FROM DanhBa Where SoDienThoai='" + number + "'");
                if (search.moveToFirst() == true) {
                    search.moveToFirst();
                    Name = search.getString(search.getColumnIndex("Ten"));
                    Avatar = search.getBlob(search.getColumnIndex("HinhAnh"));
                }

                switch (Integer.parseInt(calltype))
                {
                    case CallLog.Calls.OUTGOING_TYPE:
                        icon="\uf061";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        icon="\uf060";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        icon="\uf00d";
                        break;

                }
                arrayList.add(new CuocGoi(Avatar, Name, number, icon, date));
            }

            callLogAdapter.notifyDataSetChanged();
            //search

            }
        }
    //Xử lý search


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.callog_menucontext,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        CuocGoi c= arrayList.get(info.position);

        switch (item.getItemId())
        {
            case R.id.menu_call: {
                if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
                {
                    intentCall(c.getSodienthoai());
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                }
                return true;
            }
            case R.id.menu_messege:{
                if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED)
                {
                    intentSendSMS(c.getSodienthoai());
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_MESSEAAGE);
                }
                return true;
            }
            case R.id.menu_add_user: {
                if(c.getTen()==c.getSodienthoai())
                {
                    Intent intent=new Intent(getActivity(),ThemDanhBa_Activity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("sdt",c.getSodienthoai());
                    bundle.putInt("id",-1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getActivity(),"Đã có người này trong danh bạ",Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.menu_copy: {
                ClipboardManager clipboardManager= (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("copy",c.getSodienthoai());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getActivity(),"Đã sao chép số",Toast.LENGTH_SHORT).show();
                return true;
            }
            default: return super.onContextItemSelected(item);
        }

    }

    private void intentCall(String number)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    private  void intentSendSMS(String number)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:"+number));
        startActivity(intent);
    }

}
