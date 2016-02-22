package com.samsung.ssc.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.samsung.ssc.R;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CommonUtil {

	public static void getListViewSize(ListView myListView) {
		ListAdapter myListAdapter = myListView.getAdapter();
		if (myListAdapter == null) {
			// do nothing return null
			return;
		}
		// set listAdapter in loop for getting final size
		int totalHeight = 0;
		for (int size = 0; size < myListAdapter.getCount(); size++) {
			View listItem = myListAdapter.getView(size, null, myListView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		// setting listview item in adapter
		ViewGroup.LayoutParams params = myListView.getLayoutParams();
		params.height = totalHeight
				+ (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
		myListView.setLayoutParams(params);
		// print height of adapter on log
		Log.i("height of listItem:", String.valueOf(totalHeight));
	}

	/**
	 * Method to check if the contact exists in phone book or not
	 * 
	 * @param context
	 * @param number
	 * @return true/false
	 */
	public static boolean isContactExists(Context context, String number) {
		// / number is the phone number
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		String[] mPhoneNumberProjection = { PhoneLookup._ID,
				PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
		Cursor cur = context.getContentResolver().query(lookupUri,
				mPhoneNumberProjection, null, null, null);
		try {
			if (cur.moveToFirst()) {
				return true;
			}
		} finally {
			if (cur != null)
				cur.close();
		}
		return false;
	}

	public static void saveContact(Context context, String MobNumber,
			String DisplayName, String email, String address, Bitmap bitmap) {

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		int rawContactID = ops.size();

		// Adding insert operation to operations list
		// to insert a new raw contact in the table ContactsContract.RawContacts
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());

		// Adding insert operation to operations list
		// to insert display name in the table ContactsContract.Data
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
						rawContactID)
				.withValue(ContactsContract.Data.MIMETYPE,
						StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, DisplayName).build());

		// Adding insert operation to operations list
		// to insert Mobile Number in the table ContactsContract.Data
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
						rawContactID)
				.withValue(ContactsContract.Data.MIMETYPE,
						Phone.CONTENT_ITEM_TYPE)
				.withValue(Phone.NUMBER, MobNumber)
				.withValue(Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
				.build());

		if (address != null && !(address.equals(""))) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
					.withValue(
							ContactsContract.CommonDataKinds.StructuredPostal.STREET,
							address).build());
		}
		// .withValue(ContactsContract.Data.MIMETYPE,
		// ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
		// )
		// .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
		// "Postbox")
		// .withValue(ContactsContract.Data.MIMETYPE,
		// ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
		// )
		// .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY,
		// "city")
		//
		// .withValue(ContactsContract.Data.MIMETYPE,
		// ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
		// )
		// .withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION,
		// "region")
		//
		// .withValue(ContactsContract.Data.MIMETYPE,
		// ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
		// )
		// .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
		// "postcode")
		//
		// .withValue(ContactsContract.Data.MIMETYPE,
		// ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
		// )
		// .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
		// "country")
		//
		// .withValue(ContactsContract.Data.MIMETYPE,
		// ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
		// )
		// .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
		// "3")

		if (bitmap != null) {

			ByteArrayOutputStream image = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, image);

			Builder builder = ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI);
			builder.withValueBackReference(
					ContactsContract.Data.RAW_CONTACT_ID, 0);
			builder.withValue(ContactsContract.Data.MIMETYPE,
					ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
			builder.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO,
					image.toByteArray());
			ops.add(builder.build());

		}

		if ( email!=null && !(email.equals("")) ) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)

					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Email.DATA,
							email)
					.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
							Email.TYPE_WORK).build());
		}
		try {
			// Executing all the insert operations as a single database
			// transaction
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,
					ops);
			
			Helper.showCustomToast(context, R.string.contact_is_already_added, Toast.LENGTH_SHORT);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
	}

	public static void openDialer(Context context, String mobNumber) {
		try {
			/*
			 * Intent intent = new Intent(Intent.ACTION_DIAL);
			 * intent.setData(Uri.parse("e:" + mobNumber));
			 * context.startActivity(intent);
			 */
			Uri uri = Uri.parse("tel:" + mobNumber);

			Intent intent = new Intent(Intent.ACTION_VIEW, uri);

			context.startActivity(intent);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
