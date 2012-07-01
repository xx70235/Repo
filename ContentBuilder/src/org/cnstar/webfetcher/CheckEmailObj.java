package org.cnstar.webfetcher;
import java.io.IOException;

import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class CheckEmailObj {
 public static boolean checkEmail(String email) {
  if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
   System.err.println("Format error");
   return false;
  }

  String log = "";
  String host = "";
  String hostName = email.split("@")[1];
  Record[] result = null;
  SMTPClient client = new SMTPClient();

  try {
   // ����MX��¼
   Lookup lookup = new Lookup(hostName, Type.MX);
   lookup.run();
   if (lookup.getResult() != Lookup.SUCCESSFUL) {
    log += "�Ҳ���MX��¼\n";
    return false;
   } else {
    result = lookup.getAnswers();
   }
   
   // ���ӵ����������
   for (int i = 0; i < result.length; i++) {
    host = result[i].getAdditionalName().toString();
    client.connect(host);
    if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
     client.disconnect();
     continue;
    } else {
     log += "MX record about " + hostName + " exists.\n";
     log += "Connection succeeded to " + host + "\n";
     break;
    }
   }
   log += client.getReplyString();

   // HELO cyou-inc.com
   client.login("cyou-inc.com");
   log += ">HELO cyou-inc.com\n";
   log += "=" + client.getReplyString();

   // MAIL FROM: <zhaojinglun@cyou-inc.com>
   client.setSender("xx70235@gmail.com");
   log += ">MAIL FROM: <xx70235@gmail.com>\n";
   log += "=" + client.getReplyString();

   // RCPT TO: <$email>
   client.addRecipient(email);
   log += ">RCPT TO: <" + email + ">\n";
   log += "=" + client.getReplyString();
   
   if (250 == client.getReplyCode()) {
    return true;
   }
  } catch (Exception e) {
   e.printStackTrace();
  } finally {
   try {
    client.disconnect();
   } catch (IOException e) {
   }
   // ��ӡ��־
   System.err.println(log);
  }
  return false;
 }

 public static void main(String[] args) {
  System.err.println("Outcome: "
    + CheckEmailObj.checkEmail("erkofs@gmail.com"));
 }
}