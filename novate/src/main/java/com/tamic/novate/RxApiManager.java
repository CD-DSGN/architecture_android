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

import java.util.Iterator;
import java.util.Vector;

import rx.Subscription;

/**
 * Created by Tamic on 2017-01-16.
 */

public class RxApiManager implements RxActionManager<Object> {

    private static RxApiManager sInstance = null;

    private Vector<Subscription> mSubscriptions;

    public static RxApiManager get() {

        if (sInstance == null) {
            synchronized (RxApiManager.class) {
                if (sInstance == null) {
                    sInstance = new RxApiManager();
                }
            }
        }
        return sInstance;
    }


    private RxApiManager() {
        mSubscriptions = new Vector<>();
    }


    @Override
    public void add(Subscription subscription) {
        if (mSubscriptions == null) {
            return;
        }
        mSubscriptions.add(subscription);
    }


    @Override
    public void remove(Subscription subscription) {
        if (mSubscriptions == null) {
            return;
        }
        if (!mSubscriptions.isEmpty()) {
            mSubscriptions.remove(subscription);
        }
    }

    public void removeAll() {
        if (mSubscriptions == null) {
            return;
        }

        if (!mSubscriptions.isEmpty()) {
            mSubscriptions.clear();
        }
    }


    @Override
    public void cancel(Subscription subscription) {
        if (mSubscriptions == null) {
            return;
        }

        if (mSubscriptions.isEmpty()) {
            return;
        }
        if (!mSubscriptions.contains(subscription)) {
            return;
        }
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            mSubscriptions.remove(subscription);
        }
    }


    @Override
    public void cancelAll() {
        if (mSubscriptions == null) {
            return;
        }

        if (mSubscriptions.isEmpty()) {
            return;
        }

        //遍历删除,用iterator才不会有问题
        Iterator<Subscription> iter = mSubscriptions.iterator();
        while (iter.hasNext()) {
            Subscription subscription = iter.next();
            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
            iter.remove();
        }
    }

}