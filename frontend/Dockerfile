# Use the official Node.js runtime as the base image
FROM node:18 AS build

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the entire application code to the container
COPY . .

# Build the React app for production
RUN npm run build

# Use Nginx as the production server
FROM nginx:latest

ARG DOMAIN
ARG EMAIL

# Copy the built React app to Nginx container's app directory
RUN mkdir -p /app
COPY --from=build /app/dist /app

# Create SSL certificates
# RUN apt update && apt install -y certbot python3-certbot-nginx
# RUN certbot --nginx -d ${DOMAIN} --non-interactive --agree-tos --email ${EMAIL}

# Add cron job for renewing SSL certificates
# RUN echo "0 3 * * * certbot renew --quiet --post-hook 'nginx -s reload'" > /etc/cron.d/certbot

# Copy the Nginx configuration file
COPY ./nginx.conf /etc/nginx/nginx.conf
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# Replace `example.com` with the actual domain in the Nginx config
RUN sed -i "s/example.com/${DOMAIN}/g" /etc/nginx/conf.d/default.conf
RUN sed -i "s/example.com/${DOMAIN}/g" /etc/nginx/nginx.conf

# Expose port 80 for the Nginx server
EXPOSE 80

# Start Nginx when the container runs
CMD ["nginx", "-g", "daemon off;"]
