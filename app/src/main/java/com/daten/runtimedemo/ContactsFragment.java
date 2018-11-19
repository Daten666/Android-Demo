package com.daten.runtimedemo;


import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "Contacts";
    private TextView mMessageText;
    private String DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample";
    private String[] PROJECTION = {ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
    private String ORDER = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC";

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new ContactsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container,false);
        mMessageText = rootView.findViewById(R.id.contact_message);
        //添加联系人按钮事件
        Button button = rootView.findViewById(R.id.contact_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertContact();
            }
        });

        Button button2 = rootView.findViewById(R.id.contact_load);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadContact();
            }
        });
        return rootView;
    }

    private void loadContact() {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void insertContact() {
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);
        // First, set up a new raw contact.
        ContentProviderOperation.Builder op =
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
        operations.add(op.build());

        // Next, set the name for the contact.
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        DUMMY_CONTACT_NAME);
        operations.add(op.build());

        // Apply the operations.
        ContentResolver resolver = getActivity().getContentResolver();
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            Snackbar.make(mMessageText.getRootView(), "无法新建联系人: " +
                    e.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getActivity(), ContactsContract.Contacts.CONTENT_URI, PROJECTION,
                null, null, ORDER);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            //只显示第一个联系人
            final int totalCount = data.getCount();
            if (totalCount > 0) {
                data.moveToFirst();
                String name = data
                        .getString(data.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                mMessageText.setText(
                        getResources().getString(R.string.contacts_string, totalCount, name)
                );
            } else {
                mMessageText.setText(R.string.contacts_empty);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mMessageText.setText(R.string.contacts_empty);
    }
}
