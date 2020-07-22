package com.cuongtt.homnayangi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.cuongtt.homnayangi.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Activity context;
    private ImageView img;
    public ArrayList<Bitmap> imgArray;


    public ImageAdapter(Activity context, ArrayList imgArray) {
        this.context = context;
        this.imgArray = imgArray;
    }

    @Override
    public int getCount() {
        return imgArray.size();
    }

    @Override
    public Object getItem(int i) {
        return imgArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = context.getLayoutInflater();

        view = inflater.inflate(R.layout.item_grid_recipe_image, null);

        img = view.findViewById(R.id.item_image);
        img.setImageBitmap(imgArray.get(i));

        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Xoá hình ảnh")
                        .setMessage("Bạn có chắc muốn xoá hình ảnh này?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                imgArray.remove(i);
                                notifyDataSetChanged();
                                System.out.println(imgArray.isEmpty());
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });



        return view;
    }
}
