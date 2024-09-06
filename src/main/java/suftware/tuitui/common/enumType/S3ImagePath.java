package suftware.tuitui.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum S3ImagePath {
    //  TimeCapsule
    CAPSULE("image/capsule/"),
    CAPSULE_THUMBNAIL("image/capsule/thumbnail/"),

    //  Profile
    PROFILE("image/profile/"),
    PROFILE_THUMBNAIL("image/profile/thumbnail");

    private final String path;
}
