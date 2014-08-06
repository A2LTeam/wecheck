/**
 * 
 */
package com.forfun.wecheck.fragment;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forfun.wecheck.R;
import com.forfun.wecheck.activity.scan.CameraPreview;

/**
 * @author Nick
 * 
 */
public class ScanFragment extends Fragment {
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;

	TextView scanText;
	Button scanButton;

	ImageScanner scanner;

	private boolean barcodeScanned = false;
	private boolean previewing = true;
	FrameLayout preview;

	static {
		System.loadLibrary("iconv");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View V = inflater.inflate(R.layout.scan, container, false);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();

		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(getActivity(), mCamera, previewCb, autoFocusCB);
		preview = (FrameLayout) V.findViewById(R.id.cameraPreview);
		preview.addView(mPreview);

		scanText = (TextView) V.findViewById(R.id.scanText);

		scanButton = (Button) V.findViewById(R.id.ScanButton);

		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((LinearLayout) getActivity().findViewById(R.id.scan)).removeAllViews(); 
				ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				final Bundle bundle = new Bundle();
				bundle.putString("itemName", "ABCDE");
				bundle.putInt("itemId", 153);
				itemDetailFragment.setArguments(bundle);

				fragmentTransaction.addToBackStack("test");
				fragmentTransaction.replace(R.id.scan, itemDetailFragment);
				fragmentTransaction.commit();
//				if (barcodeScanned) {
//					barcodeScanned = false;
//					scanText.setText("Scanning...");
//					mCamera.setPreviewCallback(previewCb);
//					mCamera.startPreview();
//					previewing = true;
//					mCamera.autoFocus(autoFocusCB);
//				}
			}
		});
		return V;
	}

	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}
	
    public void onPause() {
        super.onPause();
        releaseCamera();
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
}
