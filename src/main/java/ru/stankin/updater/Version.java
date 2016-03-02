package ru.stankin.updater;

/**
 * Created by Dislike on 02.03.2016.
 */
class Version {
    private final int mainVersion;
    private final int subVersion;
    private final int revision;

    Version(int mainVersion, int subVersion, int revision) {
        this.mainVersion = mainVersion;
        this.subVersion = subVersion;
        this.revision = revision;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        Version version = (Version) obj;
        return version.mainVersion == mainVersion && version.subVersion == subVersion && revision == version.revision;
    }

    public boolean isOlderThen(Version ver) {
        if (mainVersion > ver.mainVersion)
            return true;
        else if (mainVersion == ver.mainVersion && subVersion > ver.subVersion)
            return true;
        else if (mainVersion == ver.mainVersion && subVersion == ver.subVersion && revision > ver.revision)
            return true;
        return false;
    }
}
