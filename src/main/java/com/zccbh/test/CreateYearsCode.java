package com.zccbh.test;

import com.zccbh.demand.mapper.activities.PackYearsCodeMapper;
import com.zccbh.util.base.UUIDCreator;
import com.zccbh.util.collect.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/zccbh/config/spring.xml")
public class CreateYearsCode {

    @Autowired
    PackYearsCodeMapper packYearsCodeMapper;

//    @Test
    public void create() throws Exception {

        //  生成uuid
        Map<String,Object> code = new HashMap<>();
        code.put("use_status",0);
        for (int i = 0; i < 1000; i++) {
            String  uuid = MD5Util.getMD5Code(UUIDCreator.getUUID()+"chevhuzhu_");
            code.put("uuid",uuid);
            packYearsCodeMapper.saveSingle(code);
        }

    }

}
