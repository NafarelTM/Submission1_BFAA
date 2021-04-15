package com.dicoding.submissionfundamental1.widget

import android.content.Context
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submissionfundamental1.R
import com.dicoding.submissionfundamental1.database.CursorHelper
import com.dicoding.submissionfundamental1.database.DatabaseContract
import com.dicoding.submissionfundamental1.model.User

internal class StackRemoteViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private val favItem = ArrayList<User>()
    private var cursor: Cursor? = null

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        cursor?.close()

        val identity = Binder.clearCallingIdentity()
        cursor = mContext.contentResolver.query(
            DatabaseContract.FavColumns.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        favItem.addAll(CursorHelper.toArrayList(cursor))
        Binder.restoreCallingIdentity(identity)
    }

    override fun onDestroy() {
        cursor?.close()
        favItem.clear()
    }

    override fun getCount(): Int = favItem.size

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(mContext.packageName, R.layout.item_widget)
        val image = Glide.with(mContext)
            .asBitmap()
            .load(favItem[position].avatar)
            .apply(RequestOptions().override(250, 110))
            .submit()
            .get()
        views.setImageViewBitmap(R.id.img_view, image)
        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}