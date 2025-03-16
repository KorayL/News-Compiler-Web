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

# Copy the built React app to Nginx container's app directory
RUN mkdir -p /app
COPY --from=build /app/dist /app

# Update Nginx config to use react router
RUN sed -i '/location \/ {/a \        try_files \$uri /index.html;' /etc/nginx/conf.d/default.conf
# Update Nginx config to user /app 
RUN sed -i 's/\/usr\/share\/nginx\/html/\/app/' /etc/nginx/conf.d/default.conf

# Expose port 80 for the Nginx server
EXPOSE 80

# Start Nginx when the container runs
CMD ["nginx", "-g", "daemon off;"]
