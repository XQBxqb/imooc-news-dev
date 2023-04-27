package com.imooc.api.wapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 昴星
 * @date 2023-04-11 20:00
 * @explain
 */
public class FriendLinkMngSaveMOWrapper extends HttpServletRequestWrapper {

    private final String body;
    /**
     * @description:这里也能使用request.getReader();
     * @param request
     * @return:
     * @author: 昴星
     * @time: 2023/4/15 11:25
     */
    public FriendLinkMngSaveMOWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream is = null;
        StringBuilder sb = null;
        try {
            is = request.getInputStream();
            sb = new StringBuilder();
            byte[] b = new byte[4096];
            for (int n ; (n = is.read(b)) != -1;)
            {
                sb.append(new String(b, 0, n));
            }
        } finally {
            if(is != null) {
                is.close();
            }
        }
        body = sb.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
            @Override
            public int read() throws IOException {

                return bais.read();

            }

            @Override
            public void close() throws IOException {
                System.out.println(1);
                bais.close();
            }
        };
    }

    public String getBody() {
        return body;
    }
}
