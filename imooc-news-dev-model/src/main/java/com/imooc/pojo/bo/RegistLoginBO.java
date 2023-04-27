package com.imooc.pojo.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**BO业务对象，可以由Service层输出的封装业务逻辑的对象,这里其实可以用VO一般没啥问题
 * @author 昴星
 * @date 2023-03-14 18:20
 * @explain
 */

public class RegistLoginBO {
    //notNull是自动检测传送过来的对象，进行判断，如果为空就返回mes
    @NotBlank(message="手机号不能为空")
    //NOTblank和notnull区别，notnull只能判断为null不能判断为""和" "
    private String mobile;
    @NotBlank(message = "验证码不能为空")
    private String smsCode;

    public RegistLoginBO() {
   }

    public RegistLoginBO(String mobile, String smsCode) {
        this.mobile = mobile;
        this.smsCode = smsCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
