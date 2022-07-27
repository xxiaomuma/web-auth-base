package pers.xiaomuma.auth.framework.auth.client.provider;


import pers.xiaomuma.auth.framework.auth.client.entity.AuthCredential;
import java.util.Map;

public interface AuthCredentialConverter {

	AuthCredential convert2Credential(Map<String, ?> response);

}
