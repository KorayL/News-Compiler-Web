mkdir -p frontend/certificates/openssl
cd frontend/certificates/openssl || exit

# Generate a self-signed certificate
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout selfsigned.key -out selfsigned.crt