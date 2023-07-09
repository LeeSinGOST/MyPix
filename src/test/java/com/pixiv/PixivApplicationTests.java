package com.pixiv;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.pixiv.user.Others;
@SpringBootTest
class PixivApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(Others.getDefcol());
    }

}
