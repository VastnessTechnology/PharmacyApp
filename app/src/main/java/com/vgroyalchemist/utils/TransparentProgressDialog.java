package com.vgroyalchemist.utils;

/**
 * Function to display custom Progress Dialog
 *
 * @param context
 *            - application context
 * @param resourceIdOfImage
 *            - image id
 * */

import android.app.Dialog;
import android.content.Context;

import com.vgroyalchemist.R;


public class TransparentProgressDialog extends Dialog {

	public TransparentProgressDialog(Context context, int resourceIdOfImage, boolean isCancelable) {

		super(context, R.style.TransparentProgressDialog);
		setContentView(R.layout.progressdialog_layout);

	}

	@Override
	public void show() {
		super.show();
	}
}
