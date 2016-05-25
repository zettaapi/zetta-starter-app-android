package com.zetta.android.settings.licenses;

class License {
    private final String libraryName;
    private final String libraryAuthor;
    private final String type;
    private final String website;

    License(String libraryName, String libraryAuthor, String type, String website) {
        this.libraryName = libraryName;
        this.libraryAuthor = libraryAuthor;
        this.type = type;
        this.website = website;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getLibraryAuthor() {
        return libraryAuthor;
    }

    public String getType() {
        return type;
    }

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
