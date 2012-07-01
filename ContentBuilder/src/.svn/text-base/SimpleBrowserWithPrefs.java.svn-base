import org.mozilla.interfaces.nsIPrefBranch;
import org.mozilla.interfaces.nsIPrefService;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.xpcom.Mozilla;


public class SimpleBrowserWithPrefs
{
	String contractID = "@mozilla.org/preferences-service;1";
	nsIServiceManager serviceManager = Mozilla.getInstance().getServiceManager();
	nsIPrefService prefService = (nsIPrefService)serviceManager.getServiceByContractID(contractID, nsIPrefService.NS_IPREFSERVICE_IID);
	nsIPrefBranch branch = prefService.getBranch("");

}
