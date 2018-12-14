package com.zccbh.demand.service.customer;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.customer.ComplaintMapper;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ComplaintService {
	@Autowired
	private ComplaintMapper complaintMapper;

	/**
	 * 创建投诉订单
	 * @param paramModelMap 投诉信息
	 * @return 添加结果信息
	 * @throws Exception
	 */
	@Transactional
	public String createComplaint(Map<String, Object> paramModelMap) throws Exception {
		complaintMapper.saveSingle(paramModelMap);
		return "0";
	}

	/**
	 * 修改投诉订单
	 * @param paramModelMap 投诉信息
	 * @return 结果信息
	 * @throws Exception
	 */
	@Transactional
	public String updateComplaint(Map<String, Object> paramModelMap) throws Exception {
		complaintMapper.updateModel(paramModelMap);
		return "0";
	}

	/**
	 * 查询投诉列表
	 * @param paramModelMap 查询条件
	 * @return 投诉列表
	 * @throws Exception
	 */
	public PageInfo<Map<String, Object>> findComplaint(Map<String, Object> paramModelMap) throws Exception {
		int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
		int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>> complaintList = complaintMapper.findMore(paramModelMap);
		PageInfo<Map<String, Object>> complaintInfo = new PageInfo<>(complaintList);
		return complaintInfo;
	}

	/**
	 * 查询投诉详情
	 * @param paramModelMap 查询条件
	 * @return 投诉详情
	 * @throws Exception
	 */
	public Map<String, Object> findComplaintDetail(Map<String, Object> paramModelMap) throws Exception {
		return complaintMapper.findSingle(paramModelMap);
	}

	/**
	 * 查询投诉数量
	 * @throws Exception
	 */
	public Map<String,Object> findComplaintCount(Map<String, Object> paramModelMap) throws Exception{
		return complaintMapper.findCount(paramModelMap);
	}
}
