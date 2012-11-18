package com.megadevs.nostradamus.cameramonitor.reko;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.mashape.client.authentication.Authentication;
import com.mashape.client.authentication.MashapeAuthentication;
import com.mashape.client.http.ContentType;
import com.mashape.client.http.HttpClient;
import com.mashape.client.http.HttpMethod;
import com.mashape.client.http.MashapeCallback;
import com.mashape.client.http.MashapeResponse;
import com.mashape.client.http.ResponseType;

public class FaceAndSceneRecognitionProvidedByReKognitioncom {

	private final static String PUBLIC_DNS = "orbeus-rekognition.p.mashape.com";
    private List<Authentication> authenticationHandlers;

    public FaceAndSceneRecognitionProvidedByReKognitioncom (String publicKey, String privateKey) {
        authenticationHandlers = new LinkedList<Authentication>();
        authenticationHandlers.add(new MashapeAuthentication(publicKey, privateKey));
        
    }
    
    /**
     * Synchronous call with optional parameters.
     */
    public MashapeResponse<JSONArray> face(String api_key, String api_secret, String jobs, String name_space, String urls, String user_id, String access_token, String fb_id) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        if (api_key != null && !api_key.equals("")) {
	parameters.put("api_key", api_key);
        }
        
        
        
        if (api_secret != null && !api_secret.equals("")) {
	parameters.put("api_secret", api_secret);
        }
        
        
        
        if (jobs != null && !jobs.equals("")) {
	parameters.put("jobs", jobs);
        }
        
        
        
        if (name_space != null && !name_space.equals("")) {
	parameters.put("name_space", name_space);
        }
        
        
        
        if (urls != null && !urls.equals("")) {
	parameters.put("urls", urls);
        }
        
        
        
        if (user_id != null && !user_id.equals("")) {
	parameters.put("user_id", user_id);
        }
        
        
        
        if (access_token != null && !access_token.equals("")) {
	parameters.put("access_token", access_token);
        }
        
        
        
        if (fb_id != null && !fb_id.equals("")) {
	parameters.put("fb_id", fb_id);
        }
        
        
        
        return (MashapeResponse<JSONArray>) HttpClient.doRequest(JSONArray.class,
                    HttpMethod.GET,
                    "https://" + PUBLIC_DNS + "/",
                    parameters,
                    ContentType.FORM,
                    ResponseType.JSON,
                    authenticationHandlers);
    }

    /**
     * Synchronous call without optional parameters.
     */
    public MashapeResponse<JSONArray> face(String api_key, String api_secret, String jobs) {
        return face(api_key, api_secret, jobs, "", "", "", "", "");
    }


    /**
     * Asynchronous call with optional parameters.
     */
    public Thread face(String api_key, String api_secret, String jobs, String name_space, String urls, String user_id, String access_token, String fb_id, MashapeCallback<JSONArray> callback) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        
        if (api_key != null && !api_key.equals("")) {
        
            parameters.put("api_key", api_key);
        }
        
        
        
        if (api_secret != null && !api_secret.equals("")) {
        
            parameters.put("api_secret", api_secret);
        }
        
        
        
        if (jobs != null && !jobs.equals("")) {
        
            parameters.put("jobs", jobs);
        }
        
        
        
        if (name_space != null && !name_space.equals("")) {
        
            parameters.put("name_space", name_space);
        }
        
        
        
        if (urls != null && !urls.equals("")) {
        
            parameters.put("urls", urls);
        }
        
        
        
        if (user_id != null && !user_id.equals("")) {
        
            parameters.put("user_id", user_id);
        }
        
        
        
        if (access_token != null && !access_token.equals("")) {
        
            parameters.put("access_token", access_token);
        }
        
        
        
        if (fb_id != null && !fb_id.equals("")) {
        
            parameters.put("fb_id", fb_id);
        }
        
        
        return HttpClient.doRequest(JSONArray.class,
                    HttpMethod.GET,
                    "https://" + PUBLIC_DNS + "/",
                    parameters,
                    ContentType.FORM,
                    ResponseType.JSON,
                    authenticationHandlers,
                    callback);
    }

    /**
     * Asynchronous call without optional parameters.
     */
    public Thread face(String api_key, String api_secret, String jobs, MashapeCallback<JSONArray> callback) {
        return face(api_key, api_secret, jobs, "", "", "", "", "", callback);
    }

}