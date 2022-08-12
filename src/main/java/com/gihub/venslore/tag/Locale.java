package com.gihub.venslore.tag;

import com.gihub.venslore.tag.utilities.chat.Color;
import com.gihub.venslore.tag.utilities.file.ConfigFile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Locale {
    TAGS_NO_ACCESS("ERROR.PERMISSION.TAGS-NO-ACCESS", "&cYou don't have permission to use this Tag&c. {0}&fPurchase a rank on store to grant you access!"),
    USE_NUMBERS("ERROR.GENERAL.USE_NUMBERS", "&cPlease use numbers."),
    TAGS_DONT_HAVE_APPLIED("ERROR.GENERAL.TAGS.DONT-HAVE-APPLIED", "&cYou don't have a tag applied!"),
    TAGS_ALREADY_HAVE_APPLIED("ERROR.GENERAL.TAGS.ALREADY-HAVE-APPLIED", "&cYour tag is already set to &4<tag>&c!"),
    TAG_ALREADY_CREATED("TAGS.COMMAND.ALREADY-CREATED", "&aTag &e<name> &ais already created."),
    TAG_CREATED("TAGS.COMMAND.CREATED", "&aTag &e<name> &ahas been successfully created."),
    TAG_DONT_EXISTS("TAGS.COMMAND.DONT-EXISTS", "&cTag &f<name> &cdoes not exists!"),
    TAGS_TAG_REMOVE("TAGS.TAG-REMOVED", "&aYou have removed your tag."),
    TAG_DELETED("TAGS.COMMAND.DELETED", "&aTag &f<name> &ahas been deleted."),
    TAG_INVALID_COLOR("TAGS.COMMAND.INVALID-COLOR", "&cYou've entered wrong chat color."),
    TAG_PREFIX_SET("TAGS.COMMAND.PREFIX-SET", "&aPrefix for &f<name> &atag has been updated to &f<prefix>&a"),
    TAG_TYPE_SET("TAGS.COMMAND.TYPE-SET", "&aType for &f<name> &atag has been updated to &f<type>&a"),
    TAG_COLOR_SET("TAGS.COMMAND.COLOR-SET", "&aColor for &f<name> &atag has been updated to &f<color>&a"),
    TAGS_TAG_APPLIED("TAGS.TAG-APPLIED", "&fYou have selected the &c<tag> &ftag."),

    END("", "");

    @Getter
    private final String path;
    @Getter
    private final String value;
    @Getter
    private final List<String> listValue;

    private final ConfigFile configFile = TagPlugin.INSTANCE.getLanguage();

    Locale(String path, String value) {
        this.path = path;
        this.value = value;
        this.listValue = new ArrayList<>(Collections.singletonList(value));
    }

    public String toString() {
        return Color.translate(configFile.getString(this.path));
    }
}
