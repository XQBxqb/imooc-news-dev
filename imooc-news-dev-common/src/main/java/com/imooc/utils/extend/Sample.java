package com.imooc.utils.extend;

/**
 * @author 昴星
 * @date 2023-04-07 21:37
 * @explain
 */

// 1、这里只是以ocr下的RecognizeBankCard能力为例，其他能力请引入相应类目的包和相关类。包名可参考本文档上方的SDK包名称，能力名可参考对应API文档中的Action参数。例如您想使用通用分割，其文档为https://help.aliyun.com/document_detail/151960.html，可以知道该能力属于分割抠图类目，能力名称为SegmentCommonImage，那么您需要将代码中ocr20191230改为imageseg20191230，将RecognizeBankCard改为SegmentCommonImage。
import com.aliyun.ocr20191230.Client;
import com.aliyun.ocr20191230.models.RecognizeBankCardAdvanceRequest;
import com.aliyun.ocr20191230.models.RecognizeBankCardResponse;
import com.aliyun.tea.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Sample {

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    @Autowired
    private AliyunResource aliyunResource;
    // 这里只是以ocr为例，其他能力请使用相应类目的包下面的Client类
    public  Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 2、访问的域名。注意：这个地方需要求改为相应类目的域名，参考：https://help.aliyun.com/document_detail/143103.html
        config.endpoint = "facebody-vpc.cn-shanghai.aliyuncs.com";
        // 3、这里只是以ocr为例，其他能力请使用相应类目的包下面的Client类
        return new Client(config);
    }

    public  void verifyFace(File file) throws Exception {
        // 4、"YOUR_ACCESS_KEY_ID", "YOUR_ACCESS_KEY_SECRET" 的生成请参考https://help.aliyun.com/document_detail/175144.html
        // 如果您是用的子账号AccessKey，还需要为子账号授予权限AliyunVIAPIFullAccess，请参考https://help.aliyun.com/document_detail/145025.html
        // 这里只是以ocr为例，其他能力请使用相应类目的包下面的Client类
        Client client = createClient(aliyunResource.getAccessKeyId(), aliyunResource.getAccessKeySecret());
        //使用本地文件
        InputStream inputStream = new FileInputStream(file);
        // 5、这里只是以ocr下的RecognizeBankCard为例，其他能力请使用相应类目的包和类，具体入参设置需要参考具体能力的文档
        RecognizeBankCardAdvanceRequest recognizeBankCardAdvanceRequest = new RecognizeBankCardAdvanceRequest()
                .setImageURLObject(inputStream);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 6、这里只是以ocr下的RecognizeBankCard为例，其他能力请使用相应类目的包和类，注意，recognizeBankCardAdvance方法名也需要改成对应能力的方法名。方法名是根据能力名称按照一定规范形成的，如能力名称为SegmentCommonImage，对应方法名应该为segmentCommonImageAdvance。
            RecognizeBankCardResponse resp = client.recognizeBankCardAdvance(recognizeBankCardAdvanceRequest, runtime);
            // 获取整体结果。部分能力会输出url链接，通过toJSONString转换后可能有编码问题，但是通过单个字段获取是没问题的。
            System.out.println(com.aliyun.teautil.Common.toJSONString(TeaModel.buildMap(resp)));
            // 获取单个字段，这里只是一个例子，具体能力下的字段需要看具体能力的文档
            System.out.println(resp.getBody().getData().getCardNumber());
        } catch (com.aliyun.tea.TeaException teaException) {
            // 获取整体报错信息
            System.out.println(com.aliyun.teautil.Common.toJSONString(teaException));
            // 获取单个字段
            System.out.println(teaException.getCode());
        }
    }
}
