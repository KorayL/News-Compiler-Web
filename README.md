# News Compiler 📰

## About 📚

News compiler is a resource that aggregates news articles from across the internet,
compiling them into one cohesive location with each article one click away.

- The frontend uses React with Axios to fetch data from the backend and Vite as the build tool.
- The backend uses Java's Spring Boot framework to construct a RESTful API and MySQL as the database.
- The web scraper uses Python's BeautifulSoup library to scrape articles from various news sources.
- The project is containerized entirely using Docker compose and served using Nginx.


---

## How to Deploy Yourself 🚀
1. Install [Docker Compose](https://docs.docker.com/compose/install/)
2. Clone the [repository](https://github.com/KorayL/News-Compiler-Web.git)
3. Using a Unix environment, navigate to the root directory of the project.
4. Make a copy of the `.env.template` file and rename it to `.env`: 
   ```
   cp .env.template .env
   ```
5. Fill in the `.env` file with your own values.
6. Ensure the certificate script is executable by running `chmod +x get_certificate.sh`.
7. Run the certificate script with `./get_certificate.sh` to generate a self-signed certificate for 
   the Nginx server. You will be asked to enter a number of details for the certificate.
8. Run `docker-compose up` in the root directory of the project.
9. Visit `https://localhost` in your browser.
   You will have to proceed past a security warning since the certificate is self-signed.
10. Enjoy!
11. Use `docker compose stop` and `docker compose start` to stop and start the server
    respectively.

### If you want to host globally:
1. Follow the steps above first.
2. Forward ports 80 and 443 (TCP) on your router to the host machine.
3. Ensure the application is running with the `docker compose up` command.
4. Enter into the container from another terminal window with `docker exec -it react_nginx sh`.
5. Run `certbot --nginx -d ${DOMAIN} --non-interactive --agree-tos --email ${EMAIL}`.
6. Change the nginx configuration (in `react_nginx`) in `/etc/nginx/conf.d/default.conf` to use
   the new certificates using the following two lines with `vim` or `nano`.
   Make sure to change `example.com` to your own domain name.
   ```
    ssl_certificate /etc/letsencrypt/live/example.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/example.com/privkey.pem;
   ```
7. Exit the container with `exit`.
8. Restart the nginx container with `docker compose restart frontend`. </br>
   If you messed up the configuration, the container will not start. Please follow these steps 
   to correct it:
   1. Copy the default.conf file to the current directory: `docker cp react_nginx:/etc/nginx/conf.d/default.conf .`
   2. Edit the `./default.conf` file with `vim` or `nano`.
   3. Copy the file back to the container and delete the copy we just made: `docker cp default.conf 
   react_nginx:/etc/nginx/conf.d/default.conf && rm default.conf`.
   4. Restart the nginx container with `docker compose restart frontend`.
9. Enjoy!
10. Use `docker compose stop` and `docker-compose start` to stop and start the server
    respectively.

---

## Screenshots 📸

![Whole Compiler Image](assets/mosaicImage.png)

---

![Article Image](assets/articleImage.png)
