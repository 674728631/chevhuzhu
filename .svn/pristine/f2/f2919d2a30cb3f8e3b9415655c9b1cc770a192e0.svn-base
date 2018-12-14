package com.zccbh.demand.service.customer;

import com.zccbh.demand.mapper.customer.InvitationTempMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class InvitationTempService {

    @Autowired
    private InvitationTempMapper invitationTempMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateModel(Map<String, Object> saveMap) throws Exception {
        return invitationTempMapper.updateModel(saveMap);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int saveSingle(Map<String, Object> saveMap) throws Exception {
        return invitationTempMapper.saveSingle(saveMap);
    }

    public Map<String, Object> findSingle(Map<String, Object> map) throws Exception {
        return invitationTempMapper.findSingle(map);
    }
}
