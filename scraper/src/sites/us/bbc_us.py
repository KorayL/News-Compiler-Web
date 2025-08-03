import re
from datetime import datetime

from bs4 import BeautifulSoup
from dateutil import parser

from src.Categories import Category
from src.Site import Site


class bbc_us(Site):
    def set_url(self) -> None:
        self.url = "https://www.bbc.com/news/us-canada"

    def set_source(self) -> None:
        self.source = "BBC"

    def set_category(self) -> None:
        self.category = Category.UNITED_STATES_POLITICS

    def get_article_urls(self, html: BeautifulSoup) -> list[str]:
        paths: list[str] = []

        path_tags = html.find_all(class_=re.compile("sc-8a623a54-0 hMvGwj",
                                                    flags=re.IGNORECASE),
                                  href=re.compile("^/news/"))
        for path_tag in path_tags:
            # Avoid TypeError if no link is found
            if not path_tag:
                continue

            path = path_tag["href"]

            paths.append(f"https://www.bbc.com{path}")

        return paths

    def get_title(self, html: BeautifulSoup) -> str:
        title_tag = html.find("h1", class_=re.compile("^sc-"))

        if not title_tag:
            raise ValueError("Title tag not found")

        return title_tag.getText().strip()

    def get_date(self, html: BeautifulSoup) -> datetime | None:
        try:
            date_tag = html.find("time", class_=re.compile("^sc-"))
            date: str = date_tag["datetime"]
        except (AttributeError, ValueError):
            return None

        return parser.parse(date)

    def get_image_url(self, html: BeautifulSoup) -> str | None:
        try:
            image_container = html.find("figure")
            image_tag = image_container.find("img", srcset=True)
            image_url = image_tag["src"].replace(".webp", "")
            return image_url
        except (TypeError, AttributeError):
            return None

    def get_body(self, html: BeautifulSoup) -> list[str]:
        text_tags = html.find_all("p", class_=re.compile(r"^sc-9a00e533-0"))

        body: list[str] = []
        for text_tag in text_tags:
            body.append(text_tag.getText().strip())

        return body


if __name__ == "__main__":
    """ Use this for testing. It will not run when imported in production. """
    t = bbc_us()
    t.create_articles(stacktrace=True)

    # Prints all articles
    for article in t.articles:
        print(article)
