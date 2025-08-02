import re
from datetime import datetime

import bs4
from bs4 import BeautifulSoup
from dateutil import parser

from src.Categories import Category
from src.Site import Site


class cbs_us(Site):

    def set_url(self) -> None:
        self.url = "https://www.cbsnews.com/us/"

    def set_source(self) -> None:
        self.source = "CBS News"

    def set_category(self) -> None:
        self.category = Category.UNITED_STATES_POLITICS

    def get_article_urls(self, html: BeautifulSoup) -> list[str]:
        links: list[str] = []

        articles_container = html.find("section", class_=re.compile("list.river", flags=re.IGNORECASE))
        articles = articles_container.find_all("article")

        for article in articles:
            links.append(article.find("a")["href"])

        return links

    def get_title(self, html: BeautifulSoup) -> str:
        return html.find("h1", class_=re.compile("content..title")).getText()

    def get_date(self, html: BeautifulSoup) -> datetime | None:
        try:
            date: str = html.find("p", class_=re.compile("timestamp")).find("time")["datetime"]
        except AttributeError:
            return None

        return parser.parse(date)

    def get_image_url(self, html: BeautifulSoup) -> str | None:
        try:
            poster = html.find("figure", class_=re.compile("is.video"))
            image_url = poster.find(lambda tag: tag.name == "link" and tag.has_attr("as") and tag["as"] == "image")[
                "href"]
            return image_url
        except (AttributeError, TypeError):
            try:
                return html.find("span", class_=re.compile("imbed__content")).find("img")["data-srcset"]
            except (AttributeError, TypeError):
                return None

    def get_body(self, html: BeautifulSoup) -> list[str]:
        body_container = html.find("section", class_=re.compile("content..body"))
        paragraphs = body_container.find_all("p")

        body: list[str] = []
        for paragraph in paragraphs:
            body.append(paragraph.getText())

        return body


if __name__ == "__main__":
    """ Use this for testing. It will not run when imported. """
    t = cbs_us()
    t.create_articles(stacktrace=True)
