package org.mozilla.dom;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

public class WeakValueHashMap extends LinkedHashMap 
{
	private ReferenceQueue reaped = new ReferenceQueue();
	
	private static class ValueRef extends WeakReference  
    {
		private final Object key;
		
		ValueRef(Object val, Object key, ReferenceQueue q)
        {
			super(val, q);
			this.key = key;
		}
	}
	
	@Override
	public Object put(Object key, Object value) 
    {
		reap();
		ValueRef vr = new ValueRef(value, key, reaped);
		return super.put(key, vr);
	}
	
	@Override
	public Object get(Object key)
    {
		reap();
		ValueRef vr = (ValueRef) super.get(key);
        Object result = null;
		if (vr != null) {
			result = vr.get();
		}
        return result;
	}
	
	@Override
	public Object remove(Object key) 
    {
		reap();
		ValueRef vr = (ValueRef) super.get(key);
		if (vr == null) 
        {
			return null;
		} 
        else 
        {
			vr.clear();
			super.remove(key);
			return null;
		}
	}
	
	@Override
	public int size()
    {
		reap();
		return super.size();
	}
	
	public void reap()
    {
		ValueRef ref;
		while ((ref = (ValueRef) reaped.poll()) != null)
        {
			super.remove(ref.key);
		}
	}
}
