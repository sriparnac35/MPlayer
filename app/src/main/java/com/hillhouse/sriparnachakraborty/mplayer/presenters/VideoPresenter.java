package com.hillhouse.sriparnachakraborty.mplayer.presenters;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.hillhouse.sriparnachakraborty.mplayer.models.Media;
import com.hillhouse.sriparnachakraborty.mplayer.models.Video;

import java.util.LinkedList;

/**
 * Created by sriparnachakraborty on 30/03/16.
 */
public class VideoPresenter implements MediaPresenter, LoaderManager.LoaderCallbacks<Cursor> {

    private FragmentActivity mActivity = null;
    private Callback mCallback = null;

    private static final int VIDEO_LOADER_ID = 10;

    public VideoPresenter(FragmentActivity activity){
        mActivity = activity;
    }


    @Override
    public void getMediaItems(Callback callback) {
        mCallback = callback;
        mActivity.getSupportLoaderManager().initLoader(VIDEO_LOADER_ID, null, this);
    }

    @Override
    public void onMediaItemClicked(Media media) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        if(id == VIDEO_LOADER_ID){
            CursorLoader loader = new CursorLoader(mActivity);
            loader.setUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            loader.setProjection(null);
            loader.setSelection(null);
            loader.setSelectionArgs(null);
            loader.setSortOrder(null);
            return loader;
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        if(loader.getId() == VIDEO_LOADER_ID){
            LinkedList<Media> mediaList = new LinkedList<>();

            if(data != null && data.getCount() > 0){
                int idIndex = data.getColumnIndex(MediaStore.Video.Media._ID);
                int titleIndex = data.getColumnIndex(MediaStore.Video.Media.TITLE);
                int artistIndex = data.getColumnIndex(MediaStore.Video.Media.ARTIST);
                int pathIndex = data.getColumnIndex(MediaStore.Video.Media.DATA);

                while(data.moveToNext()){
                    long id = data.getLong(idIndex);
                    String title = data.getString(titleIndex);
                    String artist = data.getString(artistIndex);
                    String path = data.getString(pathIndex);

                    Video video = new Video(id, title, artist, path);
                    mediaList.add(video);
                }
            }
            if(mCallback != null){
                mCallback.onItemsAvailable(mediaList);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        if(loader.getId() == VIDEO_LOADER_ID){
            if(mCallback != null){
                mCallback.onItemsAvailable(new LinkedList<Media>());
            }
        }

    }
}
