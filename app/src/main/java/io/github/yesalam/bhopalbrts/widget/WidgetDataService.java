package io.github.yesalam.bhopalbrts.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.data.BusDataContract;
import io.github.yesalam.bhopalbrts.util.Util;

/**
 * Created by yesalam on 4/11/16.
 */

public class WidgetDataService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockQuoteWidgetFactory(getApplicationContext(),intent);
    }

    public class StockQuoteWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

        private Cursor mCursor;
        private Context mContext;
        private int mWidgetId;

        public StockQuoteWidgetFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
            this.mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        @Override
        public void onCreate() {
            mCursor = mContext.getContentResolver().query(BusDataContract.ALLROUTES.BASE_URI,
                   null,
                   null,
                    null,
                    null);
        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) {
                mCursor.close();
            }
            mCursor = mContext.getContentResolver().query(BusDataContract.ALLROUTES.BASE_URI,
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        public void onDestroy() {
            if (mCursor != null) {
                mCursor.close();
            }
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_route_fragment);

            if (mCursor.moveToPosition(position)) {
                String name = mCursor.getString(1);
                String ends = mCursor.getString(2) ;
                String stop_count = mCursor.getString(3) ;
                remoteViews.setTextViewText(R.id.route_id, name);
                remoteViews.setTextViewText(R.id.route_leafs, ends );
                remoteViews.setTextViewText(R.id.route_stop_no,stop_count);

                String[] leafs = ends.split(" To ");

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(Util.BUS,name) ;
                fillInIntent.putExtra(Util.ORIGIN,leafs[0]) ;
                fillInIntent.putExtra(Util.DESTINATION,leafs[1]);
                // fillInIntent.setData(weatherUri);
                remoteViews.setOnClickFillInIntent(R.id.list_route_fragment_wrapper, fillInIntent);
            }

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
