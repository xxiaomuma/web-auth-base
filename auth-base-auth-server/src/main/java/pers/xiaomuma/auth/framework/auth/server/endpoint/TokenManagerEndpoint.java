package pers.xiaomuma.auth.framework.auth.server.endpoint;


import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.*;
import pers.xiaomuma.auth.framework.auth.server.store.CustomTokenStore;
import pers.xiaomuma.auth.framework.auth.server.store.FindKeysResult;
import pers.xiaomuma.framework.page.PageResult;
import pers.xiaomuma.framework.response.BaseResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static cn.hutool.core.map.MapUtil.getInt;


@FrameworkEndpoint
public class TokenManagerEndpoint {

    private CustomTokenStore tokenStore;

    @DeleteMapping("/{token}")
    public BaseResponse<Void> removeToken(@PathVariable("token") String token) {
        return this.removeTokenFromTokenStore(token);
    }

    @PostMapping("/page")
    public BaseResponse<PageResult<Map<String, String>>> getTokenPage(@RequestBody Map<String, Object> params,
                                                                      @RequestHeader(required = false) String from) {
        if (StrUtil.isBlank(from)) {
            return null;
        }
        List<Map<String, String>> list = new ArrayList<>();
        Integer current = getInt(params, "current");
        Integer size = getInt(params, "size");
        if ((current != null && current <= 0) || (size != null && size <= 0)) {
            params.put("current", 1);
            params.put("size", 20);
        }
        //根据分页参数获取对应数据
        FindKeysResult result = tokenStore.findKeysForPage(current, size);

        List<String> accessKeys = result.getAccessKeys();

        for (String accessKey : accessKeys) {
            OAuth2AccessToken token = tokenStore.readAccessToken(accessKey);

            Map<String, String> map = Maps.newHashMapWithExpectedSize(8);


            map.put(OAuth2AccessToken.TOKEN_TYPE, token.getTokenType());
            map.put(OAuth2AccessToken.ACCESS_TOKEN, token.getValue());
            map.put(OAuth2AccessToken.EXPIRES_IN, token.getExpiresIn() + "");


            OAuth2Authentication oAuth2Auth = tokenStore.readAuthentication(token);
            Authentication authentication = oAuth2Auth.getUserAuthentication();

            map.put(OAuth2Utils.CLIENT_ID, oAuth2Auth.getOAuth2Request().getClientId());
            map.put(OAuth2Utils.GRANT_TYPE, oAuth2Auth.getOAuth2Request().getGrantType());

            list.add(map);
        }
        PageResult<Map<String, String>> pageRecord = new PageResult<>(current, size);
        pageRecord.setTotal(result.getCount());
        pageRecord.setRecords(list);
        return BaseResponse.success(pageRecord);
    }


    public CustomTokenStore getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(CustomTokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    private BaseResponse<Void> removeTokenFromTokenStore(String tokenValue) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
            return BaseResponse.success();
        }
        tokenStore.removeAccessToken(accessToken);
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);
        return BaseResponse.success();
    }

}
