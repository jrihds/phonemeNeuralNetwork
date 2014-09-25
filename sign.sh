#keytool -genkey -keystore speechkeystore -keyalg rsa -dname "CN=Speech , OU=Speech, O=Speech, L=Bath, ST=PJ ,C=UK"  -alias speech -validity 3600 -keypass b1value -storepass b1valve


jarsigner -keystore speechkeystore -storepass b1valve -keypass b1valve -signedjar dest/speech.jar
