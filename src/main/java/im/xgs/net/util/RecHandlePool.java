package im.xgs.net.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class RecHandlePool {

	private static Map<String,Object> recs = new ConcurrentHashMap<String, Object>();
	
	public static <T> T get(Class<T> clazz){
		try {
			if(recs.get(clazz.getName()) == null){
				recs.put(clazz.getName(), clazz.newInstance());
			}
			return (T)recs.get(clazz.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T get(String clazzName) throws ClassNotFoundException{
		Class<T> clazz = (Class<T>) Class.forName(clazzName);
		return get(clazz);
	}
}
