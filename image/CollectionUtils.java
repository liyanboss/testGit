package com.cccj.robot.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

    public static <T> List<List<T>> buildAllCombination(List<T> nList, Integer mNum) {
        List<List<T>> result = new ArrayList<List<T>>();
        List<T> mList = new ArrayList<T>(mNum);
        buildCombination(nList, mNum, 0, mList, 0, result);
        return result;
    }

    private static <T> void buildCombination(List<T> nList, Integer mNum, int begin, List<T> mList, int index, List<List<T>> result) {
        if (mNum == 0) {
            result.add(new ArrayList<T>(mList));
        } else {
            for (int i = begin; i < nList.size(); i++) {
                if (index >= mList.size()) {
                    mList.add(index, nList.get(i));
                } else {
                    mList.set(index, nList.get(i));
                }
                buildCombination(nList, mNum - 1, i + 1, mList, index + 1, result);
            }
        }
    }

}


// 秒杀排列组合（下）————组合篇   https://blog.csdn.net/zmazon/article/details/8315418

