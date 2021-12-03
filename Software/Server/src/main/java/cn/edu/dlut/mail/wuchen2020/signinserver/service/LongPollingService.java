package cn.edu.dlut.mail.wuchen2020.signinserver.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

/**
 * 用长轮询推送消息的服务
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.IndexController}
 * 
 * @author Wu Chen
 */
@Service
public class LongPollingService {
    private final Multimap<String, DeferredResult<Object>> requestMap = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    
    // 这个方法的命名是和Android学的, 因为我发现Spring的DeferredResult和Android的PendingResult好像啊
    public DeferredResult<Object> goAsync(String id) {
        DeferredResult<Object> deferredResult = new DeferredResult<>();
        deferredResult.onCompletion(new Runnable() {
            @Override
            public void run() {
                requestMap.remove(id, deferredResult);
            }
        });
        requestMap.put(id, deferredResult);
        return deferredResult;
    }

    public void postMessage(String id, Object message) {
        if (requestMap.containsKey(id)) {
            for (DeferredResult<Object> deferredResult : requestMap.get(id)) {
                deferredResult.setResult(message);
            }
        }
    }
}
