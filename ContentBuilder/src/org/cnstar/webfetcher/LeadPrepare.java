package org.cnstar.webfetcher;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.cnstar.utils.MD5;
import org.cnstar.webfetcher.model.GroupModel;
import org.cnstar.webfetcher.model.LeadProfileModel;

public class LeadPrepare {

	HttpClient httpClient;
	HttpContext localContext;
	List<Cookie> cookies;
	List<String> fileList;
	Set<String> folderSet;

	public LeadPrepare() {
		httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient.getParams().setParameter(
				CookieSpecPNames.SINGLE_COOKIE_HEADER, true);
		cookies = new ArrayList<Cookie>();
		fileList = new ArrayList<String>();
		folderSet = new LinkedHashSet<String>();
		localContext = new BasicHttpContext();
	}

	protected boolean postData(List<NameValuePair> nvps, String url) {
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);
			cookies = ((DefaultHttpClient) httpClient).getCookieStore()
					.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
				return false;
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	protected String getLeadProfile(String url)
	{
	    return getInfo(url);
	}
	
	protected String uploadSuccessInfo(String url,String encode) {
	    return getInfo(url);
	    
	}
	
	protected String getInfo(String url)
	{
	    HttpGet httpGet = new HttpGet(url);
	    String content = null;
		HttpResponse response = null;
		try {
		    response = httpClient.execute(httpGet);
		    HttpEntity entity = response.getEntity();
			if (entity != null) {
				content = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
		}
		}catch (ClientProtocolException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	
		return content;
	}

	protected void getZipFiles(String url) {
		HttpGet httpGet = new HttpGet(url);
		try {
			BufferedOutputStream dest = null;
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			ZipInputStream zis = new ZipInputStream(entity.getContent());
			ZipEntry entry;
			File folder = new File("Ad_Groups");
			folder.mkdir();
			while ((entry = zis.getNextEntry()) != null) {
				int count;
				byte data[] = new byte[2048];
				File newFile = new File(folder,entry.getName());
				if(entry.isDirectory())
				{
					newFile.mkdir();
					folderSet.add("Ad_Groups/"+entry.toString().split("/")[0]);
					continue;
				}
				else
				{
					fileList.add("Ad_Groups/"+entry.toString());
				}
				// write the files to the disk
				FileOutputStream fos = new FileOutputStream("Ad_Groups/"+entry.getName());
				dest = new BufferedOutputStream(fos, 2048);
				while ((count = zis.read(data, 0, 2048)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zis.close();
			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从文件中读取登录信息，并登录
	 * 
	 * @param loginFilePath
	 * @return 登录是否成功
	 */
	protected boolean login(String loginFilePath) {
		List<String> loginPair = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					loginFilePath)));
			String line;
			while ((line = br.readLine()) != null) {
				loginPair.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MD5 md5 = new MD5();
		String password = md5.md5s32(loginPair.get(1));
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		nvps.add(new BasicNameValuePair("username", loginPair.get(0)));
		nvps.add(new BasicNameValuePair("mdpass", password));
		nvps.add(new BasicNameValuePair("login", "1"));
		nvps.add(new BasicNameValuePair("savesess", "y"));
		String loginUrl = "http://lead.djx1981.com/login.php";
		boolean isLoginOK = postData(nvps, loginUrl);
		return isLoginOK;

	}

	public static void main(String[] args) {
		LeadPrepare operation = new LeadPrepare();
		boolean isLoginOK = operation.login("login.txt");
		if (isLoginOK) {
			operation.getZipFiles("http://lead.djx1981.com/login.php?action=clientad&sub=downad");
		}
	}
	
	public List<String> getFileList() {
		return fileList;
	}


	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	public Set<String> getFolderSet() {
		return folderSet;
	}

	public void setFolderSet(Set<String> folderSet) {
		this.folderSet = folderSet;
	}
}
