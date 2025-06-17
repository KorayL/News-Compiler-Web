mkdir -p frontend/certificates/openssl

# Generate a self-signed certificate
grep -v "#" cert_info.txt | \
  openssl req -x509 -noenc -days 365 -newkey rsa:2048 \
  -keyout frontend/certificates/openssl/selfsigned.key \
  -out frontend/certificates/openssl/selfsigned.crt

if [[ $? -ne 0 ]]; then
  echo ""
  echo "Local Certificate generation failed..."
  echo "Exiting."
  exit 1
fi

echo ""  # New line for better readability
