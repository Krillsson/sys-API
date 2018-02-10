Setup lets encrypt

Unfortunately you cannot create a cert using lets encrypt for a IP address only:https://community.letsencrypt.org/t/certificate-for-public-ip-without-domain-name/6082/6
You have to be able to bind to either port 80 or 443 during certificate creation and renewal

MacOS
1. Set unlimited security export policy like anothermode's answer here:
https://stackoverflow.com/questions/37741142/how-to-install-unlimited-strength-jce-for-java-8-in-os-x
2. Install letsencrypt using homebrew
$ brew install letsencrypt
3. Create letsencrypt directories in your home folder:
    $ mkdir ~/.certbot
    $ mkdir ~/.config/letsencrypt
    $ nano  ~/.config/letsencrypt/cli.ini
    Paste this:
    	config-dir=/Users/<username>/.certbot/config 
        work-dir=/Users/<username>/.certbot/work
        logs-dir=/Users/<username>/.certbot/logs
(https://community.letsencrypt.org/t/certbot-error-13-permission-denied-etc-letsencrypt/21325)
For some reason it didnt want to expand tilde to the home directory
3. Request cert using letsencrypt service
$ certbot certonly --standalone -d <domain> \
	--email <your-email> --agree-tos --preferred-challenges http-01
4. Follow the commandlines advice and backup the ~/.certbot/config folder now	
5. Before we start the next step make sure to generate a strong password: <password>
6. Navigate to your servers installation directory: <sys-API install dir>
7. Create the cert dir: 
    $ mkdir cert

$ openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out cert/pkcswww.p12 -name cert -password pass: <password>
$ keytool -deststorepass  <password> -importkeystore -destkeypass  <password> -destkeystore keystorewww.jks -srckeystore cert/pkcswww.p12 -srcstoretype PKCS12 -srcstorepass  <password> -alias cert