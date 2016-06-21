package com.delacruz.ramon.fridgandroid.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.delacruz.ramon.fridgandroid.R;
import com.delacruz.ramon.fridgandroid.models.Item;
import com.delacruz.ramon.fridgandroid.util.Constants;
import com.firebase.client.Firebase;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ramon on 5/6/16.
 */
public class GroceryDetailFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences mSharedPreferences;
    private static Context mContext;
    private Item mItem;

    @Bind(R.id.detailItemNameTextView) TextView mNameTextView;
    @Bind(R.id.detailQuantityTextView) TextView mQuantityTextView;
    @Bind(R.id.detailNotesTextView) TextView mNotesTextView;
    @Bind(R.id.updateFab) FloatingActionButton mUpdateFab;
    @Bind(R.id.deleteFab) FloatingActionButton mDeleteFab;



    public GroceryDetailFragment() {
        // Required empty public constructor
    }

    public static GroceryDetailFragment newInstance(Context context, Item item) {
        GroceryDetailFragment groceryDetailFragment = new GroceryDetailFragment();
        Bundle args = new Bundle();
        mContext = context;
        args.putParcelable("item", Parcels.wrap(item));
        groceryDetailFragment.setArguments(args);
        return groceryDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItem = Parcels.unwrap(getArguments().getParcelable("item"));
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_detail, container, false);
        ButterKnife.bind(this, view);

        mNameTextView.setText(mItem.getItemName());
        mQuantityTextView.setText("x " + mItem.getItemQuantity());
        mNotesTextView.setText(mItem.getItemNotes());

        mUpdateFab.setOnClickListener(this);
        mDeleteFab.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateFab:
                break;
            case R.id.deleteFab:
                openDeleteDialog();
                break;
        }
    }

    private void openDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Item To List");
        builder.setMessage("Are you sure you want to delete this item FOREVER?");
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItemFromFirebase();
                Toast.makeText(mContext.getApplicationContext(), "Deleted forEVER", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext.getApplicationContext(), "Phew! That was close!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    public void deleteItemFromFirebase() {
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        Firebase savedItemRef = new Firebase(Constants.FIREBASE_SAVED_ITEM_URL).child(userUid);
        Intent intent = new Intent(getActivity(), GroceryActivity.class);
        getActivity().startActivity(intent);

        savedItemRef.removeValue();
    }
}