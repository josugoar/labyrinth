ARG VARIANT="15"
FROM mcr.microsoft.com/vscode/devcontainers/java:0-${VARIANT}

ARG INSTALL_MAVEN="false"
ARG MAVEN_VERSION=""
RUN if [ "${INSTALL_MAVEN}" = "true" ]; then su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""; fi
