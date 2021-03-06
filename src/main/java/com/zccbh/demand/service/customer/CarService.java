package com.zccbh.demand.service.customer;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.controller.quartz.ObservationJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.mapper.business.AccountMapper;
import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.InvitationCustomerMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.mapper.merchants.CbhCarMapper;
import com.zccbh.demand.mapper.merchants.CbhUserBusinessMapper;
import com.zccbh.demand.mapper.user.UserAdminMapper;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.demand.pojo.merchants.CbhCar;
import com.zccbh.demand.pojo.user.Car;
import com.zccbh.demand.service.basic.DictionaryService;
import com.zccbh.demand.service.event.EventService;
import com.zccbh.demand.service.system.CarLogService;
import com.zccbh.demand.service.system.UserCustomerLogService;
import com.zccbh.demand.service.user.UsersService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.Base64;
import com.zccbh.util.base.*;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.uploadImg.UploadFileUtil;
import net.sf.json.JSONObject;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;
import sun.tools.tree.SuperExpression;
import sun.tools.tree.ThisExpression;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CarService {
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private CbhCarMapper cbhCarMapper;
    @Autowired
    private UserCustomerService userCustomerService;
    @Autowired
    private UserCustomerMapper customerMapper;
    @Autowired
    private RecordRechargeMapper recordRechargeMapper;
    @Autowired
    private FoundationMapper foundationMapper;
    @Autowired
    private EventService eventService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired WeiXinUtils weiXinUtils;
    @Autowired InvitationCustomerMapper invitationCustomerMapper;
	@Autowired
	MiddleCustomerMaintenanceshopMapper middleCustomerMaintenanceshopMapper;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	@Autowired
	private UserAdminMapper adminMapper;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private CbhUserBusinessMapper businessMapper;
	@Autowired
	private CarLogService carLogService;
	@Autowired
	private UserCustomerMapper userCustomerMapper;
	@Autowired
	private UserCustomerLogService userCustomerLogService;

    /**
     * 添加车辆
     * @param paramModelMap 车辆信息
     * @return 添加结果信息
     * @throws Exception
     */
    @Transactional
    public String saveCar(Map<String, Object> paramModelMap) throws Exception {
        Map map = new HashMap();
        map.put("licensePlateNumber",paramModelMap.get("licensePlateNumber"));
        List<Map<String, Object>> carList = carMapper.findMore(map);
        if(carList.size()>0){
            return "4003";
        }
    	carMapper.saveSingle(paramModelMap);
        return "0";
    }

    public List<Map<String, Object>> findPayCar(Map<String, Object> paramModelMap) throws Exception{
    	return carMapper.findPayCar(paramModelMap);
    }

    public Map<String, Object> findOne(Map<String, Object> paramModelMap) throws Exception{
    	return carMapper.findOne(paramModelMap);
    }

    public Map<String, Object> findTotalCooperationByCustomerId(Map<String, Object> paramModelMap) throws Exception{
    	Map<String,Object> map =  carMapper.findTotalCooperationByCustomerId(paramModelMap); //查询互助金余额
    	List<Map<String,Object>> list = carMapper.findMore(paramModelMap);//用户所有车辆信息以及最后一次互助及充值情况
    	BigDecimal total = new BigDecimal(0);
    	// 循环每一辆车
    	for(Map<String,Object> m : list){
    		String timeSignout = String.valueOf(m.get("timeSignout"));
    		paramModelMap.put("time1", m.get("timeBegin"));
    		if(!timeSignout.equals("null")&&!timeSignout.equals("1111-11-11 11:11:11.0")){
				paramModelMap.put("time2", m.get("timeSignout"));
			}
    		Map<String,Object> amap =  carMapper.findCarAmtShare(paramModelMap); // 用户保障期内所有的互助分摊
    		if(amap==null){
    			continue;
    		}
    		BigDecimal a = new BigDecimal(String.valueOf(amap.get("amtShare")).equals("null")?"0":String.valueOf(amap.get("amtShare")));
    		total = total.add(a);
    	}
    	map = map==null?new HashMap<String, Object>():map;
    	map.put("expenditureCooperation", total);
    	return map;
    }

    public int updateCarAmt(Map<String, Object> map) throws Exception{
    	return carMapper.updateCarAmt(map);
    }
    public int updateCarStatus() throws Exception{
    	return carMapper.updateCarStatus();
    }

    /**
     * 修改车辆
     * @param paramModelMap 车辆信息
     * @return 修改结果信息
     * @throws Exception
     */
    @Transactional
    public String updateCar(Map<String, Object> paramModelMap) throws Exception {
        carMapper.updateModel(paramModelMap);
        return "0";
    }

    public int findCarCountByStatus(Map<String, Object> paramModelMap) throws Exception {
    	return carMapper.findCarCountByStatus(paramModelMap);
    }

    /**
     * C 端 查询车辆数据
     * @param paramModelMap 查询条件
     * @return 车辆信息
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findCarList(Map<String, Object> parameModelMap) throws Exception {
        int pageNo = Constant.toEmpty(parameModelMap.get("pageNo"))?Integer.parseInt(parameModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(parameModelMap.get("pageSize"))?Integer.parseInt(parameModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> carList = carMapper.findCarList(parameModelMap);
        PageInfo<Map<String, Object>> carInfo = new PageInfo<>(carList);
        if (carList.size() > 0) {
        	//成功邀请列表
    		List<Map<String,Object>> iList = invitationCustomerMapper.selectInvitationList(Integer.valueOf(String.valueOf(parameModelMap.get("customerId"))));
    		if (carInfo.isIsLastPage()) {
//    			Map<String, Object> car = carList.get(0);
    			Map<String, Object> firstCar = carMapper.findFirstCar(parameModelMap);
    			int a = 0;
//    			for(int i = 1; i < carList.size(); i++){
//    				Map<String, Object> map = carList.get(i);
//    				if (Integer.valueOf(car.get("id").toString()) > Integer.valueOf(map.get("id").toString())) {
//    					car = map;
//    					a = i;
//    				}
//    			}
    			for(int i=0; i<carList.size();i++){
    				Map<String, Object> map = carList.get(i);
    				if (Integer.valueOf(firstCar.get("id").toString()).compareTo(Integer.valueOf(map.get("id").toString())) == 0 ) {
    					a = i;
    					if(iList.size() >= 1){
    						firstCar.put("level", "2");
    						firstCar.put("levela", "2");
    	    				carList.set(a, firstCar);
    	    			}
					}
    			}
    			
    		}
		}
        return carInfo;
    }

	/**
	 * 后台 查询车辆数据
	 * @param paramModelMap 查询条件
	 * @return 车辆信息
	 * @throws Exception
	 */
	public PageInfo<Map<String, Object>> findCarList2(Map<String, Object> paramModelMap) throws Exception {
		int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
		int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
		int status = Constant.toEmpty(paramModelMap.get("status")) ? Integer.parseInt(paramModelMap.get("status").toString()) : 0;
		PageHelper.startPage(pageNo, pageSize);
		if (0 == status || 1 == status) { // 全部 ，待支付
			return new PageInfo<>(carMapper.findCarList2(paramModelMap));
		} else if (13 == status) { // 观察期
			return new PageInfo<>(carMapper.getStatus13Cars(paramModelMap));
		} else if (20 == status) { // 保障中
			return new PageInfo<>(carMapper.getStatus20Cars(paramModelMap));
		} else if (30 == status) { // 退出计划
			return new PageInfo<>(carMapper.getStatus30Cars(paramModelMap));
		} else {
			throw new RuntimeException("status 状态错误！");
		}
//		return new PageInfo<>(carMapper.findCarList2(paramModelMap));
	}

	/**
	 * 后台 查询车辆数据
	 * @param paramModelMap 查询条件
	 * @return 车辆信息
	 * @throws Exception
	 */
	public PageInfo<Map<String, Object>> findCarList3(Map<String, Object> paramModelMap) throws Exception {
		int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
		int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
		PageHelper.startPage(pageNo, pageSize);
		return new PageInfo<>(carMapper.findCarList3(paramModelMap));
	}

    /**
     * 查询车辆数据
     * @param paramModelMap 查询条件
     * @return 车辆信息
     * @throws Exception
     */
    public Map<String, Object> findCarDetail(Map<String, Object> paramModelMap) throws Exception {
		Map<String, Object> carDetail = carMapper.findSingle(paramModelMap);
		if(Constant.toEmpty(carDetail.get("carPhotos"))){
			carDetail.put("carPhotos",CommonField.getCarUrl((String) carDetail.get("carPhotos")));
		}
		return carDetail;
    }

    /**
     * 根据订单号查询车辆
     * @param eventNo 查询条件
     * @return 车辆信息
     * @throws Exception
     */
    public Map<String, Object> findCarByEventNo(String eventNo) throws Exception {
        return carMapper.findCarByEventNo(eventNo);
    }
    
    /**
     * 查询互助金额小于1元的车辆
     * @author xiaowuge  
     * @date 2018年9月14日  
     * @version 1.0
     */
    public List<Map<String, Object>> findOutList() throws Exception{
    	return carMapper.findOutList();
    }

    /**
     * 根据车辆id查询车辆
     * @param carId 查询条件
     * @return 车辆信息
     * @throws Exception
     */
    public Map<String, Object> findCarById(Integer carId) throws Exception {
        return carMapper.findCarById(carId);
    }
    
    /**
     * 充值结果
     * @author xiaowuge  
     * @date 2018年11月6日  
     * @version 1.0
     */
    @Transactional
    public int PayCarResult(String amountIdStr) throws Exception{
    	System.out.println("++++++++进入支付结果+++++++++");
    	try {
    		String[] amountIdArr = amountIdStr.split("\\|");
    		for(String amountId:amountIdArr){
    			Map<String, Object> carMap =  carMapper.findCarByRecordRechargeId(amountId);
    			
    			//获取用户信息
                Map<String, Object> reqMap = new HashMap<>();
                reqMap.put("id", carMap.get("customerId"));
                Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
                //用户支付
            	reqMap.clear();
            	reqMap.put("customerId", customer.get("id"));
            	reqMap.put("customerPN", customer.get("customerPN"));
            	reqMap.put("source", customer.get("source"));
            	reqMap.put("createAt", customer.get("timeJoin"));
            	reqMap.put("currentStatus", customer.get("status"));
            	reqMap.put("optTime", DateUtils.formatDate(new Date()));
            	reqMap.put("optType", 4);
            	reqMap.put("optDesc", "用户支付");
            	reqMap.put("recordTime", DateUtils.formatDate(new Date()));
            	userCustomerLogService.saveUserCustomerLog(reqMap);
            	
    			if(carMap!=null&&"2".equals(String.valueOf(carMap.get("rStatus")))){
    				String carId = String.valueOf(carMap.get("id"));
    				Map<String, Object> userMap = carMapper.selectUserByCarId(carMap);
                	SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
            		String date = time.format(new Date());
            		BigDecimal oldAmount = String.valueOf(carMap.get("amtCooperation")).equals("null")
            				?new BigDecimal(0):new BigDecimal(String.valueOf(carMap.get("amtCooperation")));
            		BigDecimal a = new BigDecimal(String.valueOf(carMap.get("amt")).equals("null")?"0":String.valueOf(carMap.get("amt")));
            		BigDecimal newAmount = oldAmount.add(a);
            	
            		Calendar calendar = Calendar.getInstance();
            		calendar.add(Calendar.YEAR, 1);
                	Map<String,Object> upmap = new HashMap<String, Object>();
            		upmap.put("carId", carId);
            		Map<String,Object> uMap = userCustomerService.findUser(upmap);
            		
            		Map<String,Object> map = new HashMap<String, Object>();
            		map.put("timeRecharge", date);
            		map.put("id", amountId);
            		map.put(CommonField.STATUS, 1);
            		map.put("description", "手机号为"+uMap.get("customerPN")+"的会员为"+uMap.get("licensePlateNumber")+"充值了"+a+"元");
            		map.put("eventType", carMap.get("amtCooperation")==null?1:2);
            		recordRechargeMapper.updateModel(map);
            		
            		//车辆充值后------>保存操作记录
                    Map<String, Object> carLogMap = new HashMap<>();
                    carLogMap.put("customerId", carMap.get("customerId"));
                    carLogMap.put("carId", carMap.get("id"));
                    carLogMap.put("optTime", map.get("timeRecharge"));
                    carLogMap.put("optType", 2);
                    carLogMap.put("optDesc", "用户车辆支付");
                    carLogMap.put("recordeTime", DateUtils.formatDate(new Date()));
                    carLogService.saveCarLog(carLogMap);
                    
                	/**/
            		String status = String.valueOf(carMap.get(CommonField.STATUS));
            		String oldStatus = status;
            		System.out.println("+++++++++++++++++oldStatus++++++++++++++"+ oldStatus);
					status = status.equals("1")?"13":status.equals("30")?"13":status;
					String typeGuarantee = "1";
					BigDecimal sum = new BigDecimal("0");
            		int f = 0;
            		try {
            			Map<String,Object> rMap = getPayAmount(carId);
            			Calendar calendar1 = Calendar.getInstance();
						sum = new BigDecimal(String.valueOf(rMap.get("amount")));
						//用户转包年
            			if(sum.compareTo(new BigDecimal("99"))>=0){
							calendar1.setTime(time.parse(String.valueOf(rMap.get("minT"))));
							calendar1.add(Calendar.YEAR, 1);
							Map<String,Object> m = new HashMap<String, Object>();
							//m.put("amtCompensation", amtCompensation);
							m.put(CommonField.STATUS, status);
							/*if(carMap.get("timeBegin")!=null){
								m.put("timeBegin", rMap.get("minT"));
								m.put("timeEnd", time.format(calendar1.getTime()));
							}*/
							if(carMap.get("timeEnd")==null){
								m.put("timeEnd", time.format(calendar1.getTime()));
							}
							typeGuarantee = "2";
							m.put("amtCooperation", newAmount);
							m.put("typeGuarantee", typeGuarantee);
							m.put("reasonSignout", "");
							m.put("timeSignout", "1111-11-11 11:11:11");
							m.put("payTime",date);
							m.put("id", carId);
							System.out.println("++++++++++"+oldStatus+"+++++++++++");
							if (oldStatus.equals("1") || oldStatus.equals("30")) {
								m.put("reJoinNum", "reJoinNum");
							}
							this.updateCar(m);
							
							/*m.put("id", rMap.get("rId"));
							m.put("flag", i);
							m= new HashMap<String, Object>();
							recordRechargeMapper.updateModel(m);*/
							f = 1;
						}
        				
					} catch (Exception e) {
						e.printStackTrace();
					}

            		if (!oldStatus.equals("1")) {
						Map<String, String> parameMap = new HashMap<>();
						parameMap.put("openid", String.valueOf(userMap.get("openId")));
						parameMap.put("amt", String.valueOf(a));
						parameMap.put("amtCooperation", String.valueOf(newAmount));
						parameMap.put("licensePlateNumber", String.valueOf(carMap.get("licensePlateNumber")));
						weiXinUtils.sendTemplate(17, parameMap);
					}
            		
					carMap.put("typeGuarantee",typeGuarantee);
            		if(String.valueOf(carMap.get(CommonField.STATUS)).equals("30")){ //已退出的车辆
        				addFoundationShowCustomer();
						Map dicMap = new HashMap();
						dicMap.put("type","observationTime");
						Map dictionary = dictionaryService.findSingle(dicMap);
						//开启定时器
						Scheduler sche = schedulerFactory.getScheduler();
						Map<String, String> dateMap = DateUtils.getDateMap(new Long(dictionary.get("value").toString()));
						Long imestamp=new Date().getTime();
						imestamp = imestamp+Long.valueOf(dictionary.get("value").toString());
						Date date1 = new Date(imestamp);
						Map<String,Object> parameterMap = new HashMap<>();
						parameterMap.put("jobName","observationJob_" + carId);
						parameterMap.put("typeGuarantee",typeGuarantee);
						parameterMap.put("carId", carId);
						parameterMap.put("openid",String.valueOf(userMap.get("openId")));
						SimpleDateFormat dateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String observationEndTime = dateTime.format(date1);
						String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
						QuartzUtils.addJob(sche,parameterMap.get("jobName").toString(), ObservationJob.class, parameterMap, cron);
						parameterMap.clear();
						parameterMap.put("observationEndTime",observationEndTime);
						parameterMap.put("id",carId);
						parameterMap.put("reJoinNum", "reJoinNum");
						updateCar(parameterMap);
						
						 //车辆进入观察期------>保存操作记录
                        Map<String, Object> carLogMap1 = new HashMap<>();
                        carLogMap1.put("customerId", carMap.get("customerId"));
                        carLogMap1.put("carId", carMap.get("id"));
                        carLogMap1.put("optTime", DateUtils.formatDate(new Date()));
                        carLogMap1.put("optType", 3);
                        carLogMap1.put("optDesc", "用户车辆进入观察期");
                        carLogMap1.put("recordeTime", DateUtils.formatDate(new Date()));
                        carLogService.saveCarLog(carLogMap1);
                        
                    	//获取用户信息
                        Map<String, Object> reqMap1 = new HashMap<>();
                        reqMap1.put("id", carMap.get("customerId"));
                        Map<String, Object> customer1 = userCustomerMapper.findUser(reqMap1);
                        //进入观察期
                        reqMap1.clear();
                        reqMap1.put("customerId", customer1.get("id"));
                        reqMap1.put("customerPN", customer1.get("customerPN"));
                        reqMap1.put("source", customer1.get("source"));
                        reqMap1.put("createAt", customer1.get("timeJoin"));
                        reqMap1.put("currentStatus", customer1.get("status"));
                        reqMap1.put("optTime", DateUtils.formatDate(new Date()));
                        reqMap1.put("optType", 5);
                        reqMap1.put("optDesc", "进入观察期");
                        reqMap1.put("recordTime", DateUtils.formatDate(new Date()));
                    	userCustomerLogService.saveUserCustomerLog(reqMap1);
						
						Map<String,Object> mp = new HashMap<>();
						mp.put("id",carId);
						BigDecimal day = new BigDecimal(dictionary.get("value").toString()).divide(new BigDecimal("86400000"), 0, RoundingMode.HALF_UP);
						Map<String,String> rmap = new HashMap<String, String>();
						rmap.put("openid", String.valueOf(userMap.get("openId")));
						rmap.put("licensePlateNumber", String.valueOf(carMap.get("licensePlateNumber")));
						rmap.put("amtCompensation", String.valueOf(carMap.get("amtCompensation"))); //互助额度
						rmap.put("day", String.valueOf(day));
						rmap.put("money",String.valueOf(a));
						weiXinUtils.sendTemplate(11, rmap);
        			}
                	if(f==0){
                		Map<String,Object> rmap = new HashMap<String, Object>();
                	//	rmap.put("timeEnd", a.compareTo(new BigDecimal("99"))==0?time.format(calendar.getTime()):"");
                		//rmap.put("amtCompensation", amtCompensation);
                		//rmap.put("timeBegin", carMap.get("timeBegin")==null?date:null);
                		System.out.println("++++++++++++"+oldStatus.equals("1"));
                		System.out.println("+++++++++进入新充值结果+++++++++++++++");
                		if (oldStatus.equals("1")) {
							rmap.put("reJoinNum", "reJoinNum");
						}
                		rmap.put("amtCooperation", newAmount);
                		rmap.put(CommonField.STATUS, status);
                		rmap.put("typeGuarantee", a.compareTo(new BigDecimal("99"))==0?2:1);
                		rmap.put("timeSignout", "1111-11-11 11:11:11");
                		rmap.put("reasonSignout", "");
                		rmap.put("id", carId);
                		rmap.put("payTime",date);
                		this.updateCar(rmap);
                		
                		if(oldStatus.equals("1")){
                			//用户车辆进入观察期------>保存车辆观察期记录
    						Map<String, Object> carLogMap2 = new HashMap<>();
    		                carLogMap2.put("customerId", carMap.get("customerId"));
    		                carLogMap2.put("carId", carMap.get("id"));
    		                carLogMap2.put("optTime", DateUtils.formatDate(new Date()));
    		                carLogMap2.put("optType", 3);
    		                carLogMap2.put("optDesc", "用户车辆进入观察期");
    		                carLogMap2.put("recordeTime", DateUtils.formatDate(new Date()));
    		                carLogService.saveCarLog(carLogMap2);	
    		                
    		                //获取用户信息
                            Map<String, Object> reqMap2 = new HashMap<>();
                            reqMap2.put("id", carMap.get("customerId"));
                            Map<String, Object> customer1 = userCustomerMapper.findUser(reqMap2);
                            //进入观察期
                            reqMap2.clear();
                            reqMap2.put("customerId", customer1.get("id"));
                            reqMap2.put("customerPN", customer1.get("customerPN"));
                            reqMap2.put("source", customer1.get("source"));
                            reqMap2.put("createAt", customer1.get("timeJoin"));
                            reqMap2.put("currentStatus", customer1.get("status"));
                            reqMap2.put("optTime", DateUtils.formatDate(new Date()));
                            reqMap2.put("optType", 5);
                            reqMap2.put("optDesc", "进入观察期");
                            reqMap2.put("recordTime", DateUtils.formatDate(new Date()));
                        	userCustomerLogService.saveUserCustomerLog(reqMap2);
                		}
                		
                	}
            		updateFoundation(a); //修改统计数据
					eventService.updateDayNumber("payNum",1); //更新支付数量
					System.out.println("sum--------------------"+sum);
					System.out.println("+++++++" + (sum.subtract(a).compareTo(new BigDecimal("0"))<=0));
					if(sum.subtract(a).compareTo(new BigDecimal("0"))<=0){
						observation(carMap);
					}
					
    			}
    		}
    		return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

    }
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public void observation(Map<String, Object> carMap)  throws Exception{
    	System.out.println("+++++++++++进入定时器设置+++++++++++++++");
		logger.info("{}车辆创建进入保障中定时器设置+++++++++++++++++++++++++++++++++",carMap);
		//创建定时任务，指定时间后进入保障期
		String carId = String.valueOf(carMap.get("id"));
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = time.format(new Date());
		Long imestamp=new Date().getTime();

		Map<String,Object> map = new HashMap<>();
		map.put("jobName","observationJob_" + carId);
		map.put("carId",carId);
		map.put("typeGuarantee", carMap.get("typeGuarantee"));
		map.put("openid", carMap.get(CommonField.OPEN_ID));
		map.put("content", "爱车" + carMap.get("licensePlateNumber") + "加入车V互助成功，您爱车正享受互助保障中。");
		map.put("keyword1", "审核通过");
		map.put("keyword2", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
		map.put("url", Constant.toReadPro("realURL")+"hfive/view/rule_photo2.html");
		Scheduler sche = schedulerFactory.getScheduler();
		QuartzUtils.removeJob(sche,map.get("jobName").toString());
		//从数据库查询定时时间
		Map dicMap = new HashMap();
		dicMap.put("type","observationTime");
		Map dictionary = dictionaryService.findSingle(dicMap);
		//开启定时器
		Map<String, String> dateMap = DateUtils.getDateMap(new Long(dictionary.get("value").toString()));

		imestamp = imestamp+Long.valueOf(dictionary.get("value").toString());
		Date date1 = new Date(imestamp);
		String observationEndTime = time.format(date1);
		String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
		map.put("cron",cron);
		QuartzUtils.addJob(sche,map.get("jobName").toString(), ObservationJob.class, map, cron);

		usersService.addAmtCompensationByInvitation(String.valueOf(carMap.get("customerId")));
		eventService.updateDayNumber("observationNum",1);

		map.clear();
		map.put("observationEndTime",observationEndTime);
		map.put("id",carId);
		//进入观察期次数(传参数即可，sql自加)
		this.updateCar(map);

		try {
			// 没有openid就不发送微信推送
			if(null==carMap.get(CommonField.OPEN_ID) || "".equals(carMap.get(CommonField.OPEN_ID)))
				return;
			map.clear();
			map.put("id",carId);
			Map<String, Object> m = carMapper.findCar(map);
			BigDecimal day = new BigDecimal(dictionary.get("value").toString()).divide(new BigDecimal("86400000"), 0, RoundingMode.HALF_UP);
			Map<String,String> rmap = new HashMap<String, String>();
			rmap.put("openid", String.valueOf(carMap.get(CommonField.OPEN_ID)));
			rmap.put("licensePlateNumber", String.valueOf(carMap.get("licensePlateNumber")));
			rmap.put("amtCompensation", String.valueOf(carMap.get("amtCompensation"))); //互助额度
			rmap.put("day", String.valueOf(day));
			if (Constant.toEmpty(carMap.get("inviterAmount"))){
				rmap.put("money",String.valueOf(carMap.get("inviterAmount")));
			}else {
				rmap.put("money", m.get("amtCooperation").toString());
			}			
			weiXinUtils.sendTemplate(11, rmap);
			logger.info("车辆创建进入保障中定时器创建成功+++++++++++++++++++++++++++++++++");
		}catch (Exception e){
			logger.error("",e);
		}
	}
    
    /**
     * 重新激活添加定时器
     * @author xiaowuge  
     * @date 2018年10月8日  
     * @version 1.0
     */
    public void oldCarObservation(Map<String, Object> carMap)  throws Exception{
		//创建定时任务，指定时间后进入保障期
		String carId = String.valueOf(carMap.get("id"));
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = time.format(new Date());
		Long imestamp=new Date().getTime();

		Map<String,Object> map = new HashMap<>();
		map.put("jobName","observationJob_" + carId);
		map.put("carId",carId);
		map.put("typeGuarantee", carMap.get("typeGuarantee"));
		map.put("openid", carMap.get(CommonField.OPEN_ID));
		map.put("content", "爱车" + carMap.get("licensePlateNumber") + "加入车V互助成功，您爱车正享受互助保障中。");
		map.put("keyword1", "审核通过");
		map.put("keyword2", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
		map.put("url", Constant.toReadPro("realURL")+"hfive/view/rule_photo2.html");
		Scheduler sche = schedulerFactory.getScheduler();
		QuartzUtils.removeJob(sche,map.get("jobName").toString());
		//从数据库查询定时时间
		Map dicMap = new HashMap();
		dicMap.put("type","observationTime");
		Map dictionary = dictionaryService.findSingle(dicMap);
		//开启定时器
		Map<String, String> dateMap = DateUtils.getDateMap(new Long(dictionary.get("value").toString()));

		imestamp = imestamp+Long.valueOf(dictionary.get("value").toString());
		Date date1 = new Date(imestamp);
		String observationEndTime = time.format(date1);
		String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
		QuartzUtils.addJob(sche,map.get("jobName").toString(), ObservationJob.class, map, cron);

		map.clear();
		map.put("observationEndTime",observationEndTime);
		map.put("id",carId);
		this.updateCar(map);

		try {
			map.clear();
			map.put("id",carId);
			Map<String, Object> m = carMapper.findCar(map);
			BigDecimal day = new BigDecimal(dictionary.get("value").toString()).divide(new BigDecimal("86400000"), 0, RoundingMode.HALF_UP);
			Map<String,String> rmap = new HashMap<String, String>();
			rmap.put("openid", String.valueOf(carMap.get(CommonField.OPEN_ID)));
			rmap.put("licensePlateNumber", String.valueOf(carMap.get("licensePlateNumber")));
			rmap.put("amtCompensation", String.valueOf(carMap.get("amtCompensation"))); //互助额度
			rmap.put("day", String.valueOf(day));
			if (Constant.toEmpty(carMap.get("inviterAmount"))){
				rmap.put("money",String.valueOf(carMap.get("inviterAmount")));
			}else {
				rmap.put("money", m.get("amtCooperation").toString());
			}
			weiXinUtils.sendTemplate(11, rmap);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
    
    public String hideStr(String licensePlateNumber){
    	if(licensePlateNumber.length()>6){
    		return licensePlateNumber.substring(0, 2)+"***"+licensePlateNumber.substring(licensePlateNumber.length()-2);
    	}
    	return licensePlateNumber;
    }
    /**
     * 累加用户
     * @author xiaowuge  
     * @date 2018年9月20日  
     * @version 1.0
     */
    @Transactional
    public void addFoundationShowCustomer() throws Exception{
    	System.out.println("增加人数方法进来了-----------------------");
    	Foundation foundation = foundationMapper.findEntitySingle(new HashMap<>());
        Map<String,Object> paramModelMap = new HashMap<>();
        paramModelMap.put("showCustomer", foundation.getShowCustomer()+1);
		paramModelMap.put("versions", foundation.getVersions());
		Integer integer = foundationMapper.updateData(paramModelMap);
		if (integer==0)
			addFoundationShowCustomer();
		else
			System.out.println("数据修改成功");
		return;
    }
    /**
     * 增加虚拟补贴
     * @author xiaowuge  
     * @date 2018年9月20日  
     * @version 1.0
     * 
     * a:实际到账金额    b:实际虚拟金额
     */
    
    @Transactional
    public void addFoundationAllowance(BigDecimal a,BigDecimal b) throws Exception{
		Foundation foundation =  foundationMapper.findEntitySingle(new HashMap<>());
		Map<String,Object> paramModelMap = new HashMap<>();
		paramModelMap.put("versions",foundation.getVersions());
		paramModelMap.put("showTotal", foundation.getShowTotal().add(a));
		if (Constant.toEmpty(b)) {
			paramModelMap.put("allowance", foundation.getAllowance().add(b));
		}
		Integer integer = foundationMapper.updateData(paramModelMap);
		if (integer==0)
            addFoundationAllowance(a,b);
		else
			System.out.println("数据修改成功");
		return;
    }
    
    /**
     * 理赔支付回调
     * @author xiaowuge  
     * @date 2018年11月30日  
     * @version 1.0
     */
    @Transactional
    public int paymentRepairResult(String amountId) throws Exception{
    	try {
    	Map<String, Object> eventMap1 =  carMapper.findEventByRecordRechargeId(amountId);
    	System.out.println("理赔支付回调进来了-----------------------------"+amountId);
    	if(eventMap1!=null&&"2".equals(String.valueOf(eventMap1.get("rStatus")))){
    		System.out.println("理赔支付回调进来了，状态为2-----------------------------"+amountId);
    		String customerId = String.valueOf(eventMap1.get("customerId"));
    		String eventNo = String.valueOf(eventMap1.get("eventNo"));
    		Map<String,Object> pMap = new HashMap<String, Object>();
        	pMap.put("eventNo", eventNo);
        	Map<String,Object> eventMap = eventService.findOrderDetail(pMap);
    		
    		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    		String date = time.format(new Date());
    		
    		Map<String,Object> map = new HashMap<String, Object>();
    		map.put("timeRecharge", date);
    		map.put("id", amountId);
    		map.put(CommonField.STATUS, 1);
    		map.put("description", "手机号为"+eventMap.get("customerPN")+"的会员救助"+eventMap.get("model")+"支付了"+eventMap.get("amtPay")+"元");
    		recordRechargeMapper.updateModel(map);
    		System.out.println("救助支付更改支付数据成功-----------------------------"+amountId);
    		
    		
    		map = new HashMap<String, Object>();
			map.put("eventNo", eventNo); //订单号
			map.put("customerId", customerId); //
			map.put("timePay", date);
			map.put("statusEvent", 51);
			eventService.updateEvent(map);
			 
            //获取用户信息
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("id", customerId);
            Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
            //用户理赔支付
        	reqMap.clear();
        	reqMap.put("customerId", customer.get("id"));
        	reqMap.put("customerPN", customer.get("customerPN"));
        	reqMap.put("source", customer.get("source"));
        	reqMap.put("createAt", customer.get("timeJoin"));
        	reqMap.put("currentStatus", customer.get("status"));
        	reqMap.put("optTime", DateUtils.formatDate(new Date()));
        	reqMap.put("optType", 8);
        	reqMap.put("optDesc", "用户理赔支付");
        	reqMap.put("recordTime", DateUtils.formatDate(new Date()));
        	userCustomerLogService.saveUserCustomerLog(reqMap);
			
			map = new HashMap<String, Object>();
			map.put("id", eventMap.get("accountId"));
			Map<String,Object> acountMap = accountMapper.findSingle(map);
			
			String amtFreeze1 = Constant.toOr(String.valueOf(acountMap.get("amtFreeze")), Constant.toReadPro("orKey"), "decrypt");
		    BigDecimal amtFreeze = new BigDecimal(amtFreeze1);
		    BigDecimal newAf = amtFreeze.add(new BigDecimal(String.valueOf(eventMap.get("amtBusiness"))));
		    map.put("amtFreeze", Constant.toOr(String.valueOf(newAf), Constant.toReadPro("orKey"), "encrypt"));
		    accountMapper.updateModel(map);

			BigDecimal amtPay = new BigDecimal(String.valueOf(eventMap.get("amtPay")).equals("null")?"0":String.valueOf(eventMap.get("amtPay")));
    		updateFoundation(amtPay);
    	}
    	return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    }
    public Map<String, Object> findAdminPN() throws Exception{
    	return adminMapper.findAdminPN();
    };
    public Map<String, Object> findUpdateCount() throws Exception{
    	return carMapper.findUpdateCount();
    };
    public void sendSMS(String carId,int type) throws Exception{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("id", carId);
		Map<String,Object> m = this.findOne(resultMap);
		String licensePlateNumber = String.valueOf(m.get("licensePlateNumber"));
		String a = type==1?"互助救助申请":"保险理赔申请";
		String code = licensePlateNumber+"的"+a;
		// 发送短信通知
		int i = 0;
		List<String> phoneList = businessMapper.getadministratorPhone();
		if (phoneList != null && phoneList.size() > 0){
			for (String phone : phoneList){
				i++;
				SmsDemo.sendSms(61, phone, code);
				System.out.println("++++++++电话 "+phone+" +++++已发送");				
			}
		}
		System.out.println("+++++共有++"+phoneList.size()+"++++要发送++++，已发送+++++"+i);

    }

	/**
	 * 按状态统计车辆数量
	 * @throws Exception
	 */
	public Map<String, Object> findCarCount(Map<String, Object> paramModelMap) throws Exception {
		// 处理结束时间，多加一天，sql用的是between and
		if (Constant.toEmpty(paramModelMap.get("endTime"))){
			String endTime = LocalDate.parse((String)paramModelMap.get("endTime")).plusDays(1).toString();
			paramModelMap.put("endTime",endTime);
		}
		Map<String, Object> count = carMapper.findCount(paramModelMap);
		count.put("reJoinNum", carMapper.findCarList3(paramModelMap).size());
		count.put("observationNum", carMapper.getStatus13Cars(paramModelMap).size());
		count.put("guaranteeNum", carMapper.getStatus20Cars(paramModelMap).size());
		count.put("outNum", carMapper.getStatus30Cars(paramModelMap).size());
		return count;
	}
	
	/**
	 * 统计一年内的充值金额
	 * @author xiaowuge  
	 * @date 2018年11月7日  
	 * @version 1.0
	 */
	public Map<String,Object> getPayAmount(String carId) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		BigDecimal a = new BigDecimal("0");
		Map<String,Object> pmap = new HashMap<String, Object>();
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        pmap.put("carId", carId);
       	Map<String,Object> cMap = recordRechargeMapper.selectCreatTime(pmap);
       	if(cMap!=null){
       		String ctime = String.valueOf(cMap.get("ctime"));
           	String rId = String.valueOf(cMap.get("id"));
           	Calendar nTime = Calendar.getInstance();
           	Calendar cTime = Calendar.getInstance();
           	
				cTime.setTime(time.parse(ctime));
				Calendar calendar1 = Calendar.getInstance();
				for(int i=0;i<100;i++){
					cTime.add(Calendar.YEAR, 1);
					if(cTime.getTime().getTime()>nTime.getTime().getTime()){
						cTime.add(Calendar.YEAR, -1);
						pmap.put("time", time.format(cTime.getTime()));
						Map<String,Object> sMap = recordRechargeMapper.selectAmt(pmap);
						if(sMap!=null){
							a = new BigDecimal(String.valueOf(sMap.get("sum")).equals("null")?"0":String.valueOf(sMap.get("sum")));
							map.put("minT", sMap.get("minT"));
						}
						break;
					}
				}
				map.put("rId", rId);
       	}
    	map.put("amount", a);
       	return map;
	}
	public Map<String,Object> saveImg(String mediaId,String url) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String token = weiXinUtils.getAccessToken();
		URL urlGet = new URL("https://api.weixin.qq.com/cgi-bin/media/get?access_token="+token+"&media_id="+mediaId);    

        HttpURLConnection http = (HttpURLConnection) urlGet

                .openConnection();

        http.setRequestMethod("GET"); // 必须是get方式请求

        http.setRequestProperty("Content-Type",

                "application/x-www-form-urlencoded");

        http.setDoOutput(true);

        http.setDoInput(true);

        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒

        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

        http.connect();

        // 获取文件转化为byte流

        InputStream is = http.getInputStream();
        byte[] data = readInputStream(is);
		if(url!=null){
			String name = UUID.randomUUID().toString().replace("-", "")+".jpg";
			UploadFileUtil.saveImg(url,name,data);
			map.put("associatorimg", name);
			map.put("flag",1);
		}
		BASE64Encoder encoder = new BASE64Encoder();
        String base = encoder.encode(data);
		System.out.println(new String(data));
		base = 	base.replace("data:image/png;base64,","");
		base = base.replace("data:image/jpeg;base64,","");
		map.put("base",base);
        return map;
	}
	 public static byte[] readInputStream(InputStream inStream) throws Exception{    
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();    
	        //创建一个Buffer字符串    
	        byte[] buffer = new byte[1024];    
	        //每次读取的字符串长度，如果为-1，代表全部读取完毕    
	        int len = 0;    
	        //使用一个输入流从buffer里把数据读取出来    
	        while( (len=inStream.read(buffer)) != -1 ){    
	            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度    
	            outStream.write(buffer, 0, len);    
	        }    
	        //关闭输入流    
	        inStream.close();    
	        //把outStream里的数据写入内存    
	        return outStream.toByteArray();    
	    }

	/**
	 * 					车妈妈激活卡
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Map<String,Object> saveChevhuzhu(HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = MapUtil.build().put(CommonField.STATUS,false).put(CommonField.RESULTMESSAGE,"").over();
		Map parameterMap = getParameterMap(request);
		if (getStatus(parameterMap)) {
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.PARAMETER_NOT_BE_BLANK);
			return resultMap;
		}
		String mobileNumber = (String) parameterMap.get(CommonField.MOBILE_NUMBER);
		String licensePlateNumber =(String) parameterMap.get(CommonField.LICENSE_PLATE_NUMBER);
		String merchantId = (String) parameterMap.get(CommonField.MERCHANT_CODE);
		Integer isNaturalUser = Integer.valueOf(parameterMap.get("isNaturalUser").toString());
		BigDecimal money = new BigDecimal((String) parameterMap.get("money"));
		if (!Constant.toEmpty(merchantId)){
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.MERCHANT_CODE_CANNOT_EMPTY);
			return resultMap;
		}
		if (!Constant.toReadPro("merchantCode").equals(merchantId)){
			resultMap.put(CommonField.VALIDATION_CODE, CommonField.MERCHANTCODE_FAILURE);
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.MERCHANT_CODE_INEXISTENCE);
			return resultMap;
		}
		if (!(Constant.toEmpty(mobileNumber)&&Constant.toEmpty(licensePlateNumber)&&Constant.toEmpty(isNaturalUser)&&Constant.toEmpty(money))) {
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.MUST_NOT_BE_BLANK);
			return resultMap;
		}
//		if (1==isNaturalUser){
//			resultMap.put(CommonField.RESULTMESSAGE,"对不起,你不是非自然用户");
//			return resultMap;
//		}
		if (money.compareTo(new BigDecimal(99))==1){
			resultMap.put(CommonField.RESULTMESSAGE,"本年度充值金额不能大于99元!");
			return resultMap;
		}

		Car car =carMapper.getCarByLicensePlateNumber(licensePlateNumber);
		if (car != null) {
			if (13==car.getStatus()||20 ==car.getStatus()) {
				resultMap.put(CommonField.VALIDATION_CODE, "389");
				resultMap.put(CommonField.RESULTMESSAGE,"对不起,车辆已激活!");
				return resultMap;
			}
		}
		Map<String,Object> parameter = new HashMap<>();
		parameter.put(CommonField.LICENSE_PLATE_NUMBER,licensePlateNumber);
		parameter.put(CommonField.MOBILE_NUMBER,mobileNumber);
		Map<String,Object> carMaps = carMapper.getCarDetail(parameter);
		Integer status=0;
		if (!getStatus(carMaps)){
			status = (Integer) carMaps.get(CommonField.STATUS);
			if (2 == (Integer) carMaps.get("typeGuarantee")) {
				resultMap.put(CommonField.RESULTMESSAGE,"年费会员请不要重复激活");
				return resultMap;
			}
		}
		if (getStatus(carMaps)&&car != null) {
			resultMap.put(CommonField.RESULTMESSAGE,"对不起,该车辆已经存在别人名下!");
			return resultMap;
		}

		String date = DateUtils.getStringDateTime(new Date());
		Map<String,Object> paramModelMap =new HashMap<>();
		paramModelMap = customerMapper.getCustomerinfo(mobileNumber);
		String customerId;
		if(getStatus(paramModelMap)){
			//保存用户
			HashMap<String, Object> hashMap = new HashMap<>();
			Date tokenTime = DateUtils.getTokenTime();
			String toKen = SecurityUtil.getToKen();
			hashMap.put("timeLogin", date);
			hashMap.put("customerPN",mobileNumber);
			hashMap.put("toKen",toKen);
			hashMap.put("tokenaging",tokenTime);
			hashMap.put("source","车妈妈");
			customerId = userCustomerService.saveAndGetCustomer(hashMap);
		}else{
			customerId= String.valueOf(paramModelMap.get("id"));
			paramModelMap.clear();
		}
		
		paramModelMap =new HashMap<>();
		Map<String,Object> maintenanceshop = middleCustomerMaintenanceshopMapper.selectByMobileNumber(mobileNumber);
		Map<String,Object> openIdMap = customerMapper.selectByMobileNumber(mobileNumber);
		String openId=null;
		if(!getStatus(openIdMap)) openId = openIdMap.get(CommonField.OPEN_ID).toString();
		if(!getStatus(maintenanceshop)){ //有关注记录则更新
			paramModelMap.put("customerId", customerId);
			if (!Constant.toEmpty(maintenanceshop.get(CommonField.CUSTOMER_TEL)))
				paramModelMap.put(CommonField.CUSTOMER_TEL,mobileNumber);
			else
				paramModelMap.put(CommonField.CUSTOMER_TEL,maintenanceshop.get(CommonField.CUSTOMER_TEL).toString());
			if (!Constant.toEmpty(maintenanceshop.get(CommonField.OPEN_ID))) {
				if (Constant.toEmpty(openId)) paramModelMap.put(CommonField.OPEN_ID, openId);
			}
			if ((Integer)maintenanceshop.get(CommonField.MAINTENANCESHOP_ID)==-1) paramModelMap.put(CommonField.MAINTENANCESHOP_ID,merchantId);

			if (!Constant.toEmpty(maintenanceshop.get(CommonField.CUSTOMER_TEL))||!Constant.toEmpty(maintenanceshop.get(CommonField.OPEN_ID))
					||(Integer)maintenanceshop.get(CommonField.MAINTENANCESHOP_ID)==-1) middleCustomerMaintenanceshopMapper.updateModelByCustomerTel(paramModelMap);
			middleCustomerMaintenanceshopMapper.updateModel(paramModelMap);
		}else{
			if (Constant.toEmpty(openId))paramModelMap.put(CommonField.OPEN_ID, openId);
			paramModelMap.put(CommonField.CUSTOMER_TEL,mobileNumber);
			paramModelMap.put("createAt",date);
			paramModelMap.put(CommonField.STATUS,0);
			paramModelMap.put(CommonField.MAINTENANCESHOP_ID,merchantId);
			paramModelMap.put("customerId",customerId);
			middleCustomerMaintenanceshopMapper.saveSingle(paramModelMap); //保存关注表
		}


		paramModelMap=new HashMap<>();
		paramModelMap.put(CommonField.LICENSE_PLATE_NUMBER, licensePlateNumber);
		Map<String,Object> carMap = findOne(paramModelMap);
		String carId = "";
		Integer typeGuarantee=1;
		//如果车辆已经存在则更新
		if(carMap!=null&&carMap.get("id")!=null){
			carId = String.valueOf(carMap.get("id"));
			BigDecimal oldAmount = String.valueOf(carMap.get("amtCooperation")).equals("null")
					?new BigDecimal(0):new BigDecimal(String.valueOf(carMap.get("amtCooperation")));
			BigDecimal newAmount = oldAmount.add(money);
			if (30==status){
				Date timeBegin = (Date) carMap.get("timeBegin");
				Date yearDate = DateUtils.getYearDate(timeBegin);
				BigDecimal[] rechargeBalance = getRechargeBalance(timeBegin,carId);
				BigDecimal recordRechargeMoney=rechargeBalance[0];
				if (recordRechargeMoney.compareTo(new BigDecimal(99))==0){
					resultMap.put(CommonField.RESULTMESSAGE,"你本年度已经充值99元了,不能继续充值!");
					return resultMap;
				}
				if (null !=recordRechargeMoney&&recordRechargeMoney.add(money).compareTo(new BigDecimal(99))==1){
					resultMap.put(CommonField.RESULTMESSAGE,"你本年度充值大于99元了,不能继续充值!");
					return resultMap;
				}

				paramModelMap.clear();
				System.err.println(recordRechargeMoney.add(money).compareTo(new BigDecimal(99))==0);
				if (null !=recordRechargeMoney&&recordRechargeMoney.add(money).compareTo(new BigDecimal(99))==0){
					paramModelMap.put("timeEnd", yearDate);
					paramModelMap.put("typeGuarantee",2);
					typeGuarantee=2;
				}
			}
			paramModelMap.put(CommonField.STATUS, 13);
			paramModelMap.put("messageFlag", 5);
			paramModelMap.put("id",carId);
			paramModelMap.put("amtCooperation",newAmount);
			paramModelMap.put("reJoinNum", "reJoinNum");
			updateCar(paramModelMap);
		}else {  //增加车辆
			paramModelMap.clear();
			paramModelMap.put("customerId", customerId);
			paramModelMap.put("amtCompensation", 1000);
			paramModelMap.put("amtCooperation", money);
			paramModelMap.put("reJoinNum", "reJoinNum");
			if (money.compareTo(new BigDecimal(99))==0)
				paramModelMap.put("typeGuarantee", 2);
			else
				paramModelMap.put("typeGuarantee", 1);
			paramModelMap.put("level", 1);
			paramModelMap.put("createAt", date);
			paramModelMap.put("payTime", date);
			paramModelMap.put("licensePlateNumber", licensePlateNumber); //车牌
			paramModelMap.put(CommonField.STATUS, 13);
			paramModelMap.put("timeSignout", "1111-11-11 11:11:11");
			paramModelMap.put("reasonSignout", "");
			paramModelMap.put("messageFlag", 5);
			saveCar(paramModelMap);
			carId = String.valueOf(paramModelMap.get("id"));
		}
		
		//保存车辆后---->查询车辆信息------>保存操作记录
        Map<String, Object> carLogMap = new HashMap<>();
        carLogMap.put("customerId", customerId);
        carLogMap.put("carId", carId);
        carLogMap.put("optTime", carMap.get("createAt"));
        carLogMap.put("optType", 1);
        carLogMap.put("optDesc", "车妈妈用户车辆支付");
        carLogMap.put("recordeTime", DateUtils.formatDate(new Date()));
        carLogService.saveCarLog(carLogMap);
        
		addFoundationShowCustomer();
		Map dicMap = new HashMap();
		dicMap.put("type","observationTime");
		Map dictionary = dictionaryService.findSingle(dicMap);
		//开启定时器
		Scheduler sche = schedulerFactory.getScheduler();
		Map<String, String> dateMap = DateUtils.getDateMap(new Long(dictionary.get("value").toString()));
		Long imestamp=new Date().getTime();
		imestamp = imestamp+Long.valueOf(dictionary.get("value").toString());
		Date date1 = new Date(imestamp);
		Map<String,Object> map = new HashMap<>();
		map.put("jobName","observationJob_" + carId);
		map.put("typeGuarantee",typeGuarantee);
		map.put("carId", carId);
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String observationEndTime = time.format(date1);
		String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
		QuartzUtils.addJob(sche,"observationJob_" + carId, ObservationJob.class, map, cron);
		map.clear();
		map.put("observationEndTime",observationEndTime);
		map.put("id",carId);
		map.put("reJoinNum", "reJoinNum");
		updateCar(map);
		
		saveRecordRecharge(customerId, carId, money, "1", mobileNumber, licensePlateNumber);
		
		//车辆充值后------>保存操作记录
        Map<String, Object> carLogMap1 = new HashMap<>();
        carLogMap1.put("customerId", customerId);
        carLogMap1.put("carId", carId);
        carLogMap1.put("optTime", DateUtils.formatDate(new Date()));
        carLogMap1.put("optType", 2);
        carLogMap1.put("optDesc", "车妈妈用户车辆支付");
        carLogMap1.put("recordeTime", DateUtils.formatDate(new Date()));
        carLogService.saveCarLog(carLogMap1);
        
    	//保存车辆进入观察期------>保存操作记录
        Map<String, Object> carLogMap2 = new HashMap<>();
        carLogMap2.put("customerId", customerId);
        carLogMap2.put("carId", carId);
        carLogMap2.put("optTime", carMap.get("createAt"));
        carLogMap2.put("optType", 1);
        carLogMap2.put("optDesc", "车妈妈用户车辆进入观察期");
        carLogMap2.put("recordeTime", DateUtils.formatDate(new Date()));
        carLogService.saveCarLog(carLogMap2);
		
		addFoundationAllowance(money,money);
		Map<String,Object> pMap = new HashMap<>();
		pMap.put(CommonField.LICENSE_PLATE_NUMBER,licensePlateNumber);
		pMap.put(CommonField.MOBILE_NUMBER,mobileNumber);
		Map<String,Object> carMap1 = carMapper.getCarDetail(pMap);
//		JSONObject json = JSONObject.fromObject(carMap1);
//		byte[] encoded = ThreeDES.encryptMode(ThreeDES.keyBytes, json.toString().getBytes());
//		String carDetail = ThreeDES.bytesToHexString(encoded);
		eventService.updateDayNumber("observationNum",1);
		String chemama = "chemama"+mobileNumber;
		String s = redisUtil.get(chemama);
        if (Constant.toEmpty(s)) {
            redisUtil.put(chemama,s+"_"+carMap1.get(CommonField.LICENSE_PLATE_NUMBER).toString()+","+carMap1.get("amtCompensation").toString());
        }else{
			redisUtil.put(chemama,carMap1.get(CommonField.LICENSE_PLATE_NUMBER).toString()+","+carMap1.get("amtCompensation").toString());
        }
		resultMap.put(CommonField.STATUS,true);
		resultMap.put(CommonField.CAR_DETAIL,getMap(carMap1));
		
		//短信提醒用户激活
		SmsDemo.sendSms(22, mobileNumber, null);
		
		return resultMap;

	}
	
	
	
	public Map<String,Object> getChemamaCarByMobile(String mobileNumber) throws Exception{
		return carMapper.getChemamaCarByMobile(mobileNumber);
	}

	private BigDecimal[] getRechargeBalance(Date timeBegin,String carId){
		if (timeBegin==null){
			CbhCar cbhCar = cbhCarMapper.selectByPrimaryKey(Integer.valueOf(carId));
			System.out.println("cbhCar = " + cbhCar);
			BigDecimal[] bigDecimals;
			if (cbhCar.getTypeGuarantee()==1)
				bigDecimals =new BigDecimal[]{new BigDecimal(0),new BigDecimal(90)};
			else
				bigDecimals =new BigDecimal[]{new BigDecimal(99),new BigDecimal(0)};
			return bigDecimals;
		}
		Date yearDate = DateUtils.getYearDate(timeBegin);
		String stringDateTime = DateUtils.getStringDateTime(yearDate);
		Integer dayNumber = Integer.valueOf(Constant.toReadPro("observerDayNumber"));
		Date dayDate = DateUtils.getDayDate(timeBegin, "-", dayNumber);
		Map<String, Object> over = MapUtil.build().put("carId", Integer.valueOf(carId)).put("yearDate", stringDateTime).put("timeBegin", DateUtils.getStringDateTime(dayDate)).over();
		BigDecimal recordRechargeMoney = recordRechargeMapper.findRechargeByCarId(over);
		BigDecimal subtract = new BigDecimal(99).subtract(recordRechargeMoney);
		BigDecimal[] bigDecimals =new BigDecimal[]{recordRechargeMoney,subtract};
		return bigDecimals;
	}

	/**
	 * 
	 * @author xiaowuge  
	 * @date 2018年9月14日  
	 * @version 1.0
	 * "1"表示未激活  "2"表示激活 "3"表示不可用
	 */

	public Map<String,Object> findChemamaCar(HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = MapUtil.build().put(CommonField.STATUS,"1").put(CommonField.RESULTMESSAGE,"").over();
		Map parameterMap = getParameterMap(request);
		if (getStatus(parameterMap)) {
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.PARAMETER_NOT_BE_BLANK);
			return resultMap;
		}
		String mobileNumber = (String) parameterMap.get(CommonField.MOBILE_NUMBER);
		String licensePlateNumber =(String) parameterMap.get(CommonField.LICENSE_PLATE_NUMBER);
		String merchantCode = (String) parameterMap.get(CommonField.MERCHANT_CODE);
		Integer isNaturalUser = Integer.valueOf(parameterMap.get("isNaturalUser").toString());
		if (!Constant.toEmpty(merchantCode)){
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.MERCHANT_CODE_CANNOT_EMPTY);
			return resultMap;
		}
		if (!Constant.toReadPro("merchantCode").equals(merchantCode)){
			resultMap.put(CommonField.VALIDATION_CODE, CommonField.MERCHANTCODE_FAILURE);
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.MERCHANT_CODE_INEXISTENCE);
			return resultMap;
		}
		if (!(Constant.toEmpty(mobileNumber)&&Constant.toEmpty(licensePlateNumber)&&Constant.toEmpty(isNaturalUser))) {
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.MUST_NOT_BE_BLANK);
			return resultMap;
		}
		Map<String,Object> pMap = new HashMap<>();
		pMap.put(CommonField.LICENSE_PLATE_NUMBER,licensePlateNumber);
		pMap.put(CommonField.MOBILE_NUMBER,mobileNumber);
		Map<String,Object> carMap = carMapper.getCarDetail(pMap);
		Car car =carMapper.getCarByLicensePlateNumber(licensePlateNumber);
		if (getStatus(carMap)) {
			if (car != null) {
				resultMap.put(CommonField.RESULTMESSAGE,"对不起,该车辆已经存在别人名下!");
				return resultMap;
			}
			resultMap.put(CommonField.VALIDATION_CODE, "387");
			resultMap.put(CommonField.RESULTMESSAGE,"会员未激活");
			resultMap.put(CommonField.CAR_DETAIL,getMap(0,99));
			return resultMap;
		}
		Integer status = (Integer) carMap.get(CommonField.STATUS);
		if(31 == status){
			resultMap.put(CommonField.VALIDATION_CODE, "387");
			resultMap.put(CommonField.RESULTMESSAGE,"对不起，该车辆不在服务范围内！");
			resultMap.put(CommonField.STATUS, "3");
		}
		if (1 ==status||30 ==status) {
			resultMap.put(CommonField.VALIDATION_CODE, "387");
			resultMap.put(CommonField.RESULTMESSAGE,"会员未激活");
			String carId = String.valueOf(carMap.get("carId"));
			Date timeBegin = (Date) carMap.get("timeBegin");
			BigDecimal[] rechargeBalance = getRechargeBalance(timeBegin,carId);
			resultMap.put(CommonField.CAR_DETAIL,getMap(rechargeBalance[0].intValue(),rechargeBalance[1].intValue()));
			return resultMap;
		}
//		JSONObject json = JSONObject.fromObject(carMap);
//		byte[] encoded = ThreeDES.encryptMode(ThreeDES.keyBytes, json.toString().getBytes());
//		String carDetail = ThreeDES.bytesToHexString(encoded);
		resultMap.put(CommonField.STATUS,"2");
		resultMap.put(CommonField.CAR_DETAIL,getMap(carMap));
		return resultMap;
	}


	private Map<String,Object> getMap(Map<String,Object> carMap){
		List<String> list = Arrays.asList(CommonField.OPEN_ID, "customerId", CommonField.STATUS, "carId","timeBegin");
		BigDecimal[] rechargeBalance = getRechargeBalance((Date) carMap.get("timeBegin"),carMap.get("carId").toString());
		list.forEach(a->{
			carMap.remove(a);
		});
		carMap.put("rechargeBalance",rechargeBalance[1]);
		return carMap;
	}

	private  Map<String,Object> getMap(Integer amtCooperation,Integer rechargeBalance){
		return MapUtil.build().put(CommonField.LICENSE_PLATE_NUMBER,"").put("amtCooperation",0).put("rechargeBalance",rechargeBalance).
				put("amtCompensation",1000).put("typeGuarantee",1).over();
	};

	/**
	 * 车妈妈支付后调的接口
	 * @param request
	 * @return
	 */
	@Transactional
	public Map<String,Object> payEnd(HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = MapUtil.build().put(CommonField.STATUS,false).put(CommonField.RESULTMESSAGE,"").over();
		Map parameterMap = getParameterMap(request);
		if (getStatus(parameterMap)) {
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.PARAMETER_NOT_BE_BLANK);
			return resultMap;
		}
		String ipAdrress = IpAdrressUtil.getIpAdrress(request);
		String[] split = Constant.toReadPro("cheMaMaIP").split("_");
		if (!Arrays.asList(split).contains(ipAdrress)){
			resultMap.put(CommonField.RESULTMESSAGE,"对不起,请使用正确IP调用");
			return resultMap;
		}
		String mobileNumber = (String) parameterMap.get(CommonField.MOBILE_NUMBER);
		String licensePlateNumber =(String) parameterMap.get(CommonField.LICENSE_PLATE_NUMBER);
		String merchantCode = (String) parameterMap.get(CommonField.MERCHANT_CODE);
		BigDecimal money = new BigDecimal((String) parameterMap.get("money"));
		if (!Constant.toEmpty(merchantCode)){
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.MERCHANT_CODE_CANNOT_EMPTY);
			return resultMap;
		}
		if (!Constant.toReadPro("merchantCode").equals(merchantCode)){
			resultMap.put(CommonField.VALIDATION_CODE, CommonField.MERCHANTCODE_FAILURE);
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.MERCHANT_CODE_INEXISTENCE);
			return resultMap;
		}
		if (!(Constant.toEmpty(mobileNumber)&&Constant.toEmpty(licensePlateNumber)&&Constant.toEmpty(money))) {
			resultMap.put(CommonField.RESULTMESSAGE,CommonField.MUST_NOT_BE_BLANK);
			return resultMap;
		}
		Map<String,Object> pMap = new HashMap<>();
		pMap.put(CommonField.LICENSE_PLATE_NUMBER,licensePlateNumber);
		pMap.put(CommonField.MOBILE_NUMBER,mobileNumber);
		Map<String,Object> carMap = carMapper.getCarDetail(pMap);
		if (getStatus(carMap)) {
			resultMap.put(CommonField.RESULTMESSAGE,"对不起,没有这个车辆");
			return resultMap;
		}
		if (2 == (Integer) carMap.get("typeGuarantee")) {
			resultMap.put(CommonField.RESULTMESSAGE,"年费会员请不要重复充值!");
			return resultMap;
		}
		Integer	status = (Integer) carMap.get(CommonField.STATUS);
		String customerId = String.valueOf(carMap.get("customerId"));
		String carId = String.valueOf(carMap.get("carId"));
		BigDecimal oldAmount = String.valueOf(carMap.get("amtCooperation")).equals("null")
				?new BigDecimal(0):new BigDecimal(String.valueOf(carMap.get("amtCooperation")));
		BigDecimal newAmount = oldAmount.add(money);
		Integer typeGuarantee=1;

		Date timeBegin = (Date) carMap.get("timeBegin");
		Date yearDate = DateUtils.getYearDate(timeBegin);
		BigDecimal[] rechargeBalance = getRechargeBalance(timeBegin,carId);
		BigDecimal recordRechargeMoney=rechargeBalance[0];
