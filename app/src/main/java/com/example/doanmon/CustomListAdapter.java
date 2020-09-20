package com.example.doanmon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

import ClassDoiTuon.DanhBa;

public class CustomListAdapter extends BaseAdapter implements SectionIndexer {

    private List<DanhBa> listdata;
    private int layout;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;

    public CustomListAdapter(List<DanhBa> listdata, int layout, Context context) {
        this.listdata = listdata;
        this.layout = layout;
        this.context = context;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =layoutInflater.inflate(layout,null);
            holder=new ViewHolder();
            holder.Name=convertView.findViewById(R.id.txt_name);
            holder.Avatar=convertView.findViewById(R.id.imageItem);
            convertView.setTag(holder);
        }else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        DanhBa danhBa=listdata.get(position);

        holder.Name.setText(danhBa.getName());

        byte[] hinh=danhBa.getAvatar();
        Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
        holder.Avatar.setImageBitmap(bitmap);

        return convertView;
    }
// Custum dong
        private void setSection(LinearLayout header, String label) {
            TextView text = new TextView(context);
            header.setBackgroundColor(0xffaabbcc);
            text.setTextColor(Color.WHITE);
            text.setText(label.substring(0, 1).toUpperCase());
            text.setTextSize(20);
            text.setPadding(5, 0, 0, 0);
            text.setGravity(Gravity.CENTER_VERTICAL);
            header.removeAllViews(); // <-- remove previous added
            header.addView(text);
            header.setVisibility(View.VISIBLE); // <-- show it
        }
    @Override
    public int getPositionForSection(int section) {
        if (section == 35) {
            return 0;
        }
        for (int i = 0; i <listdata.size(); i++) {
            String l = listdata.get(i).getName();
            char firstChar = l.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public Object[] getSections() {
        return new Object[0];
    }




    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    static class ViewHolder{
        ImageView Avatar;
        TextView Name;
    }

    public void toogleSelection(int position)
    {
        selectView(position,!mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public void remove(DanhBa object)
    {
        listdata.remove(object);
        notifyDataSetChanged();
    }

}
