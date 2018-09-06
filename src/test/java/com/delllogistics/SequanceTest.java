package com.delllogistics;

import com.delllogistics.sequence.Sequence;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** 微信支付结果解析测试
 * Created by xzm on 2018-4-2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SequanceTest {

    @Autowired
    private Sequence sequence;

    @Test
    public void getSequence() throws WxPayException {
        for (int i=0;i<500;i++){
            System.out.println(""+sequence.nextId());
        }

    }
}