//		String stringDateTime = DateUtils.getStringDateTime(yearDate);
//		Map<String, Object> over = MapUtil.build().put("carId", Integer.valueOf(carId)).put("yearDate", stringDateTime).put("timeBegin", DateUtils.getStringDateTime(timeBegin)).over();
//		BigDecimal recordRechargeMoney = recordRechargeMapper.findRechargeByCarId(over);
		if (recordRechargeMoney.compareTo(new BigDecimal(99))==0){
			resultMap.put(CommonField.RESULTMESSAGE,"你本年度已经充值99元了,不能继续充值!");
			return resultMap;
		}
		if (null !=recordRechargeMoney&&recordRechargeMoney.add(money).compareTo(new BigDecimal(99))==1){
			resultMap.put(CommonField.RESULTMESSAGE,"你本年度充值大于99元了,不能继续充值!");
			return resultMap;
		}
		parameterMap.clear();
		if (null !=recordRechargeMoney&&recordRechargeMoney.add(money).compareTo(new BigDecimal(99))==0){
			parameterMap.put("timeEnd", yearDate);
			parameterMap.put("typeGuarantee",2);
			typeGuarantee=2;
		}
		if (30==status) {
			parameterMap.put(CommonField.STATUS, 13);
			Map<String, Object> upfMap = new HashMap<>();
			upfMap.put("id", carId);
			upfMap.put("typeGuarantee", typeGuarantee);
			upfMap.put(CommonField.OPEN_ID, carMap.get(CommonField.OPEN_ID));
			upfMap.put("licensePlateNumber", licensePlateNumber);
			upfMap.put("customerId", carMap.get("customerId"));
			observation(upfMap);
			addFoundationShowCustomer();
		}
		int i = saveRecordRecharge(customerId, carId, money, "2", mobileNumber, licensePlateNumber);
		if (1 != i) {
			resultMap.put(CommonField.RESULTMESSAGE,"充值失败");
			return resultMap;
		}
		parameterMap.put("id",carId);
		parameterMap.put("amtCooperation",newAmount);
		updateCar(parameterMap);
		addFoundationAllowance(money,money);
		eventService.updateDayNumber("payNum",1);
		resultMap.put(CommonField.STATUS,true);
		resultMap.put(CommonField.CAR_DETAIL,getMap(carMapper.getCarDetail(pMap)));
		return resultMap;
	}
	@Transactional
	public void updateFoundation (BigDecimal money)throws Exception{
		Foundation foundation =  foundationMapper.findEntitySingle(new HashMap<>());
		Map<String,Object> upfMap = new HashMap<>();
		upfMap.put("amtTotal", foundation.getAmtTotal().add(money));
		upfMap.put("amtBalance", foundation.getAmtBalance().add(money));
		upfMap.put("showTotal", foundation.getShowTotal().add(money));
		upfMap.put("payNum", foundation.getPayNum()+1);
		upfMap.put("versions", foundation.getVersions());
		Integer integer = foundationMapper.updateData(upfMap);
		if (integer==0)
			updateFoundation(money);
		else
			System.out.println("数据修改成功");
		return;

	}

	@Transactional
	public int saveRecordRecharge(String customerId,String carId,BigDecimal money,String eventType,String mobileNumber,
								   String licensePlateNumber)throws Exception{
		HashMap<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("customerId", customerId);
		parameterMap.put("carId", carId);
		parameterMap.put("amt", money);
		parameterMap.put("type", 10);
		parameterMap.put(CommonField.STATUS, 1);
		parameterMap.put("eventType", eventType);
		parameterMap.put("timeRecharge", DateUtils.getDateTime());
		parameterMap.put("description", "手机号为"+mobileNumber+"的会员为"+licensePlateNumber+"充值了"+money+"元");
		return recordRechargeMapper.saveSingle(parameterMap);
	}

	private Map getParameterMap(HttpServletRequest request)throws Exception{
		JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
		String commonField = Constant.toJsonValue(jsonObject, CommonField.COMMON_FIELD);
		if (Constant.toEmpty(commonField)) {
			byte[] srcBytes = ThreeDES.decryptMode(ThreeDES.keyBytes, ThreeDES.hexStringToByte(commonField));
			return JsonUtils.json2Map(new String(srcBytes));
		}
		return null;
	}
	
	/**
	 * 
	* @author xiaowuge  
	* @date 2018年9月5日  
	* @version 1.0
	* 空位 true  否则为false
	 */
   private boolean getStatus(Map parameterMap){
	   if (parameterMap == null||parameterMap.isEmpty()) {
		   return true;
	   }
	   return false;
   }
	public int saveCarImg(HttpServletRequest request) throws Exception{
		int flag = 1;
		try {
			String mobileNumber = request.getParameter("mobileNumber");
			String licensePlateNumber = request.getParameter("licensePlateNumber");

			String[] base = request.getParameterValues("base");
			JSONObject json = new JSONObject();
			JSONObject jsonObject = new JSONObject();
			Map<String,Object> pMap = new HashMap<String, Object>();
			pMap.put("licensePlateNumber",	licensePlateNumber);
			pMap.put("mobileNumber",	mobileNumber);
			List<Map<String,Object>> carList = carMapper.findCardetail(pMap);
			if(carList!=null&&carList.size()>0){
				String carId = String.valueOf(carList.get(0).get("id"));
				String carPhotos = String.valueOf(carList.get(0).get("carPhotos"));
				if(!carPhotos.equals("null")){
					jsonObject = JSONObject.fromObject(carPhotos);
				}
				int i = 1;
				for(String b:base){
					if(b.equals("null")){
						switch (i) {
							case 1:
								json.put("zh", jsonObject.get("zh"));
								break;
							case 2:
								json.put("yh", jsonObject.get("yh"));
								break;
							case 3:
								json.put("zq", jsonObject.get("zq"));
								break;
							case 4:
								json.put("yq", jsonObject.get("yq"));
								break;
							case 5:
								json.put("qd", jsonObject.get("qd"));
								break;
							case 6:
								json.put("zc", jsonObject.get("zc"));
								break;
							case 7:
								json.put("yc", jsonObject.get("yc"));
								break;
						/*case 5:
							json.put("cjh", jsonObject.get("cjh"));
							break;*/
							default:
								break;
						}
					}else{
						Map<String,Object> rmap = saveImg(b,CommonField.CAR_IMG_URL);
						if(rmap!=null&&rmap.get("flag")!=null){
							switch (i) {
								case 1:
									json.put("zh", rmap.get("associatorimg"));
									break;
								case 2:
									json.put("yh", rmap.get("associatorimg"));
									break;
								case 3:
									json.put("zq", rmap.get("associatorimg"));
									break;
								case 4:
									json.put("yq", rmap.get("associatorimg"));
									break;
								case 5:
									json.put("qd", rmap.get("associatorimg"));
									break;
								case 6:
									json.put("zc", rmap.get("associatorimg"));
									break;
								case 7:
									json.put("yc", rmap.get("associatorimg"));
									break;
							/*case 5:
								json.put("cjh", rmap.get("associatorimg"));
								break;*/
								default:
									break;
							}
						}
					}

					i++;
				}
				SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = time.format(new Date());
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("carPhotos", JSON.toJSONString(json));
				map.put("id", carId);
				map.put("timeExamine", date);
				map.put(CommonField.STATUS, 10);
				updateCar(map);

				sendSMS(carId, 1);

				eventService.updateDayNumber("examineNum",1);
			}else{
				flag = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	public int checkChemamaCarById(String carId) throws Exception{
		List<Map<String,Object>> list = carMapper.checkChemamaCarById(carId);
		return list!=null&&list.size()>0?1:0;
	}
	/**
	 * 修改互助余额
	 * @author xiaowuge  
	 * @date 2018年9月17日  
	 * @version 1.0
	 */
	@Transactional
	public void updateAmt(Map<String,Object> paramModelMap) throws Exception{
		String type = paramModelMap.get("type").toString();
		if(type.equals("1")){
			carMapper.updateAmt1(paramModelMap);
		}else{
			carMapper.updateAmt2(paramModelMap);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("licensePlateNumber", paramModelMap.get("licensePlateNumber"));
		Map<String,Object> userInfo = userCustomerService.findUser(map);
		map.clear();
		map.put("customerId", userInfo.get("id"));
		map.put("carId", userInfo.get("carId"));
		map.put("amt", paramModelMap.get("addAmt"));
		map.put("type", 4);
		map.put(CommonField.STATUS, 1);
		map.put("eventType", "2");
		map.put("description", paramModelMap.get("practitioner") + "为"+paramModelMap.get("licensePlateNumber")+"充值了"+paramModelMap.get("addAmt")+"元");
		recordRechargeMapper.saveSingle(map);
        addFoundationAllowance(new BigDecimal(paramModelMap.get("addAmt").toString()),new BigDecimal(paramModelMap.get("addAmt").toString()));
	}

	/**
	 * 车妈妈首页数据的接口
	 * @return
	 */
	public Map<String,Object> homePageData()throws Exception {
		Map<String, Object> resultMap = MapUtil.build().put(CommonField.STATUS,false).put(CommonField.RESULTMESSAGE,"").over();
		Map<String,Object> homePageData = customerMapper.getHomePageData();
		if(getStatus(homePageData)){
			resultMap.put(CommonField.RESULTMESSAGE,"查询失败请重新再查!");
			return resultMap;
		}
		homePageData.put("runDayNumbey",DateUtils.cutTwoDateToDay(DateUtils.getDateTimeByString("2018-04-04 00:00:00"), DateUtils.getDateTime()));
		resultMap.put(CommonField.STATUS,true);
		resultMap.put("homePageData",homePageData);
		return resultMap;


	}

	/**
	 * 将车辆修改为不可用状态
	 * @param paramModelMap
	 */
	@Transactional
	public void modifyCarUnavailable(Map<String, Object> paramModelMap) throws Exception {
		if(Constant.toEmpty(paramModelMap.get("licensePlateNumber"))){
			Map<String, Object> carInfo = carMapper.findCarByLicensePlateNumber(MapUtil.build().put("licensePlateNumber", paramModelMap.get("licensePlateNumber")).over());
			if (carInfo == null || carInfo.get("status") == null || 31 == (Integer) carInfo.get("status")){
				System.out.println("该车辆不存在!,或者该这已经是不可用状态,不能重复设置!");
				return;
			}
			// 将车辆的状态更改为31:不可用;互助余额设置为0,更新时间
			paramModelMap.put("unavailableTime", DateUtils.formatDate(new Date()));
			carMapper.modifyCarUnavailable(paramModelMap);
			// 查询出该车牌号的所有，充值记录
			List<Map<String, Object>> recordList =  recordRechargeMapper.findAllRecordByLicensePlateNumber(paramModelMap);
			// 新增充值记录的日志
			for (Map<String, Object> record : recordList){
				Map<String, Object> map = new HashMap<>();
				map.put("customerId", record.get("customerId"));
				map.put("carId", record.get("carId"));
				if (Constant.toEmpty(record.get("amt"))){
					BigDecimal decimal = (BigDecimal)record.get("amt");
					map.put("amt", decimal.multiply(new BigDecimal(-1)));
				}
				map.put("type", 4);
				map.put("status", 1);
				map.put("eventType", "2");
				map.put("description","因將车辆"+record.get("licensePlateNumber")+"设置为不可用,故需要把充值记录依次清空;充值了"+map.get("amt")+"元");
				recordRechargeMapper.saveSingle(map);
			}
		}
	}
	
	/**
	 * 根据车主id查询车辆信息（第一辆）
	 * @author xiaowuge  
	 * @date 2018年9月19日  
	 * @version 1.0
	 */
	@Transactional
	public Map<String, Object> getCarByCustomerId(Map<String, Object> map){
		return carMapper.getCarByCustomerId(map);
	}
	
	public void editObservation()  throws Exception{
		
		List<Map<String, Object>> list = carMapper.selectCarAndUser();
		for(int i=0;i<list.size();i++){
			
			Map<String, Object> carMap = new HashMap<>();
			carMap.put("id",list.get(i).get("id"));
			carMap.put("typeGuarantee", list.get(i).get("typeGuarantee"));
			carMap.put("licensePlateNumber", list.get(i).get("licensePlateNumber"));
			carMap.put("openId", list.get(i).get("openId"));			
			carMap.put("customerId", list.get(i).get("customerId"));
			carMap.put("createAt",list.get(i).get("createAt"));

			//创建定时任务，指定时间后进入保障期
			String carId = String.valueOf(carMap.get("id"));
			SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = time.format(new Date());
			Long imestamp=new Date().getTime();

			Map<String,Object> map = new HashMap<>();
			map.put("jobName","observationJob_" + carId);
			map.put("carId",carId);
			map.put("typeGuarantee", carMap.get("typeGuarantee"));
			map.put("openid", carMap.get(CommonField.OPEN_ID));
			map.put("content", "爱车" + carMap.get("licensePlateNumber") + "加入车V互助成功，您爱车正享受互助保障中。");
			map.put("keyword1", "审核通过");
			map.put("keyword2", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
			map.put("url", Constant.toReadPro("realURL")+"hfive/view/rule_photo2.html");
			Scheduler sche = schedulerFactory.getScheduler();
			QuartzUtils.removeJob(sche,map.get("jobName").toString());
			//从数据库查询定时时间
			Map dicMap = new HashMap();
			dicMap.put("type","observationTime");
			Map dictionary = dictionaryService.findSingle(dicMap);
			//开启定时器
			Map<String, String> dateMap = DateUtils.getDateTime(carMap.get("createAt").toString(), new Long(dictionary.get("value").toString()));

			Long goTime = imestamp - time.parse(carMap.get("createAt").toString()).getTime();
			imestamp = imestamp+Long.valueOf(dictionary.get("value").toString()) - goTime;
			Date date1 = new Date(imestamp);
			String observationEndTime = time.format(date1);
			String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
			QuartzUtils.addJob(sche,map.get("jobName").toString(), ObservationJob.class, map, cron);

			usersService.addAmtCompensationByInvitation(String.valueOf(carMap.get("customerId")));
			eventService.updateDayNumber("observationNum",1);

			map.clear();
			map.put("observationEndTime",observationEndTime);
			map.put("id",carId);
			this.updateCar(map);

			try {
				map.clear();
				map.put("id",carId);
				Map<String, Object> m = carMapper.findCar(map);
				BigDecimal day = new BigDecimal(dictionary.get("value").toString()).divide(new BigDecimal("86400000"), 0, RoundingMode.HALF_UP);
				Map<String,String> rmap = new HashMap<String, String>();
				rmap.put("openid", String.valueOf(carMap.get(CommonField.OPEN_ID)));
				rmap.put("licensePlateNumber", String.valueOf(carMap.get("licensePlateNumber")));
				rmap.put("day", String.valueOf(day));
				if (Constant.toEmpty(carMap.get("inviterAmount"))){
					rmap.put("money",String.valueOf(carMap.get("inviterAmount")));
				}else {
					rmap.put("money", m.get("amtCooperation").toString());
				}
//				weiXinUtils.sendTemplate(11, rmap);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		

	}
	
	public void updateCompensateNum(Map<String, Object> map){
		carMapper.updateCompensateNum(map);
	}
	
	
}


