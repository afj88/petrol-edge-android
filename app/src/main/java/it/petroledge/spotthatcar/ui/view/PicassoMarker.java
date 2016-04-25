package it.petroledge.spotthatcar.ui.view;

/**
 * Created by friz on 16/04/16.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import it.petroledge.spotthatcar.App;
import it.petroledge.spotthatcar.R;

public class PicassoMarker implements Target {
    Marker mMarker;

    public PicassoMarker(Marker marker) {
        mMarker = marker;
    }

    @Override
    public int hashCode() {
        return mMarker.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PicassoMarker) {
            Marker marker = ((PicassoMarker) o).mMarker;
            return mMarker.equals(marker);
        } else {
            return false;
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Bitmap bmp = getMarkerBitmapFromView(bitmap);
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    private Bitmap getMarkerBitmapFromView(Bitmap bmp1) {

        Bitmap bmp2 = BitmapFactory.decodeResource(App.getAppContext().getResources() , R.drawable.bubble_extra_small);

        Bitmap bmOverlay = Bitmap.createBitmap(bmp2.getWidth(), bmp2.getHeight(), bmp2.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        Bitmap resized = getResizedBitmap(bmp1, bmp2.getWidth() * 0.83, bmp2.getHeight() * 0.7);

        Matrix tMatrix = new Matrix();
        tMatrix.setTranslate((float)(bmp2.getWidth() * 0.088), (float)(bmp2.getHeight() * 0.096));

        canvas.drawBitmap(resized, tMatrix, null);
        canvas.drawBitmap(bmp2, new Matrix(), null);

        return bmOverlay;
    }


    private Bitmap getResizedBitmap(Bitmap bm, double newWidth, double newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        //ThumbnailUtils.extractThumbnail(bm, width, height,  )

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }
}