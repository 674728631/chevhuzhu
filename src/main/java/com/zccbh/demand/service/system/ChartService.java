package com.zccbh.demand.service.system;

import com.zccbh.demand.mapper.business.MaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.customer.WechatLoginMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.mapper.order.OrderMapper;
import com.zccbh.util.base.MapUtil;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ChartService {
	@Autowired
	private UserCustomerMapper customerMapper;

	@Autowired
	private CarMapper carMapper;

    @Autowired
    private RecordRechargeMapper rechargeMapper;

    @Autowired
    private MaintenanceshopMapper maintenanceshopMapper;

	@Autowired
	private EventMapper eventMapper;

	@Autowired
	private WechatLoginMapper wechatLoginMapper;

	@Autowired
	private OrderMapper orderMapper;

	/**
	 * 查询注册用户数据
	 * @throws Exception
	 */
	public Map<String,Object> register(Map<String, Object> paramModelMap) throws Exception{
		List<Map<String,Object>> chartList = customerMapper.chartRegister(paramModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map register = chartList.get(i);
			datelist[i] = register.get("datelist").toString();
			numlist[i] = new Integer(register.get("num").toString());
			countNumber += numlist[i];
		}

        // 获取渠道拉新的数据
//        List<Map<String,Object>> channelList = customerMapper.chartRegisterForChannel(paramModelMap);
//        String[] channelNames = new String[channelList.size()];
//        Integer[] channelNums = new Integer[channelList.size()];
//        Integer channelTotal = 0;
//
//        for (int i=0;i<channelList.size();i++) {
//            Map item = channelList.get(i);
//            channelNames[i] = item.get("names").toString();
//            channelNums[i] = Integer.parseInt(item.get("nums").toString());
//            channelTotal += channelNums[i];
//        }

		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);


//        result.put("channelNames",channelNames);
//        result.put("channelNums",channelNums);
//        result.put("channelTotal",channelTotal);
		return result;
	}

	/**
	 * 查询微信访问数据
	 * @throws Exception
	 */
	public Map<String,Object> view(Map<String, Object> paramModelMap) throws Exception{
		List<Map<String,Object>> chartList = wechatLoginMapper.chartView(paramModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map register = chartList.get(i);
			datelist[i] = register.get("datelist").toString();
			numlist[i] = Constant.toEmpty(register.get("number"))?new Integer(register.get("number").toString()):0;
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 查询保障中车辆数据
	 * @throws Exception
	 */
	public Map<String,Object> guarantee(Map<String, Object> paramModelMap) throws Exception{
        List<Map<String,Object>> chartList = carMapper.chartGuarantee(paramModelMap);
        String[] datelist = new String[chartList.size()];
        Integer[] numlist = new Integer[chartList.size()];
        Integer countNumber = 0;
        for (int i=0;i<chartList.size();i++) {
            Map guarantee = chartList.get(i);
            datelist[i] = guarantee.get("datelist").toString();
            numlist[i] = new Integer(guarantee.get("num").toString());
            countNumber += numlist[i];
        }
        Map result = new HashMap();
        result.put("datelist",datelist);
        result.put("numlist",numlist);
        result.put("countNumber",countNumber);
        return result;
	}

	/**
	 * 查询商家数据
	 * @throws Exception
	 */
	public Map<String,Object> shop(Map<String, Object> paramModelMap) throws Exception{
		List<Map<String,Object>> chartList = maintenanceshopMapper.chartShop(paramModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map shop = chartList.get(i);
			datelist[i] = shop.get("datelist").toString();
			numlist[i] = new Integer(shop.get("num").toString());
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 查询渠道数据
	 * @throws Exception
	 */
	public Map<String,Object> channel(Map<String, Object> paramModelMap) throws Exception{
		List<Map<String,Object>> chartList = maintenanceshopMapper.chartChannel(paramModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map shop = chartList.get(i);
			datelist[i] = shop.get("datelist").toString();
			numlist[i] = new Integer(shop.get("num").toString());
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 查询理赔订单数据
	 * @throws Exception
	 */
	public Map<String,Object> event(Map<String, Object> paramModelMap) throws Exception{
		List<Map<String,Object>> chartList = eventMapper.chartEvent(paramModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map event = chartList.get(i);
			datelist[i] = event.get("datelist").toString();
			numlist[i] = new Integer(event.get("num").toString());
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 查询互助金数据
	 * @throws Exception
	 */
	public Map<String,Object> foundation(Map<String, Object> paramModelMap) throws Exception{
        List<Map<String,Object>> chartList = rechargeMapper.chartFoundation(paramModelMap);
        String[] datelist = new String[chartList.size()];
        BigDecimal[] numlist = new BigDecimal[chartList.size()];
        BigDecimal countNumber = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        for (int i=0;i<chartList.size();i++) {
            Map guarantee = chartList.get(i);
            datelist[i] = guarantee.get("datelist").toString();
            numlist[i] = new BigDecimal(guarantee.get("amt").toString());
			countNumber = countNumber.add(numlist[i]).setScale(2, RoundingMode.HALF_UP);
        }
        Map result = new HashMap();
        result.put("datelist",datelist);
        result.put("numlist",numlist);
        result.put("countNumber",countNumber);
        return result;
	}

	/**
	 * 查询待支付车辆数据
	 * @throws Exception
	 */
	public Map<String,Object> initNum(Map<String, Object> paramModelMap) throws Exception{
		List<Map<String,Object>> chartList = wechatLoginMapper.chartView(paramModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map register = chartList.get(i);
			datelist[i] = register.get("datelist").toString();
			numlist[i] = Constant.toEmpty(register.get("initNum"))?new Integer(register.get("initNum").toString()):0;
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 查询观察期车辆数据
	 * @throws Exception
	 */
	public Map<String,Object> observationNum(Map<String, Object> paramModelMap) throws Exception{
		List<Map<String,Object>> chartList = wechatLoginMapper.chartView(paramModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map register = chartList.get(i);
			datelist[i] = register.get("datelist").toString();
			numlist[i] = Constant.toEmpty(register.get("observationNum"))?new Integer(register.get("observationNum").toString()):0;
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 查询保障中车辆数据
	 * @throws Exception
	 */
	public Map<String,Object> guaranteeNum(Map<String, Object> paramModelMap) throws Exception{
		List<Map<String,Object>> chartList = wechatLoginMapper.chartView(paramModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map register = chartList.get(i);
			datelist[i] = register.get("datelist").toString();
			numlist[i] = Constant.toEmpty(register.get("guaranteeNum"))?new Integer(register.get("guaranteeNum").toString()):0;
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 查询退出车辆数据
	 * @throws Exception
	 */
	public Map<String,Object> outNum(Map<String, Object> paramModelMap) throws Exception{
		List<Map<String,Object>> chartList = wechatLoginMapper.chartView(paramModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map register = chartList.get(i);
			datelist[i] = register.get("datelist").toString();
			numlist[i] = Constant.toEmpty(register.get("outNum"))?new Integer(register.get("outNum").toString()):0;
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 查询二次充值数据
	 * @throws Exception
	 */
	public Map<String,Object> twiceRecharge(Map<String, Object> paramModelMap) throws Exception{
		List<Map<String,Object>> chartList = rechargeMapper.chartTwiceRecharge(paramModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map event = chartList.get(i);
			datelist[i] = event.get("datelist").toString();
			numlist[i] = new Integer(event.get("num").toString());
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 微信关注数统计
	 * @param parameModelMap
	 * @return
	 */
	public Map<String, Object> weChatConcerns(Map<String, Object> parameModelMap) {
		List<Map<String,Object>> chartList = wechatLoginMapper.weChatConcerns(parameModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map register = chartList.get(i);
			datelist[i] = register.get("datelist").toString();
			numlist[i] = Constant.toEmpty(register.get("numbers"))?new Integer(register.get("numbers").toString()):0;
			countNumber += numlist[i];
		}
		// 获取渠道拉新的数据
//        List<Map<String,Object>> channelList = wechatLoginMapper.weChatConcernsForChannel(parameModelMap);
//        String[] channelNames = new String[channelList.size()];
//        Integer[] channelNums = new Integer[channelList.size()];
//        Integer channelTotal = 0;
//
//        for (int i=0;i<channelList.size();i++) {
//            Map item = channelList.get(i);
//            channelNames[i] = Constant.toEmpty(item.get("names")) ? item.get("names").toString():"自然用户";
//            channelNums[i] = Integer.parseInt(item.get("nums").toString());
//            channelTotal += channelNums[i];
//        }

		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);

//        result.put("channelNames",channelNames);
//        result.put("channelNums",channelNums);
//        result.put("channelTotal",channelTotal);
		return result;
	}

	/**
	 * 互助理赔审核不通过
	 * @param parameModelMap
	 * @return
	 */
	public Map<String, Object> eventApplyFail(Map<String, Object> parameModelMap) {
		parameModelMap.put("statusEvent",2);
		List<Map<String,Object>> chartList = eventMapper.eventApplyFail(parameModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map event = chartList.get(i);
			datelist[i] = event.get("datelist").toString();
			numlist[i] = new Integer(event.get("numbers").toString());
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 互助理赔审核通过
	 * @param parameModelMap
	 * @return
	 */
	public Map<String, Object> eventApplySuccess(Map<String, Object> parameModelMap) {
		List<Map<String,Object>> chartList = eventMapper.eventApplySuccess(parameModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map event = chartList.get(i);
			datelist[i] = event.get("datelist").toString();
			numlist[i] = new Integer(event.get("numbers").toString());
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 申请保险理赔的统计
	 * @param parameModelMap
	 * @return
	 */
	public Map<String, Object> order(Map<String, Object> parameModelMap) {
		List<Map<String,Object>> chartList = orderMapper.orderCount(parameModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map event = chartList.get(i);
			datelist[i] = event.get("datelist").toString();
			numlist[i] = new Integer(event.get("numbers").toString());
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 保险理赔，审核不通过统计
	 * @param parameModelMap
	 * @return
	 */
	public Map<String, Object> orderApplyFail(Map<String, Object> parameModelMap) {
		List<Map<String,Object>> chartList = orderMapper.orderApplyFail(parameModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map event = chartList.get(i);
			datelist[i] = event.get("datelist").toString();
			numlist[i] = new Integer(event.get("numbers").toString());
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 保险理赔，审核通过
	 * @param parameModelMap
	 * @return
	 */
	public Map<String, Object> orderApplySuccess(Map<String, Object> parameModelMap) {
		List<Map<String,Object>> chartList = orderMapper.orderApplySuccess(parameModelMap);
		String[] datelist = new String[chartList.size()];
		Integer[] numlist = new Integer[chartList.size()];
		Integer countNumber = 0;
		for (int i=0;i<chartList.size();i++) {
			Map event = chartList.get(i);
			datelist[i] = event.get("datelist").toString();
			numlist[i] = new Integer(event.get("numbers").toString());
			countNumber += numlist[i];
		}
		Map result = new HashMap();
		result.put("datelist",datelist);
		result.put("numlist",numlist);
		result.put("countNumber",countNumber);
		return result;
	}

	/**
	 * 渠道拉新统计
	 * @param parameModelMap
	 * @return
	 */
    public Map<String, Object> channelData(Map<String, Object> parameModelMap) {
		List<Map<String,Object>> carList = carMapper.channelData(parameModelMap);
//		// 获取邀请活动的数据
//		Map<String, Object> invitedMap = carMapper.countInvitedNumbers(parameModelMap);
//		Long invitedNums = 0L;
//		if (invitedMap != null && invitedMap.get("numbers") != null){
//			invitedNums = (Long)invitedMap.get("numbers");
//			if (invitedNums.compareTo(0L) > 0){
//				carList.add(MapUtil.build().put("source","邀请活动").put("numbers",invitedNums).over());
//			}
//		}
//		// 计算自然用户的数据
//		Long naturalNums = 0L;
//		Long notChannelDataNums = null;
//		if (carList != null && carList.size() > 0){
//			Iterator<Map<String, Object>> iterator = carList.iterator();
//			while (iterator.hasNext()){
//				Map<String, Object> next = iterator.next();
//				if ("notChannelData".equals(next.get("source"))){
//					notChannelDataNums = (Long)next.get("numbers");
//					iterator.remove();
//				}
//			}
//		}
//		if (notChannelDataNums != null && notChannelDataNums > invitedNums){
//			naturalNums = notChannelDataNums - invitedNums;
//			carList.add(MapUtil.build().put("source","自然用户").put("numbers",naturalNums).over());
//		}
		Long count = 0L;
		// 统计总数
		if (carList != null && carList.size() > 0){
			for (Map<String,Object> item : carList){
				count += (Long) item.get("numbers");
			}
		}
		// 计算比例
		DecimalFormat df=new DecimalFormat("0.000");
		if (carList != null && carList.size() > 0){
			for (Map<String,Object> item : carList){
				Long numbers = (Long) item.get("numbers");
				BigDecimal ratio = new BigDecimal(numbers).divide(new BigDecimal(count),3,BigDecimal.ROUND_HALF_UP);
//				BigDecimal ratio = new BigDecimal(df.format((float) numbers / count));
				item.put("ratio", ratio.multiply(new BigDecimal(100)));
			}
		}
		// 对carList排序
		if (carList != null && carList.size() > 0){
			Collections.sort(carList, new Comparator<Map<String, Object>>() {
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					Long map1value = (Long) o1.get("numbers");
					Long map2value = (Long) o2.get("numbers");
					return map2value.compareTo(map1value);
				}
			});
		}

		Map<String, Object> result = new HashMap<>();
		result.put("data",carList);
		result.put("total", count);
		result.put("countContent", "总数(观察期)");
		return result;
    }
}
