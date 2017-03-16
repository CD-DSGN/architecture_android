/*
 *    Copyright (C) 2016 Tamic
 *
 *    link :https://github.com/Tamicer/Novate
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.tamic.novate;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Tamic on 2017-01-16.
 */

public class RxApiManager {

    Vector<Subscriber> mSubscribers;

    public RxApiManager() {
        this.mSubscribers = new Vector<>();
    }

    private static final String TAG = "RxApiManager";
    public void add(Subscriber mSubscriber) {
        Log.d(TAG, "add() called with: mSubscriber = [" + mSubscriber + "]");
        mSubscribers.add(mSubscriber);

    }

    public void remove(Subscriber mSubscriber) {
        Log.d(TAG, "remove() called with: mSubscriber = [" + mSubscriber + "]");

        if (mSubscribers.contains(mSubscriber)) {
            if (!mSubscriber.isUnsubscribed())
                mSubscriber.unsubscribe();
            mSubscribers.remove(mSubscriber);
        }
    }

    public void removeAll() {
        Log.d(TAG, "removeAll() called");

        if (mSubscribers != null && !mSubscribers.isEmpty()) {
            for (Subscriber mSubscriber :
                    mSubscribers) {
                if (!mSubscriber.isUnsubscribed()) {
                    mSubscriber.unsubscribe();
                    mSubscribers.remove(mSubscriber);
                    Log.d(TAG, "removeAll() called");
                }
            }
        }
    }

}