import json
import os
from json import dumps
from sys import stderr

import requests
from requests import Response
from multiprocessing import Pool

from src.Site import Site
from src.sites import *


def instantiate(class_: type) -> object:
    """
    Used when multiprocessing to instantiate a class.
    :param class_: The class to instantiate.
    :return: The instantiated class.
    """
    return class_()

def get_sites() -> list[Site]:
    """
    Gets all subclasses of Site and instantiates them.
    :return: The list of instantiated sites.
    """

    print("loading sites...")

    # Instantiate all sites
    with Pool() as pool:
        sites: list[Site] = pool.map(instantiate, Site.__subclasses__())

    return sites

def _write_to_file(articles) -> None:
    """
    Writes articles to a file. The format of the articles does not matter as it will be written
    to a JSON file. The only requirement is that it must be serializable.

    The intention is to use this function for debugging and testing.
    :param articles: The articles to write.
    """

    print("writing articles to file")
    data_path = os.path.join(os.path.dirname(__file__), "articles.json")
    with open(data_path, "w") as file:
        file.write(dumps(articles, indent=4))

def main():
    # Get a list of instantiated sites
    sites = get_sites()

    # Create a dictionary with all articles from all sites
    article_list: list[dict] = []
    for i, site in enumerate(sites):
        print(f"\033[1mdownloading articles from website {i+1} of {len(sites)} — "
              f"{site.source} — {site.category.value}\033[0m")

        # Try to fetch data and create articles
        try:
            site.create_articles()

            # Format articles and add to list
            for article in site.articles:
                article_list.append(article.to_dict())
        except Exception as e:  # If any exception occurs, skip the site
            print(f"Error: {e}", file=stderr)

    # Write articles to file
    _write_to_file(article_list)

    # Send articles to backend
    print("sending articles to backend")
    response = requests.post("http://backend-rest:8080/api/articles", json=article_list)
    print(response.status_code)

if __name__ == '__main__':
    main()
