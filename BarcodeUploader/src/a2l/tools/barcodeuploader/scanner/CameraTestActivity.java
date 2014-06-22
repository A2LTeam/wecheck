/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 * 
 * Created by lisah0 on 2012-02-24
 */
package a2l.tools.barcodeuploader.scanner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
/* Import ZBar Class files */
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import a2l.tools.barcodeuploader.R;
import a2l.tools.barcodeuploader.constant.ItemCst;
import a2l.tools.barcodeuploader.database.DataManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CameraTestActivity extends Activity {

	private static final String SUBMIT_URL = "http://ktslopez.bugs3.com/setBarCode.php";

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private boolean isSubmit;
	private String barcodeTxt;
	private String itemCode;

	TextView scanText;
	Button scanButton;

	ImageScanner scanner;

	private boolean barcodeScanned = false;
	private boolean previewing = true;

	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.scanning);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();

		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
		preview.addView(mPreview);

		scanText = (TextView) findViewById(R.id.scanText);

		scanButton = (Button) findViewById(R.id.SubmitButton);

		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Bundle extras = getIntent().getExtras();
				itemCode = extras.getString(ItemCst.CODE);
				if (barcodeScanned) {
					AsyncTask<Void, Void, Boolean> loadingTask = new AsyncTask<Void, Void, Boolean>() {

						// --------------------------------------------------------------------------------------------------
						@Override
						protected Boolean doInBackground(Void... params) {
							boolean updateSuccess = false;
							try {
								String url = SUBMIT_URL;
								// + "itemCode=" + itemCode + "&barCode=" +
								// barcodeTxt
								HttpGet httpGet = new HttpGet(url + "?itemCode=" + itemCode + "&barCode=" + barcodeTxt);
								HttpClient client = new DefaultHttpClient();
								HttpResponse response = client.execute(httpGet);
								if ("Success".equals(EntityUtils.toString(response.getEntity()))) {
									updateSuccess = true;
								}
								// Success

							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							return updateSuccess;
						}

						// --------------------------------------------------------------------------------------------------
						@Override
						protected void onPostExecute(Boolean succeed) {
							if (succeed) {
								new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
										.setTitle("Update Success").setMessage("Update Successfully").show();
								DataManager dataManager = DataManager.getInstance(getActivity());
								dataManager.openDatabase();
								String query = "UPDATE ITEM SET BARCODE = '" + barcodeTxt + "' WHERE CODE = '" + itemCode
										+ "'";
								dataManager.getSQLiteDatabase().execSQL(query);
								dataManager.closeDatabase();
								setResult(RESULT_OK);
								finish();
							} else {
								new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
										.setTitle("Update Fail").setMessage("Update Fail").show();
								turnOnBarcodeScan();
							}
						}
					};
					loadingTask.execute();

				}
			}
		});
	}

	private void turnOnBarcodeScan() {
		barcodeScanned = false;
		scanText.setText("Scanning...");
		mCamera.setPreviewCallback(previewCb);
		mCamera.startPreview();
		previewing = true;
		mCamera.autoFocus(autoFocusCB);
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					scanText.setText("barcode result " + sym.getData());
					barcodeTxt = sym.getData();
					barcodeScanned = true;
				}
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	public boolean getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(boolean isSubmit) {
		this.isSubmit = isSubmit;
	}

	private Activity getActivity() {
		return this;
	}
}
