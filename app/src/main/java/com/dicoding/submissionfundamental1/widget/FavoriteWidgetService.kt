package com.dicoding.submissionfundamental1.widget

import android.content.Intent
import android.widget.RemoteViewsService

class FavoriteWidgetService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext)
    }
}