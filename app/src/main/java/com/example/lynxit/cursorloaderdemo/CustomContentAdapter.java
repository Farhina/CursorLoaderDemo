package com.example.lynxit.cursorloaderdemo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by farhina on 21/01/2016.
 */
public class CustomContentAdapter extends BaseAdapter {
    Cursor cursor;
    Context mContext;
    LayoutInflater inflater;

    public CustomContentAdapter(Context context, Cursor cursor)
    {
        mContext = context;
        this.cursor=cursor;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        cursor.moveToPosition(position);
        if(view == null)
        {
            view=inflater.inflate(R.layout.customcontentadapter,parent,false);
            holder = new Holder();
            holder.tvContactName = (TextView) view.findViewById(R.id.tvContactName);
            holder.tvContactNumber=(TextView) view.findViewById(R.id.tvContactNumber);
            holder.ivContactImage=(ImageView) view.findViewById(R.id.ivContactImage);
            view.setTag(holder);
        }else
        {
            holder = (Holder) view.getTag();
        }
        holder.tvContactName.setText(cursor.getString(cursor.getColumnIndex
                (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
        holder.tvContactNumber.setText(cursor.getString(cursor.getColumnIndex
                (ContactsContract.CommonDataKinds.Phone.NUMBER)));
        String imageUri = cursor.getString(cursor.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),
                    Uri.parse(imageUri));
            holder.ivContactImage.setImageBitmap(bitmap);
            scaleImage(holder.ivContactImage);
        }catch(Exception e)
        {
            //holder.ivContactImage.setImageResource(R.drawable);
            scaleImage(holder.ivContactImage);
        }
        return view;
    }

    class Holder{
        TextView tvContactName, tvContactNumber;
        ImageView ivContactImage;
    }

    private void scaleImage(ImageView imageView)
    {
        Drawable drawing = imageView.getDrawable();
        if(drawing==null)
        {

        }
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(50);

        float xScale = ((float) bounding)/width;
        float yScale = ((float)bounding)/height;
        float scale = (xScale<=yScale)?xScale:yScale;

        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);

        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        width=scaledBitmap.getWidth();
        height=scaledBitmap.getHeight();
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        imageView.setImageDrawable(result);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.width = width;
        params.height=height;
        imageView.setLayoutParams(params);

    }
    private int dpToPx(int dp)
    {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

}
