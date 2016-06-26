package com.zetta.android.settings.licenses;

import android.support.annotation.NonNull;

class License {
    @NonNull private final String libraryName;
    @NonNull private final String libraryAuthor;
    @NonNull private final String type;
    @NonNull private final String website;

    License(@NonNull String libraryName,
            @NonNull String libraryAuthor,
            @NonNull String type,
            @NonNull String website) {
        this.libraryName = libraryName;
        this.libraryAuthor = libraryAuthor;
        this.type = type;
        this.website = website;
    }

    @NonNull
    public String getLibraryName() {
        return libraryName;
    }

    @NonNull
    public String getLibraryAuthor() {
        return libraryAuthor;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getWebsite() {
        return website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        License license1 = (License) o;

        return libraryName.equals(license1.libraryName)
            && libraryAuthor.equals(license1.libraryAuthor)
            && type.equals(license1.type)
            && website.equals(license1.website);
    }

    @Override
    public int hashCode() {
        int result = libraryName.hashCode();
        result = 31 * result + libraryAuthor.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + website.hashCode();
        return result;
    }
}
