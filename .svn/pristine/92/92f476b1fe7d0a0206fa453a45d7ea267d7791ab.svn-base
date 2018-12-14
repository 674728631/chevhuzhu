package com.zccbh.demand.service.order;

import com.zccbh.demand.mapper.order.RecordPaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RecordPaymentService {
	@Autowired
	private RecordPaymentMapper recordPaymentMapper;

	/**
	 * 创建保险理赔支付记录
	 */
	@Transactional
	public void saveRecordPayment(Map<String, Object> paramModelMap) throws Exception{
		recordPaymentMapper.saveSingle(paramModelMap);
	}

	/**
	 * 修改保险理赔支付记录
	 */
	@Transactional
	public void updateRecordPayment(Map<String, Object> paramModelMap) throws Exception {
		recordPaymentMapper.updateModel(paramModelMap);
	}
}
