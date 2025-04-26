package org.example.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "org.example.userservice",  // 自己的專案包
        "org.example.auth"          // auth-common 裡面的類（JwtService / JwtAuthenticationFilter）
})
class UserServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
