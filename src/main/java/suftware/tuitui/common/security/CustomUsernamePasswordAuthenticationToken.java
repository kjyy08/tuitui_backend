package suftware.tuitui.common.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import suftware.tuitui.domain.Profile;

import java.util.Collection;

public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Profile profile;

    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Profile profile) {
        super(principal, credentials, authorities);
        this.profile = profile;
    }

    public Integer getProfileId(){
        return profile.getProfileId();
    }

    public String getNickname(){
        return profile.getNickname();
    }
}
