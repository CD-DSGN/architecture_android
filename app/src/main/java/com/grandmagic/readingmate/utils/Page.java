package com.grandmagic.readingmate.utils;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/3/23.
 */

public class Page {
    public List list;
    public int page_size;
    public int cur_page = 1;
    public int total_num = -1;

    public Page(List list, int size) {
        this.list = list;
        this.page_size = size;
    }

    public void refresh(List al) {
        list.clear();
        list.addAll(al);
        cur_page = list.size() / page_size + 1;
    }

    public void more(List al) {
        if (al == null) {
            return;
        }

        if (list.size()%total_num == 0) {
            list.addAll(al);
        }else{
            int last_num = list.size()%page_size;
            List sub = al.subList(last_num, al.size());
            list.addAll(sub);
        }
        cur_page = list.size() / page_size + 1;
    }

    public boolean hasMore() {
        return !(list.size() == total_num);
    }

    public void delete(int index) {
        list.remove(index);
        cur_page = list.size() / page_size + 1;
        total_num--;
    }

}
