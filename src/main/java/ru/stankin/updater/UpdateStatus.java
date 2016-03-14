package ru.stankin.updater;

/**
 * Created by Dislike on 02.03.2016.
 */
enum UpdateStatus {
    PREPARED, SUCCESS, FAIL, NOT_NEED_UPDATE, LOCAL_FILE_VERSION_IS_BAD, HAVE_NO_NET_CONNECTION;

    public boolean isOk() {
        return this == SUCCESS || this == NOT_NEED_UPDATE || this == HAVE_NO_NET_CONNECTION;
    }
}
