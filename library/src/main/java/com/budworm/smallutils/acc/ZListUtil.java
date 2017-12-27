package com.budworm.smallutils.acc;


import java.util.Iterator;
import java.util.List;

/**
 * author zx
 * version 1.0
 * since 2017/1/3 14:40
 */
public class ZListUtil {
    public final String TAG = ZListUtil.class.getSimpleName();

    /**
     * iterator删除数据
     * author zx
     * version 1.0
     * since 2017/2/17 11:11
     */
    public static void iteratorDelete(List<Integer> list, int position) {
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            int i = it.next();
            if (i == position) {
                it.remove();
            }
        }
    }


    /**
     * 获取下一个信息
     * author zx
     * version 1.0
     * since 2017/1/18 18:17
     */
    public static int getListNext(List list, int position) {
        if (null != list) {
            int size = list.size();
            if (size > 0) {
                if (position > -1) {
                    int next = position + 1;
                    if (next < size) {
                        position++;
                    } else {
                        position = 0;
                    }
                } else {
                    position = 0;
                }
                return position;
            }
        }
        return 0;
    }


    /**
     * 获取上一个信息
     * author zx
     * version 1.0
     * since 2017/1/18 18:17
     */
    public static int getListLast(List list, int position) {
        if (null != list) {
            int size = list.size();
            if (size > 0) {
                if (position > -1 && position < size) {
                    int last = position - 1;
                    if (last > -1) {
                        position--;
                    } else {
                        position = size - 1;
                    }
                } else {
                    position = 0;
                }
                return position;
            }
        }
        return 0;
    }

}
