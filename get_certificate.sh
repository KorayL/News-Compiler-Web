mkdir -p frontend/certificates
cd frontend/certificates || exit

# Generate a self-signed certificate
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout selfsigned.key -out selfsigned.crt