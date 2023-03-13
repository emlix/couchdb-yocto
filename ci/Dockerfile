FROM ubuntu:bionic

ARG UID=1000
ARG GID=1000

RUN apt-get update \
 && export DEBIAN_FRONTEND=noninteractive \
 && apt-get install -y sudo locales vim screen libncurses-dev \
    build-essential python3 python3-distutils \
    git gawk wget diffstat chrpath cpio \
 && rm -rf /var/lib/apt/lists/*

RUN locale-gen en_US.UTF-8 \
 && groupadd -g $GID -o ci \
 && useradd -m -u $UID -g $GID -o -s /bin/bash ci \
 && echo 'ci ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers

RUN echo '[ $BUILDDIR ] || source ci/yocto-build.env' >> /home/ci/.bashrc

USER ci
ENV LC_ALL=en_US.UTF-8
ENV DOCKERBUILD=1

CMD /bin/bash
