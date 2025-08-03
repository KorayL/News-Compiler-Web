from enum import Enum

class Category(Enum):
    """
    An enumeration of categories supported by the application.
    The values of the enum represent different news categories used for classification.
    """

    UNITED_STATES_POLITICS = "UNITED_STATES_POLITICS"
    WORLD_POLITICS = "WORLD_POLITICS"
    SCIENCE = "SCIENCE"
    TECHNOLOGY = "TECHNOLOGY"
    SPORTS = "SPORTS"
    ENTERTAINMENT = "ENTERTAINMENT"
    BUSINESS = "BUSINESS"
    HEALTH = "HEALTH"
    EDUCATION = "EDUCATION"
    ENVIRONMENT = "ENVIRONMENT"
    TRAVEL = "TRAVEL"
    FOOD = "FOOD"
    LIFESTYLE = "LIFESTYLE"
    OPINION = "OPINION"
    OTHER = "OTHER"
