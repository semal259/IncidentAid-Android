package com.cmusv.ias.ui.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.cmusv.ias.data.IASHelper;
import com.cmusv.ias.data.IASIncident;
import com.cmusv.ias.data.IASUtilities;
import com.cmusv.ias.IncidentSessionManager;
import com.cmusv.ias.R;
import com.cmusv.ias.ui.ImageFileAdapter;

public class CommCOPFragment extends Fragment{
	BroadcastReceiver sent_reciever;
	BroadcastReceiver delivered_reciever;
	private GridView gridCOP;
	private ArrayList<String> mThumbs;
	private Button btnTakePicture;
	private Button btnCloseIncident;
	
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final String ARG_SECTION_NUMBER = "section_number";

	public static CommCOPFragment newInstance(int sectionNumber) {
		CommCOPFragment fragment = new CommCOPFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_comm_cop, container, false);
		
		// COP GRIDVIEW
		//Integer[] mThumbIds = IASHelper.getAllCOPThumbnailIdsByIncident(getActivity());
		
		mThumbs = getThumbs();
		
		gridCOP = (GridView) rootView.findViewById(R.id.gridCOP);
		gridCOP.setAdapter(new ImageFileAdapter(getActivity(), getResources(), mThumbs, 200, 150));
		gridCOP.setOnItemClickListener(new OnItemClickListener() {
			
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	
            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            	View view = View.inflate(getActivity(), R.layout.image, null);
            	alert.setView(view);
            	ImageView imgCOP = (ImageView) view.findViewById(R.id.img);
            	imgCOP.setImageDrawable(Drawable.createFromPath(mThumbs.get(position)));
				
				alert.setPositiveButton("Set as COP", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                	try {
	                		Log.d("DEV", "TODO: set this as current COP on server");
	                	} catch (Exception e) {
	                		Toast.makeText(getActivity(), "Unable to set COP", Toast.LENGTH_SHORT).show();
	                	}
	                }
	            });
				
	            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                     dialog.cancel();
	                }
	            });
				alert.show();
			}
		});
		
		// TAKE PICTURE BUTTON
		btnTakePicture = (Button) rootView.findViewById(R.id.btnTakePicture);
		btnTakePicture.setOnClickListener(new View.OnClickListener() {
	 		public void onClick(View v) {
	 			if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
	 				Toast.makeText(getActivity(), "CANNOT TAKE PICTURES WITH THIS DEVICE.", Toast.LENGTH_LONG).show();
	 				return;
	 			}
	 			
	 			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	 			if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
		        	startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	 			}
	 		}
	    });

	  	// CLOSE INCIDENT BUTTON
		btnCloseIncident = (Button) rootView.findViewById(R.id.btnCloseIncident);
		btnCloseIncident.setOnClickListener(new View.OnClickListener() {
	 		public void onClick(View v) {
	
				IncidentSessionManager inc_session = new IncidentSessionManager(getActivity());
				HashMap<String, String> incident = inc_session.getIncidentDetails();
	
				IASIncident oIASIncident = new IASIncident();
		        oIASIncident.setIncident_name(incident.get(IncidentSessionManager.KEY_INC_NAME));
		        oIASIncident.setEnd_time(IASUtilities.getCurrentDateTime());
		        oIASIncident.setFirefighters(null);
	
		        if(IASHelper.updateIncidentByIncidentName(getActivity(), oIASIncident))
		        	inc_session.closeIncident();
	 		}
	    });
		
		return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap imageBitmap = (Bitmap) extras.get("data");
	        try {
				File thumbFile = createImageFile();
				FileOutputStream fos = new FileOutputStream(thumbFile);
				imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
			} catch (IOException e) {
				Toast.makeText(getActivity(), "ERROR SAVING PICTURE.", Toast.LENGTH_LONG).show();
			}
	    }
		mThumbs = getThumbs();
		gridCOP = (GridView) getView().findViewById(R.id.gridCOP);
		gridCOP.setAdapter(new ImageFileAdapter(getActivity(), getResources(), mThumbs, 200, 150));
	}
		

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		String imageFilename = "JPEG_" + timeStamp + "_";
	    File storageDir = getActivity().getFilesDir();
	    
	    
	    
	    File image = File.createTempFile(
	        imageFilename,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );
	    
	    Log.d("DEV", "SAVING TO: " + image.getAbsolutePath());

	    // Save a file: path for use with ACTION_VIEW intents
	    //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	private ArrayList<String> getThumbs() {
		ArrayList<String> thumbs = new ArrayList<String>();
		File dir = getActivity().getFilesDir();
		File[] files = dir.listFiles();
		
		for (File file : files) {
			Log.d("DEV", "TRYING: " + file.getAbsolutePath());
			String[] filename = file.getAbsolutePath().split("\\.");
			if (filename[filename.length - 1].equals("jpg")) {
				Log.d("DEV", "LOADING: " + file.getAbsolutePath());
				thumbs.add(file.getAbsolutePath());
			}
		}

		return thumbs;
	}
		
	@Override
    public void onDestroy() 
	{
		if (delivered_reciever != null)
			getActivity().unregisterReceiver(delivered_reciever);
		if (sent_reciever != null)
			getActivity().unregisterReceiver(sent_reciever);
		super.onDestroy();
	}
}
