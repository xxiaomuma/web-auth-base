package pers.xiaomuma.auth.framework.auth.server.authentication;

public interface UserConnectionRepository {

    UserConnection getConnection(String connectId);
}
