package io.github.yesalam.bhopalbrts.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import io.github.yesalam.bhopalbrts.datamodel.Stop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yesalam on 22-08-2015.
 */
public class AssetDatabaseHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = BusDataContract.DB_NAME ;
    private static final int DB_VERSION = 2 ;

    public AssetDatabaseHelper(Context context) {
        super(context,DB_NAME, null,DB_VERSION );
    }


}
