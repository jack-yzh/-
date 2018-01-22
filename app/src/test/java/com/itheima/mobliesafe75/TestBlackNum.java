package com.itheima.mobliesafe75;

import android.test.AndroidTestCase;

import com.itheima.mobliesafe75.db.dao.BlackNumOpenHelper;

/**
 * Created by jack_yzh on 2018/1/20.
 */

public class TestBlackNum extends AndroidTestCase {
    public void testBlackNumOpenHelper(){
        BlackNumOpenHelper blackNumOpenHelper = new BlackNumOpenHelper(getContext());
        blackNumOpenHelper.getReadableDatabase();
    }
}
