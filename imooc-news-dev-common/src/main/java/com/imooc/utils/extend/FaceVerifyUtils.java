package com.imooc.utils.extend;

import com.aliyun.tea.TeaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 昴星
 * @date 2023-04-05 17:36
 * @explain
 */

@Component
public class FaceVerifyUtils {
    @Autowired
    AliyunResource aliyunResource;

        /**
         * 使用AK&SK初始化账号Client
         * @param accessKeyId
         * @param accessKeySecret
         * @return Client
         * @throws Exception
         */
        public  com.aliyun.facebody20191230.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
            com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                    // 必填，您的 AccessKey ID
                    .setAccessKeyId(accessKeyId)
                    // 必填，您的 AccessKey Secret
                    .setAccessKeySecret(accessKeySecret);
            // 访问的域名
            config.endpoint = "facebody.cn-shanghai.aliyuncs.com";
            return new com.aliyun.facebody20191230.Client(config);
        }

        public  Boolean faceVerify(String base64A,String base64B)  {
            // 工程代码泄露可能会导致AccessKey泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
            com.aliyun.facebody20191230.Client client = null;
            try {
                client = createClient(aliyunResource.getAccessKeyId(), aliyunResource.getAccessKeySecret());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            com.aliyun.facebody20191230.models.CompareFaceRequest compareFaceRequest = new com.aliyun.facebody20191230.models.CompareFaceRequest()
                    .setImageDataA(base64A)
                    .setImageDataB(base64B);
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            try {
                // 复制代码运行请自行打印 API 的返回值
                client.compareFaceWithOptions(compareFaceRequest, runtime);
            } catch (TeaException error) {
                // 如有需要，请打印 error
                com.aliyun.teautil.Common.assertAsString(error.message);
            } catch (Exception _error) {
                TeaException error = new TeaException(_error.getMessage(), _error);
                // 如有需要，请打印 error
                com.aliyun.teautil.Common.assertAsString(error.message);
            }
            return true;
    }
}
