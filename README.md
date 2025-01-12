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
3. Make a copy of the `.env.example` file and rename it to `.env`
4. Fill in the `.env` file with your own values
5. Run `docker-compose up` in the root directory of the project
6. Visit `localhost:3000` in your browser
7. Enjoy!

---

## Screenshots 📸

![Whole Compiler Image](assets/mosaicImage.png)

---

![Article Image](assets/articleImage.png)