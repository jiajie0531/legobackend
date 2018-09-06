package com.delllogistics.wechat;

import com.delllogistics.service.order.OrderRefundService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**微信退款
 * Created by xzm on 2018-4-3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WxRefundServiceTest {

    @Autowired
    private OrderRefundService orderRefundService;

    @Test
    public void resultnotifyTest(){
        String xmlResult="<xml><return_code>SUCCESS</return_code><appid><![CDATA[wxe184298fc71118e4]]></appid><mch_id><![CDATA[1497713532]]></mch_id><nonce_str><![CDATA[49e9ece74034798c1e6445158c2fafdf]]></nonce_str><req_info><![CDATA[icxy1o7uUsVRkeZCFTwl3CiFdHXz0r3TuL7PQXQ/wfLXoyPkWwggqmZ3RRiEc5GKKmBdQQqTOobC3lCQ1z6o1e5XNJFdnZbzqmQRDM2w6SgiY9ZlWUgUpgHkyGWAj/Oxe9NCN5mqrQ+EFpG89cJ/5LiSGB64eFvnPkRh8IUtCqg26wA13TjvLXD/qsR03KOtbMXO8KwSWZMrEY7754uXiZDmAh6sb7wMJrZGSe4GpwqVj4zPawy48z5EPwjYVIAgsUxLRh4MSJYqhLZOSFKdxmrwtcFVZDucvcMDs6U0PW5VzJE0YgcKmR9Cxgw5i2GhxIUmE23q/wifnWcd1rCFqDFyUu17kicGob1MPy530lqjlGVtbNyEWb7OY7ikIIYj5DbM1kgONbdz36wapbykgwdDQXgIojXYcsx940pp8Ukz4e5APl8bxA51+dWnPhEHcnVz0CtJchmmQlpD387yKjyTUuN6pa1B1FGUdTb5ccfE1jmImKIb4xUTsaJ16URuJLIi2I02YhaO3pRMttWbYh+yseisc2Cc8RuwnzKU4oywpzn600zYxk3u7kFFPhOp9+z1XZUhFC7Vej8NrL+3UgwCzMJZme0PWwzdcF/CkHkxjkVAqdjylax2XR5WyRAmJINpyEvYEqi5If+QIipHaxvo+c/11ev5Oq6JNYBzRfXldOm9VkICrwewVxD9vl3W7TlOTvJuewgwr9DggIjwJE+L8uQiNo4bxt1sXKssLCi7n05Z7U1H+CmmPvpzPu/W3Fk1+k4Tve2L7Df233MegqmU8yHPt1LJfFygRovi7jzoesdByRbD1dj69ivnxIpXtBhs7C7qjD3C5ZCBYleUQw6ZzEbdE+s5LkLVNuP2hRLG6TwrYFKp0D+bfojYNe4ZECMg8xInWDRpQZCrXtnXvxwegKNfuaYcOWpSld7J2PdMUQH5hgwAJucPSCOl4hTEc0M9F/5lvahKADKgbSIvSGZ01PFpkX6zsynf1azHYsFq15hgA0f7hWAoJXoavudRCJI/ueFCAwqIFxpumfxZsv4Ttgx0vGLQypSJRpKywxo=]]></req_info></xml>";
        String resultnotify = orderRefundService.resultnotify(xmlResult);
        System.out.println(resultnotify);
    }
}
