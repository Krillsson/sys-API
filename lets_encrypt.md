Setup lets encrypt
Two options: 
1. use an existing web server to process the CSR with Lets Encrypt (this guide)
2. serve everything from sys-API (Dropwizard). This has it's limitations
 - You need to configure sys-API to run on port 80 for HTTP and 443 for HTTPS respectively.
 - You need to add configuration to server the challenge files through assets directory (not yet supported)
 
Following this guide assumes that you are using option 1, but it should be easy to pull off option 2. 


Prerequisites:
	- A working domain name
		- Unfortunately you cannot create a cert using lets encrypt for a IP address only: https://community.letsencrypt.org/t/certificate-for-public-ip-without-domain-name/6082/6
		  Let's Encrypt CertBot script have to be able to bind to either port 80 or 443 during certificate creation and renewal or able to serve the CSR using an existing web server.
	- This guide assumes you are using your web server to create the CSR and then use the result of that to serve HTTPS. There is a guide for doing this with purely Dropwizard.
	  But then you have to have dropwizard binded on port 80 and 443
Here's how to disable apache while doing the request
https://certbot.eff.org/#debianstretch-apache
certbot --authenticator standalone --installer apache --pre-hook "apachectl -k stop" --post-hook "apachectl -k start"
https://certbot.eff.org/#debianstretch-apache

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

Copy the below commands to a text file. Then find replace each variable with the appropriate info.

<domain name> = your domain name that you previously created a cert for
<password> = a strong password
<sys-API install dir> = directory where you run sys-API (~/sys-api)

$ openssl pkcs12 -export \
-in /etc/letsencrypt/live/<domain name>/fullchain.pem \
-inkey /etc/letsencrypt/live/<domain name>/privkey.pem \
-out <sys-API install dir>/cert/temp/pkcswww.p12 \
-name cert -password pass: <password>
$ keytool \
-deststorepass <password> \
-importkeystore -destkeypass <password> \
-destkeystore <sys-API install dir>/cert/keystorewww.jks \
-srckeystore <sys-API install dir>/cert/temp/pkcswww.p12 \
-srcstoretype PKCS12 \
-srcstorepass <password> -alias cert

cd <sys-API install dir>
mkdir cert
mkdir cert/temp