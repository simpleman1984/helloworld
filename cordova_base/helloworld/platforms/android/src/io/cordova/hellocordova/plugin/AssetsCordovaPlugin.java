package io.cordova.hellocordova.plugin;

import android.net.Uri;

import org.apache.cordova.CordovaPlugin;

/**
 * Created by Administrator on 2016/11/27.
 */

public class AssetsCordovaPlugin extends CordovaPlugin {
    /**
     *
     * @param uri
     * @return
     */
    public Uri remapUri(Uri uri) {
        //如果当前目录，不是从assets中读取，则强制修改为从assets中读取；
        //最合理的改法，应该放到上面的remapUri方法中，以后再修改吧。
        String remapperUrl = uri.getPath();
        if (!remapperUrl.startsWith("/android_asset/")) {
            remapperUrl = "file:///android_asset/www" + remapperUrl;
        }
        uri = Uri.parse(remapperUrl);

        return uri;
    }

}
