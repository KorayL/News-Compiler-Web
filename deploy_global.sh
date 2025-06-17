# Load environment variables from .env file
. .env

sudo chwon --recursive ${USER} *

# Create a local certificate so certbot can use it
chmod +x get_certificate.sh
./get_certificate.sh > /dev/null 2>&1

# Check if the certificate generation was successful
if [[ $? -ne 0 ]]; then
  echo "Local Certificate generation failed..."
  echo "Exiting."
  exit 1
else
  echo "Local Certificate generated successfully..."
fi

# Start Docker Compose
docker compose up -d --build

# Check if Docker Compose started successfully
if [[ $? -ne 0 ]]; then
  echo "Docker Compose failed to start..."
  echo "Exiting."
  exit 1
else
  echo "Docker Compose started successfully..."
fi

# Create public certifications if they do not exist
if find ./frontend/certificates/certbot/live/${DOMAIN} -name "*.pem" > /dev/null 2>&1; then
  echo "Found existing public certificates..."
  echo "Updating nginx configuration to use existing certificates..."

  # Update nginx configuration to use the new certificates
  docker exec react_nginx sed -i "/selfsigned/ s/    /    # /" /etc/nginx/conf.d/default.conf  # Comment out self-signed certificate lines
  docker exec react_nginx sed -i "/letsencrypt/ s/# //" /etc/nginx/conf.d/default.conf  # Uncomment Let's Encrypt certificate lines
else
  echo "No public certificates found, creating new ones..."

  # Create the public certifications
  docker exec react_nginx bash -c "certbot --nginx -d \$DOMAIN -d www.\$DOMAIN --non-interactive --agree-tos --email \$EMAIL --test-cert"

  # Ensure the command was successful
  if [[ $? -ne 0 ]]; then
    echo "Certbot failed to create public certificates..."
    echo "Exiting."
    exit 1
  else
    echo "Public certificates created successfully..."
  fi
fi

docker compose restart frontend
