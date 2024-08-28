ARG CBO_IMAGE_URL=armdocker.rnd.ericsson.se/proj-ldc/common_base_os_release/sles
ARG CBO_VERSION=6.16.0-13

FROM ${CBO_IMAGE_URL}:${CBO_VERSION}

ARG CBO_VERSION

ARG ARM_URL="https://arm.sero.gic.ericsson.se/artifactory/proj-ldc-repo-rpm-local"
ARG DEV_REPO="$ARM_URL/adp-dev/adp-build-env/$CBO_VERSION"
ARG CBO_REPO="$ARM_URL/common_base_os/sles/$CBO_VERSION"

RUN zypper addrepo --gpgcheck-strict -f $CBO_REPO COMMON_BASE_OS_SLES_REPO \
    && zypper addrepo --gpgcheck-strict -f $DEV_REPO ADP_DEV_BUILD_ENV_REPO \
    && zypper --gpg-auto-import-keys refresh -f \
    && zypper install -l -y curl \
    && zypper install -l -y unzip \
    && zypper install -l -y wget \
    && zypper install -l -y java-11-openjdk-headless \
    && zypper install -l -y vim \
    && zypper clean --all

COPY eric-oss-enm-fls.zip /

RUN unzip eric-oss-enm-fls.zip -d eric-oss-enm-fls
RUN chmod 777 -R /eric-oss-enm-fls

ARG USER_ID=10001
ARG USER_NAME="eric-oss-fls"
RUN echo "$USER_ID:x:$USER_ID:0:An Identity for $USER_NAME:/nonexistent:/bin/false" >>/etc/passwd

USER $USER_ID

WORKDIR /eric-oss-enm-fls/bin

ENTRYPOINT ["./file-lookup-service.sh"]