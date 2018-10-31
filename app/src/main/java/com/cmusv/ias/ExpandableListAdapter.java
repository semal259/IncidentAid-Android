package com.cmusv.ias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<String> _listDataHeader; 
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, List<String>> _hashUserAlert;
 
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
            HashMap<String, List<String>> listChildData, HashMap<String, List<String>> hashUserAlert) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._hashUserAlert = hashUserAlert;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @SuppressLint("InflateParams") @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        
        TextView txtListAlertStatus = (TextView) convertView
                .findViewById(R.id.lblListAlertStatus);
        
        TextView txtListDelivered = (TextView) convertView
                .findViewById(R.id.lblDelivered);
 
        TextView txtListAcknowledged = (TextView) convertView
                .findViewById(R.id.lblAcknowledged);
        
        txtListChild.setText(childText);
    	
        List<String> alert_details = new ArrayList<String>();
        
        if(_hashUserAlert.get(childText) != null) {
        	alert_details = _hashUserAlert.get(childText);
	        txtListAlertStatus.setText(alert_details.get(0)); 
	        txtListAlertStatus.setBackgroundColor(Color.BLUE);
	        if(alert_details.get(0).contains("Mayday"))
        	{
        		txtListChild.setBackgroundColor(Color.RED);
        	}
	        
	        if(alert_details.get(1).equals("1")) {
	        	txtListDelivered.setBackgroundColor(Color.YELLOW);
	        	txtListDelivered.setText("D");
	        	txtListDelivered.setTextColor(Color.BLACK);
	        }
	        else {
	        	txtListDelivered.setBackgroundColor(Color.TRANSPARENT);
	        	txtListDelivered.setText("");
	        }
	        if(alert_details.get(2).equals("1")) {
	        	txtListAcknowledged.setBackgroundColor(Color.GREEN);
	        	txtListAcknowledged.setText("A");
	        	txtListAcknowledged.setTextColor(Color.BLACK);
	        }
	        else {
	        	txtListAcknowledged.setBackgroundColor(Color.TRANSPARENT);
	        	txtListAcknowledged.setText("");
	        }
        }
        else
        {
        	txtListAlertStatus.setText("");
        	txtListDelivered.setText("");
        	txtListAcknowledged.setText("");
        	txtListDelivered.setBackgroundColor(Color.TRANSPARENT);
        	txtListAlertStatus.setBackgroundColor(Color.TRANSPARENT);
        	txtListAcknowledged.setBackgroundColor(Color.TRANSPARENT);
        }
        
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @SuppressLint("InflateParams") @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}