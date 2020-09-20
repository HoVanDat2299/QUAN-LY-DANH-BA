package com.example.doanmon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import ClassDoiTuon.CuocGoi;
import ClassDoiTuon.DanhBa;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListUsersAdapter extends RecyclerView.Adapter<ListUsersAdapter.ViewHolder> implements View.OnClickListener {

    ArrayList<DanhBa> listUsers = new ArrayList<>();
    ArrayList<DanhBa> listseach;
    private ItemClickListener itemClickListener;

    public ListUsersAdapter(ArrayList<DanhBa> listUsers) {
        this.listUsers = listUsers;
        this.listseach=new ArrayList<>();
    }

    public  ListUsersAdapter(View view)
    {
        view.setOnClickListener(this); // Mấu chốt ở đây , set sự kiên onClick cho View
    }
    public void filter(String charText) {

        charText= charText.toLowerCase(Locale.getDefault());
        if(listseach!=null)
        {
            listseach.clear();
        }
        if (charText.length() == 0) {
            listseach.addAll(listUsers);
        }
        else {
            for (int i= 0; i<listUsers.size();i++) {
                if (listUsers.get(i).getName().toLowerCase(Locale.getDefault()).contains(charText.toLowerCase())) {
                    listseach.add(listUsers.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
    public ArrayList<DanhBa> getHistories(){
        return listseach;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemrecyler, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Collections.sort(listUsers, new Comparator<DanhBa>() {
            @Override
            public int compare(DanhBa o1, DanhBa o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        DanhBa users = listUsers.get(position);
        holder.getmUserName().setText(users.getName());
        byte[] hinh = users.getAvatar();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
        holder.mUserAvatar.setImageBitmap(bitmap);
        holder.getmSection().setText(users.getName().substring(0, 1));
        if (position > 0) {
            int i = position - 1;
            if (i < listUsers.size() && users.getName().substring(0, 1).equals(listUsers.get(i).getName().substring(0, 1))) {
                holder.getmSection().setVisibility(View.GONE);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DanhBa d = listUsers.get(position);
                Intent intent = new Intent(v.getContext(), ChiTiet_Activity.class);
                intent.putExtra("id", d.getId());
                v.getContext().startActivity(intent);

            }
        });



    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView mUserAvatar;
        private final TextView mUserName;
        private final TextView mSection;


        public ViewHolder(View v) {
            super(v);
            mUserAvatar = (CircleImageView) v.findViewById(R.id.imageItem);
            mUserName = (TextView) v.findViewById(R.id.txt_name);
            mSection = (TextView) v.findViewById(R.id.mSection);
        }

        public CircleImageView getmUserAvatar() {
            return mUserAvatar;
        }

        public TextView getmUserName() {
            return mUserName;
        }

        public TextView getmSection() {
            return mSection;
        }

    }


}


