package com.github.xgameenginee.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息处理管理器
 * 
 * @author Steven
 * 
 */
public class GameHandlerManager {

    private static GameHandlerManager instance   = new GameHandlerManager();

    private GameHandler[]             _handlers;

    private Map<Short, GameHandler>   handlerMap = new HashMap<Short, GameHandler>();

    /**
     * 设置协议处理器的大小
     * 
     * @param size
     */
    public void initHandlers(int count) {
        if (count <= 0) {
            new IllegalArgumentException("count cannot less than zero");
        }
        _handlers = new GameHandler[count];
    }

    /**
     * 注册一个协议
     * 
     * @param cls
     * @param id
     * @return 返回true代表从缓存中拿出，false新注册的
     * @throws Exception
     */
    public boolean register(Class<? extends GameHandler> cls, short id) throws Exception {
        assert (id > 0 && id < _handlers.length);
        // assert (_handlers[id] == null);
        GameHandler handler = handlerMap.get(id);
        if (handler != null) {
            _handlers[id] = handler;
            handlerMap.remove(id);
            return true;
        } else {
            if (_handlers[id] != null)
                throw new Exception("Handler " + id + " has been existed! please check your code!");
            _handlers[id] = cls.newInstance();
        }
        return false;
    }

    /**
     * 替换一个协议
     * 
     * @param cls
     * @param id
     * @return 返回true代表从缓存中拿出，false新注册的
     * @throws Exception
     */
    public boolean replace(short id, GameHandler obj) throws Exception {
        assert (id > 0 && id < _handlers.length);
        if (_handlers[id] != null) {
            // Class<? extends IHandler> cls = (Class<? extends IHandler>)
            // HotswapTools.getObjectFromStr(NetConfig.getInstance().getHotfixPath(),
            // _handlers[id].getClass().getName());
            _handlers[id] = obj;
            return true;
        }
        return false;
    }

    /**
     * 注销协议
     * 
     * @param id
     *            协议编号
     * @return 如果注销成功返回true，失败false
     */
    public boolean unregister(short id) {
        GameHandler handler = _handlers[id];
        if (handler != null) {
            handlerMap.put(id, handler);
            _handlers[id] = null;
            return true;
        }
        return false;
    }

    public GameHandler getHandler(short id) {
        if ((id >= 0) && (id < _handlers.length))
            return _handlers[id];
        else
            return null;
    }

    public static GameHandlerManager getInstance() {
        return instance;
    }
}
