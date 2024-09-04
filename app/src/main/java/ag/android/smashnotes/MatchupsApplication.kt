package ag.android.smashnotes

import ag.android.smashnotes.data.Graph
import android.app.Application

class MatchupsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}