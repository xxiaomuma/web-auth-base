package pers.xiaomuma.auth.framework.auth.server.authentication.social;

public enum SocialType {

    WX("wx");

    private String name;

    SocialType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
