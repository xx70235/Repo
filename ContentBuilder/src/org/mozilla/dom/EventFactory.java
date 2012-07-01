package org.mozilla.dom;

import org.mozilla.dom.events.KeyEventImpl;
import org.mozilla.dom.events.MouseEventImpl;
import org.w3c.dom.events.Event;
import org.mozilla.interfaces.*;

public class EventFactory
{
    public static final int MOUSE_EVENT = 0x01;
    public static final int KEY_EVENT = 0x02;    
    
    private EventFactory() {}
   
    public static Event getNodeInstance(nsIDOMEvent ev, int evType)
    {
        if (ev == null) {
			return null;
		}
            
        switch (evType)
        {
            case MOUSE_EVENT:
                return MouseEventImpl.getDOMInstance((nsIDOMMouseEvent) ev.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID));        
            case KEY_EVENT:
                return KeyEventImpl.getDOMInstance((nsIDOMKeyEvent) ev.queryInterface(nsIDOMKeyEvent.NS_IDOMKEYEVENT_IID));        
            default:
                return null;
        }
    }
}
