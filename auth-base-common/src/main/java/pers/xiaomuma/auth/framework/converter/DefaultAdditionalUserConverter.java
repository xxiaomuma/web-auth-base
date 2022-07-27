package pers.xiaomuma.auth.framework.converter;

import lombok.SneakyThrows;
import pers.xiaomuma.auth.framework.constant.AuthConstant;
import pers.xiaomuma.auth.framework.user.AdditionalUser;
import java.util.HashMap;
import java.util.Map;


public class DefaultAdditionalUserConverter implements AdditionalUserConverter {

    @Override
    public Map<String, ?> convert2Map(AdditionalUser user) {
        Map<String, Object> map = new HashMap<>();
        map.put(AuthConstant.USER_ID, user.getUserId());
        return map;
    }

    @Override
    @SneakyThrows
    public AdditionalUser convert2Entity(Map<String, ?> map) {
        AdditionalUser user = new AdditionalUser();
        user.setUserId((Integer) map.get(AuthConstant.USER_ID));
        return user;
    }
}
