mkdir -p frontend/certificates
cd frontend/certificates || exit

# Generate a self-signed certificate
grep -v "#" ../../cert_info.txt | \
  openssl req -x509 -noenc -days 365 -newkey rsa:2048 -keyout selfsigned.key -out selfsigned.crt

echo ""

if [[ $? -eq 0 ]]; then
  echo "Local Certificate generation failed..."
  echo "Exiting."
  exit 1
fi

echo ""  # New line for better readability

cd ../../  # Return to the previous directory
