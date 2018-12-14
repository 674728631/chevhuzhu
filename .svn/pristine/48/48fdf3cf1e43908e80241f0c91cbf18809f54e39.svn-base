package com.zccbh.util.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/** 
 * @ClassName: DrawLotteryUtil 
 * @Description: TODO(返回奖品下标) 
 * @author xiaowuge
 * @date 2018年9月28日 下午4:31:35 
 *  
 */
public class DrawLotteryUtil {
	
	public static int drawGift(List<Map<String, Object>> giftList){

        if(null != giftList && giftList.size()>0){
            List<Double> orgProbList = new ArrayList<Double>(giftList.size());
            for(Map<String, Object> gift:giftList){
                //按顺序将概率添加到集合中
                orgProbList.add(Double.valueOf(gift.get("prob").toString()));
            }

            return draw(orgProbList);

        }
        return -1;
    }

    public static int draw(List<Double> giftProbList){

        List<Double> sortRateList = new ArrayList<Double>();

        // 计算概率总和
        Double sumRate = 0D;
        for(Double prob : giftProbList){
            sumRate += prob;
        }

        if(sumRate != 0){
            double rate = 0D;   //概率所占比例
            for(Double prob : giftProbList){
                rate += prob;   
                // 构建一个比例区段组成的集合(避免概率和不为1)
                sortRateList.add(rate / sumRate);
            }

            // 随机生成一个随机数，并排序
            double random = Math.random();
            sortRateList.add(random);
            Collections.sort(sortRateList);

            // 返回该随机数在比例集合中的索引
            return sortRateList.indexOf(random);
        }


        return -1;
    }

}
