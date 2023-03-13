do_install_append () {
    sed 's/#pragma once//' -i ${D}/${includedir}/openssl/opensslconf.h
}
