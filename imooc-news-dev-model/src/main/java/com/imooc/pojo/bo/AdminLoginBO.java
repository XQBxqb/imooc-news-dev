package com.imooc.pojo.bo;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @author 昴星
 * @date 2023-04-01 10:45
 * @explain
 */
public class AdminLoginBO {
        private String username;

        private String password;

        private String img64;

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getImg64() {
                return img64;
        }

        public void setImg64(String img64) {
                this.img64 = img64;
        }
}
