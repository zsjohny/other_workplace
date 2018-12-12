package com.reliable.util

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

/**
 * Created by nessary on 16-5-31.
 */
class videoPlay {
    public static void main(String[] args) {

        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://v.youku.com/v_show/id_XMTU3MjYzODAzMg==.html");

        /*List<BasicNameValuePair> listName = new ArrayList<>();

        listName.add(new BasicNameValuePair("oauth_signature", "x8+CCvWqj4/wIJI2yj4mgRJ9kiY="));
        listName.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
        listName.add(
                new BasicNameValuePair("oauth_nonce", "F07875FA-9CC6-47EA-9F3E-FD7C5D6B265C"));
        listName.add(new BasicNameValuePair("x_auth_password", "dmjsns"));
        listName.add(new BasicNameValuePair("x_auth_model", "client_auth"));
        listName.add(new BasicNameValuePair("apiVersion", "2.8.0"));
        listName.add(new BasicNameValuePair("oauth_timestamp", "1464535023"));
        listName.add(new BasicNameValuePair("oauth_version", "1.0"));
        listName.add(new BasicNameValuePair("source", "028fa5cddf7e5130dfd35344299ffbba"));
        listName.add(new BasicNameValuePair("oauth_consumer_key",
                "028fa5cddf7e5130dfd35344299ffbba"));

        listName.add(new BasicNameValuePair("x_auth_username", "+86" + phone));

        post.setEntity(new UrlEncodedFormEntity(listName));*/
        HttpResponse execute = client.execute(post);


        String msg = EntityUtils.toString(execute.getEntity(), "utf-8");
        println msg


    }
}
