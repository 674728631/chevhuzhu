/**   
 * @author xiaowuge  
 * @date 2018年9月28日  
 * @version 1.0  
 */ 
package com.zccbh.demand.mapper.activities;

import java.util.List;
import java.util.Map;

import com.zccbh.demand.mapper.BaseMapper;

/** 
 * @ClassName: GiftMapper 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年9月28日 下午2:19:15 
 *  
 */
public interface GiftMapper extends BaseMapper{
	
	List<Map<String, Object>> selectByDrawNum(Map<String, Object> map);

}
