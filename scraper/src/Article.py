from datetime import datetime
from json import dumps

from src.Categories import Category

class Article:
    """ Represents an article with necessary information scraped from a news website. """

    def __init__(self, url: str, title: str, image_url: str, body: str, source: str,
                 category: Category, date: datetime | None) -> None:
        """
        Initializes an article.
        The time of construction will also be stored as the time fetched.
        
        :param url:
        :param title: The title of the article.
        :param image_url: URL to the image obtained from the scraped website.
        :param body: Body of the article.
        :param source: Where the article came from.
        :param category: What is this article about?
        :param date: The date associated with the article from the scraped site. It can be ``None``
            if no date is found.
        """

        self.url: str = url
        """ Link to the article from the original website. """
        self.title: str = title
        """ The title of the article. """
        self.image_url: str = image_url
        """ URL to the image obtained from the scraped website. """
        self.body: str = body
        """ Body of the article. """
        self.source: str = source
        """ Where the article came from. """
        self.category: Category = category
        """ What is this article about? This is how it will be sorted later. """
        self.date: datetime | None = date
        """ The time/date of publishing or editing. Can be None if no date found. """
        self.fetched_date: datetime = datetime.now().astimezone()
        """ The date and time when the article was fetched. """
        self.dict = None
        """ Dictionary containing all article information. To be put in json data file. """

    def to_dict(self) -> dict:
        """
        Stores all article data in a dictionary.
        This dictionary matches the RESTful API's expected format.

        :return: Dictionary with all article data.
        """

        if self.dict is None:
            self.dict = {
                "articleUrl": self.url,
                "imageUrl": self.image_url,
                "title": self.title,
                "body": self.body,
                "source": self.source,
                "timePublished": self.date.isoformat() if self.date else None,
                "timeFetched": self.fetched_date.isoformat(),
                "category": self.category.value,
            }

        return self.dict

    def __str__(self) -> str:
        """
        Generates a string representation of the article.

        The String representation is formatted as a JSON where each field is a key and the value
        is the corresponding value of the field.

        :return: The string representation of the article.
        """

        return dumps(self.to_dict(), indent=4)
