package com.mad2man.sbweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final CorsConfiguration cors = new CorsConfiguration();
    private final ApplicationProperties.Cache cache = new ApplicationProperties.Cache();
    private final ApplicationProperties.Http http = new ApplicationProperties.Http();
    private final ApplicationProperties.Project project = new ApplicationProperties.Project();

    public ApplicationProperties() {
    }

    public CorsConfiguration getCors() {
        return this.cors;
    }

    public ApplicationProperties.Cache getCache() {
        return this.cache;
    }
    public ApplicationProperties.Http getHttp() {
        return this.http;
    }

    public ApplicationProperties.Project getProject() {
        return project;
    }

    public static class Project {

        private String name;
        private String version;
        private String url;
        private String description;

        private IssueManagement issueManagement = new ApplicationProperties.Project.IssueManagement();
        private License license = new ApplicationProperties.Project.License();

        public ApplicationProperties.Project.IssueManagement getIssueManagement() {
            return this.issueManagement;
        }

        public License getLicense() {
            return license;
        }

        public Project() {
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getDescription() {
            return description;
        }

        public String getVersion() {
            return version;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setIssueManagement(IssueManagement issueManagement) {
            this.issueManagement = issueManagement;
        }

        public void setLicense(License license) {
            this.license = license;
        }

        public static class License {

            private String name;
            private String url;

            public License() {
            }

            public String getName() {
                return name;
            }

            public String getUrl() {
                return url;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class IssueManagement {

            private String url;
            private String system;

            public IssueManagement() {
            }

            public String getUrl() {
                return url;
            }

            public String getSystem() {
                return system;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setSystem(String system) {
                this.system = system;
            }
        }
    }

    public static class Cache {

        private final ApplicationProperties.Cache.Ehcache ehcache = new ApplicationProperties.Cache.Ehcache();

        public Cache() {
        }

        public ApplicationProperties.Cache.Ehcache getEhcache() {
            return this.ehcache;
        }

        public static class Ehcache {
            private int timeToLiveSeconds = 3600;
            private long maxEntries = 100L;

            public Ehcache() {
            }

            public int getTimeToLiveSeconds() {
                return this.timeToLiveSeconds;
            }

            public void setTimeToLiveSeconds(int timeToLiveSeconds) {
                this.timeToLiveSeconds = timeToLiveSeconds;
            }

            public long getMaxEntries() {
                return this.maxEntries;
            }

            public void setMaxEntries(long maxEntries) {
                this.maxEntries = maxEntries;
            }
        }
    }

    public static class Http {

        private final ApplicationProperties.Http.Cache cache = new ApplicationProperties.Http.Cache();
        public ApplicationProperties.Http.Version version;

        public Http() {
            this.version = ApplicationProperties.Http.Version.V_1_1;
        }

        public ApplicationProperties.Http.Cache getCache() {
            return this.cache;
        }

        public ApplicationProperties.Http.Version getVersion() {
            return this.version;
        }

        public void setVersion(ApplicationProperties.Http.Version version) {
            this.version = version;
        }

        public static class Cache {
            private int timeToLiveInDays = 1461;

            public Cache() {
            }

            public int getTimeToLiveInDays() {
                return this.timeToLiveInDays;
            }

            public void setTimeToLiveInDays(int timeToLiveInDays) {
                this.timeToLiveInDays = timeToLiveInDays;
            }
        }

        public static enum Version {
            V_1_1,
            V_2_0;

            private Version() {
            }
        }
    }
}
