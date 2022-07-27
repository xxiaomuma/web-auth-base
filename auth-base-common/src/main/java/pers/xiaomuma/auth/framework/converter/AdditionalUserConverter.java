package pers.xiaomuma.auth.framework.converter;


import pers.xiaomuma.auth.framework.user.AdditionalUser;

import java.util.Map;

public interface AdditionalUserConverter {

    Map<String, ?> convert2Map(AdditionalUser user);

    AdditionalUser convert2Entity(Map<String, ?> map);
}
