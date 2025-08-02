import re
from datetime import datetime

import bs4
from bs4 import BeautifulSoup
from dateutil import parser

from src.Categories import Category
from src.Site import Site


class template(Site):
    """
    Creating your own site class is very easy. You must inherit from the Site superclass,
    implementing all abstract methods. After that, follow the documentation for each method,
    reading over other implementations to supplement, and you should be good to go. Furthermore,
    the name of this class is functionally irrelevant.

    The only thing that matters for the abstract methods is that the return type is what is asked
    for. Your implementation can be as creative as it needs to be as long as the return types are
    met. Examples: you can override the static "get_html(url)" method, create methods, add fields/
    attributes, etc.

    You may want to add "if __name__ == "__main__":" to the bottom of this file for testing
    purposes. You can use it run functions in this class. It does not matter if you delete it
    once you're done. If you do this, start by instantiating the class. This will set_url,
    set_source, set_category, and then get_html. To test additional functions, run them
    individually or call create_articles. Create_articles will call all the other methods
    for every article obtained from the link that get_article_urls returns. In production, if
    any one of these methods fail, the entire article will be skipped.
    """

    def set_url(self) -> None:
        self.url = ""

    def set_source(self) -> None:
        self.source = ""

    def set_category(self) -> None:
        self.category = Category.OTHER

    def get_article_urls(self, html: BeautifulSoup) -> list[str]:
        pass

    def get_title(self, html: BeautifulSoup) -> str:
        pass

    def get_date(self, html: BeautifulSoup) -> datetime | None:
        try:
            # Get the date from the website
            date: str = ""
        except (AttributeError, ValueError):
            return None

        return parser.parse(date)  # Add `.astimezone()` if needed

    def get_image_url(self, html: BeautifulSoup) -> str | None:
        try:
            # Try to get the image from the website
            pass
        except (TypeError, AttributeError):
            return None

    def get_body(self, html: BeautifulSoup) -> list[str]:
        pass

if __name__ == "__main__":
    """ Use this for testing. It will not run when imported in production. """
    t = template()
    t.create_articles(stacktrace=True)

    # Prints all articles
    for article in t.articles:
        print(article)
