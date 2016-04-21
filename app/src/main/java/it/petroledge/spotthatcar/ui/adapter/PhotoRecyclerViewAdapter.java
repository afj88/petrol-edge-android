package it.petroledge.spotthatcar.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import it.petroledge.spotthatcar.R;
import it.petroledge.spotthatcar.entity.CarEntity;
import it.petroledge.spotthatcar.ui.fragment.GalleryFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CarEntity} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.GalleryPhotoViewHolder> {

    private List<CarEntity> mPhotos;
    private final OnListFragmentInteractionListener mListener;

    public PhotoRecyclerViewAdapter(List<CarEntity> items, OnListFragmentInteractionListener listener) {
        mPhotos = items;
        mListener = listener;
    }

    @Override
    public GalleryPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_photo, parent, false);
        return new GalleryPhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryPhotoViewHolder holder, int position) {
        holder.mItem = mPhotos.get(position);
        Picasso.with((Context)mListener).load(mPhotos.get(position).getImageURI()).into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public class GalleryPhotoViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView mImageView;
        public CarEntity mItem;

        public GalleryPhotoViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.galleryImageView);
        }
    }
}
