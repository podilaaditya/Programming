package com.innominds.lenskart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dms.jagdambajewellers.util.Constents;
import com.dms.jagdambajewellers.util.Utils;

public class Main extends Activity {

	int[] harnments = { R.drawable.product_1, R.drawable.product_2, };
	private boolean isRotated = true;

	int[] actors = { R.drawable.dupion };

	private int CAMERA_REQUEST = 1233;

	private LinearLayout layout;
	// private CustomView myView;
	// private VerticalSeekBar vertical_seek;
	private TouchImageView imageView;
	protected static final int RESULT_LOAD_IMAGE = 0;

	private static final int CAMERA_PIC_REQUEST = 234;
	private ProgressDialog pd;
	private static int Image_no = 1;
	int angle = 0;
	private TouchImageView harm_image;
	private LinearLayout harnment_layout;
	private int current_actor = 0;

	private LinearLayout mypic_layout;

	private Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

	/*	if (root.exists()) {

			if (root.list().length < 1) {
				Toast.makeText(getApplicationContext(),
						"First Load Images and try again.", Toast.LENGTH_LONG)
						.show();
				finish();
			} else {*/
				initGUI();

				events();
		/*	}
		} else {
			Toast.makeText(getApplicationContext(),
					"First Load Images and try again.", Toast.LENGTH_LONG)
					.show();
			finish();
		}*/

	}

	private void initGUI() {

		layout = (LinearLayout) findViewById(R.id.scroller_layout);

		// myView = (CustomView) findViewById(R.id.myView);

		imageView = new TouchImageView(getApplicationContext());

		mypic_layout = (LinearLayout) findViewById(R.id.mypic_layout);

		mypic_layout.addView(imageView, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		harnment_layout = (LinearLayout) findViewById(R.id.harnment_layout);

		harm_image = new TouchImageView(getApplicationContext());

		/*harnment_layout.addView(harm_image, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));*/

		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				actors[current_actor]);

		/*Bitmap haram_bitmap = BitmapFactory.decodeFile(root.listFiles()[0]
				.getAbsolutePath());
		harm_image.setImage(haram_bitmap, width, height);*/

		imageView.setImage(bitmap, width, height);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		// File[] harnments = root.listFiles();

		for (int i = 0; i < harnments.length; i++) {

			View view = inflater.inflate(R.layout.product_item, null);

			((ImageView) view.findViewById(R.id.product_image))
					.setImageBitmap((BitmapFactory.decodeResource(
							getResources(), harnments[i])));

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					harnment_layout.removeAllViews();

					harnment_layout.setVisibility(View.VISIBLE);

					((ImageView) v.findViewById(R.id.product_image))
							.buildDrawingCache();

					/*Display display = getWindowManager().getDefaultDisplay();
					int width = display.getWidth();
					int height = display.getHeight();*/

					harm_image = new TouchImageView(getApplicationContext());
					harm_image.setImage(((ImageView) v
							.findViewById(R.id.product_image))
							.getDrawingCache(), 150, 72);

					// harm_image.setBackgroundDrawable(imageView.getDrawable());

					harnment_layout.addView(harm_image,
							new LinearLayout.LayoutParams(
									LayoutParams.FILL_PARENT,
									LayoutParams.FILL_PARENT));
				}
			});

			layout.addView(view);

		}
	}

	private void events() {

		((CheckBox) findViewById(R.id.layout_handler))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked)
							((LinearLayout) findViewById(R.id.mainHiderLayOut))
									.setVisibility(View.VISIBLE);
						else
							((LinearLayout) findViewById(R.id.mainHiderLayOut))
									.setVisibility(View.GONE);
					}
				});

		((Button) findViewById(R.id.clear))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						harnment_layout.setVisibility(View.GONE);
						harnment_layout.removeAllViews();
					}
				});

		((Button) findViewById(R.id.rotate_pic))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						//
						// imageView.setDrawingCacheEnabled(true);
						// // // TODO Auto-generated method stub
						// Bitmap rotatedBitmap = rotateBitmap(
						// imageView.getDrawingCache(), 90);
						// if (isRotated)
						imageView.rotateImage(90);
						//
						// imageView = new
						// TouchImageView(getApplicationContext());
						//
						// mypic_layout.removeAllViews();
						//
						// mypic_layout.addView(imageView,
						// new LinearLayout.LayoutParams(
						// LayoutParams.FILL_PARENT,
						// LayoutParams.FILL_PARENT));
						//
						// Display display = getWindowManager()
						// .getDefaultDisplay();
						// int width = display.getWidth();
						// int height = display.getHeight();
						// imageView.setImage(rotatedBitmap, width, height);
						imageView.invalidate();
					}

				});

		((Button) findViewById(R.id.takeSnap))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Intent cameraIntent = new Intent(
						// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						// startActivityForResult(cameraIntent, CAMERA_REQUEST);
						launchCamera();

					}
				});

		((Button) findViewById(R.id.choose_image))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

						startActivityForResult(intent, RESULT_LOAD_IMAGE);
					}
				});

		// vertical_seek.setOnProgressChangedListener(new OnProgressBarChanged()
		// {
		//
		// @Override
		// public void onProgressChanged(int percent) {
		// // myView.ResizeImageWithPercent(percent);
		//
		// }
		// });
	}

	private void launchCamera() {

		// Define the file-name to save photo taken by Camera activity
		String fileName = "Camera_Example.jpeg";

		// Create parameters for Intent with filename

		ContentValues values = new ContentValues();

		values.put(MediaStore.Images.Media.TITLE, fileName);

		values.put(MediaStore.Images.Media.DESCRIPTION,
				"Image capture by camera");

		// imageUri is the current activity attribute, define and save it for
		// later usage
		imageUri = getContentResolver().insert( 
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		// Standard Intent action that can be sent to have the camera
		// application capture an image and return it.

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, 0);

		startActivityForResult(intent, CAMERA_PIC_REQUEST);

	}

	protected Bitmap rotateBitmap(Bitmap orglImg, int i) {
		Matrix matrix = new Matrix();
		if (isRotated) {
			isRotated = false;
			// matrix.postRotate(i);
			matrix.setRotate(i, orglImg.getWidth() / 2, orglImg.getHeight() / 2);
		} else {
			isRotated = true;
			// matrix.postRotate(-i);
			matrix.setRotate(-i, orglImg.getWidth() / 2,
					orglImg.getHeight() / 2);
		}
		// return Bitmap.createBitmap(orglImg, 0, 0, orglImg.getWidth(),
		// orglImg.getHeight(), matrix, true);
		imageView.setScaleType(ScaleType.MATRIX);
		imageView.setImageMatrix(matrix);
		imageView.invalidate();
		return null;
	}

	protected Boolean saveImage() {

		Bitmap finalBitmap = combineImages(imageView.getDrawingCache(),
				harm_image.getDrawingCache());
		boolean result = false;
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;

			try {
				isDirectoryAvailable();

				while (isFileAvailable()) {

				}
				FileOutputStream out = new FileOutputStream(
						Environment.getExternalStorageDirectory()
								+ File.separator + "Jagadamba" + File.separator
								+ "Jagadamba" + Image_no + ".png");
				finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.close();
				result = true;

				return result;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;

			Toast.makeText(getApplicationContext(),
					"Problem while saving file", Toast.LENGTH_SHORT).show();

			return false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write

			Toast.makeText(getApplicationContext(), "Memmory Not available",
					Toast.LENGTH_SHORT).show();
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			return false;
		}
		return result;

		/*
		 * File sdCard = Environment.getExternalStorageDirectory(); File dir =
		 * new File (sdCard.getAbsolutePath() + "/dir1/dir2"); dir.mkdirs();
		 * File file = new File(dir, "filename");
		 * 
		 * FileOutputStream f = new FileOutputStream(file); InputStream in =
		 * f.getInputStream();
		 * 
		 * byte[] buffer = new byte[1024]; int len1 = 0; while ((len1 =
		 * in.read(buffer)) > 0) { f.write(buffer, 0, len1); } f.close();
		 */

	}

	private void isDirectoryAvailable() {
		// TODO Auto-generated method stub
		File f = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "Jagadamba" + File.separator);
		if (f.isDirectory())
			return;
		else {
			f.mkdir();
			return;
		}
	}

	private boolean isFileAvailable() {

		File f = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "Jagadamba" + File.separator + "Jagadamba"
				+ Image_no + ".png");
		if ((f.exists())) {
			Image_no++;
			return true;
		} else {
			return false;
		}
	}

	public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter
														// 'String loc' if you
														// want to save the new
														// image - left some
														// code to do that at
														// the bottom
		Bitmap cs = null;

		int width, height = 0;

		if (c.getWidth() > s.getWidth()) {
			width = c.getWidth();
			height = c.getHeight();
		} else {
			width = s.getWidth();
			height = c.getHeight();
		}

		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Canvas comboImage = new Canvas(cs);

		comboImage.drawBitmap(c, 0f, 0f, null);

		Bitmap waterMarkImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.water_mark_2);

		comboImage.drawBitmap(waterMarkImage,
				width - (waterMarkImage.getWidth() + 30), height
						- (waterMarkImage.getHeight() + 50), null);
		comboImage.drawBitmap(s, 0f, 0f, null);
		// Bitmap.createScaledBitmap(waterMarkImage, 120, 120, false);

		// this is an extra bit I added, just incase you want to save the new
		// image somewhere and then return the location
		/*
		 * String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";
		 * 
		 * OutputStream os = null; try { os = new FileOutputStream(loc +
		 * tmpImg); cs.compress(CompressFormat.PNG, 100, os); }
		 * catch(IOException e) { Log.e("combineImages",
		 * "problem combining images", e); }
		 */
		return cs;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			// imageView = (ImageView) findViewById(R.id.pic);
			imageView.setVisibility(View.VISIBLE);

			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			int height = display.getHeight();

			Bitmap bitmap = decodeFile1(new File(picturePath));

			if (bitmap != null) {
				mypic_layout.removeAllViews();
				imageView = new TouchImageView(getApplicationContext());
				imageView.setImage(bitmap, width, height);
				mypic_layout.addView(imageView, new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				imageView.invalidate();
			} else {
				showDialogAlert("Image Capture", "Problem with image");
			}

		}

		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			mypic_layout.removeAllViews();
			imageView.setVisibility(View.VISIBLE);
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imageView = new TouchImageView(getApplicationContext());
			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			int height = display.getHeight();
			imageView.setImage(photo, width, height);
			mypic_layout.addView(imageView, new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			imageView.invalidate();

			// imageView.setImageBitmap(photo);
		}
		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {

			if (resultCode == RESULT_OK) {
				// Load Captured Image.
				String imageId = Utils.convertImageUriToFile(imageUri,
						Main.this);

				// Create and execute AsyncTask to load capture image
				LoadImagesFromSDCard loadImagesFromSDCard = new LoadImagesFromSDCard();
				loadImagesFromSDCard.execute("" + imageId);

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	private Bitmap decodeFile1(File file) {
		// TODO Auto-generated method stub

		Bitmap preview_bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;

		try {
			preview_bitmap = BitmapFactory.decodeStream(new FileInputStream(
					file), null, options);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return preview_bitmap;
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 40;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public void showDialogAlert(String string, String string2) {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(string);
		// Screening for a new patient for DR TB
		alertDialog.setMessage(string2);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alertDialog.show();
	}

	private void sendMail() {
		Bitmap screenshot = getBitmapFromMapView();

		String path = "";
		// String path = Images.Media.insertImage(getContentResolver(),
		// screenshot, "title", null);

		Uri u = null;
		File fi = Environment.getRootDirectory();

		try {
			u = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(
					getContentResolver(), fi.getAbsolutePath(), null, null));
			path = u.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Uri screenshotUri = Uri.parse(path);

		final Intent emailIntent = new Intent(
				android.content.Intent.ACTION_SEND);
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (u != null)
			emailIntent.putExtra(Intent.EXTRA_STREAM, u);
		emailIntent.setType("image/png");

		startActivity(Intent.createChooser(emailIntent, "Send email using"));

	}

	public Bitmap getBitmapFromMapView() {
		// Get Bitmap from MapView
		View view = (RelativeLayout) findViewById(R.id.final_layout);
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bm = view.getDrawingCache();
		return bm;
	}

	// Class with extends AsyncTask class
	public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(Main.this);
		private Bitmap mBitmap;

		protected void onPreExecute() {
			// Progress Dialog
			Dialog.setMessage(" Loading image from Sdcard..");
			Dialog.show();
		}

		// Call after onPreExecute method
		protected Void doInBackground(String... urls) {

			Bitmap bitmap = null;
			Bitmap newBitmap = null;
			Uri uri = null;
			try {

				uri = Uri.withAppendedPath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ""
								+ urls[0]);
				// Decode an input stream into a bitmap.
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uri));
				if (bitmap != null) {
					// Creates a new bitmap, scaled from an existing bitmap.
					newBitmap = Bitmap
							.createScaledBitmap(bitmap, bitmap.getWidth() / 2,
									bitmap.getHeight() / 2, true);
					bitmap.recycle();
					if (newBitmap != null) {
						mBitmap = newBitmap;
					}
				}
			} catch (IOException e) {
				// Cancel execution of this task.
				cancel(true);
			}
			return null;
		}

		protected void onPostExecute(Void unused) {
			// You can call UI Element here.
			// Close progress dialog
			Dialog.dismiss();
			if (mBitmap != null) {
				// Set Image to ImageView
				mypic_layout.removeAllViews();
				imageView.setVisibility(View.VISIBLE);
				// Bitmap photo = (Bitmap) data.getExtras().get("data");
				imageView = new TouchImageView(getApplicationContext());
				Display display = getWindowManager().getDefaultDisplay();
				int width = display.getWidth();
				int height = display.getHeight();
				imageView.setImage(mBitmap, width, height);
				mypic_layout.addView(imageView, new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				imageView.invalidate();
			}
		}

	}

	private class MyBackGroundTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Main.this.pd.dismiss();
			if (result) {
				Toast.makeText(getApplicationContext(),
						"File Save Successfully", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Problem while saving file", Toast.LENGTH_SHORT).show();

			}

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Boolean returnValue = saveImage();
			return returnValue;
		}

	}
}
