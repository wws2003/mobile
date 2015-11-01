package com.tbg.simplestvallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.model.dto.LocalEntry;
import com.tbg.simplestvallet.util.DateUtil;

import java.util.List;

/**
 * Created by wws2003 on 10/29/15.
 */
public class PendingEntryListAdapter extends ArrayAdapter<LocalEntry> {

    private LayoutInflater mLayoutInflater;

    public PendingEntryListAdapter(Context context, List<LocalEntry> objects) {
        super(context, 0, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null) {
            itemView = mLayoutInflater.inflate(R.layout.pending_entry, null);
        }
        LocalEntry localEntry = getItem(position);
        renderLocalEntry(localEntry, itemView);
        return itemView;
    }

    private void renderLocalEntry(LocalEntry entry, View itemView) {
        TextView tvEntryType = (TextView)itemView.findViewById(R.id.tv_item_pending_entry_type);
        tvEntryType.setText(entry.getType().toString());
        TextView tvEntryDate = (TextView)itemView.findViewById(R.id.tv_item_pending_entry_date);
        tvEntryDate.setText(DateUtil.getYMDString(entry.getCreatedAt()));
        TextView tvEntryAmount = (TextView)itemView.findViewById(R.id.tv_item_pending_entry_amount);
        tvEntryAmount.setText(entry.getMoneyQuantity().toString());
    }
}
