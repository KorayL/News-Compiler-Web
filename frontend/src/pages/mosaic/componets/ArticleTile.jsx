import {useNavigate} from "react-router-dom";
import PropTypes from "prop-types";

/**
 * ArticleTile component displays a single article tile with its title, image, and source.
 * @param id - The ID of the article.
 * @param title - The title of the article.
 * @param imageUrl - The URL of the article's image (optional).
 * @param source - The source from which the article was scraped.
 * @returns {JSX.Element} The rendered article tile component.
 */
const ArticleTile = ({id, title, imageUrl, source}) => {

    /** Navigation hook to navigate to different pages */
    const navigate = useNavigate()

    /**
     * Opens an article by its ID.
     * @param articleId - The ID of the article to open.
     */
    function openArticle(articleId) {
        console.log("Opening article with ID: " + articleId)
        navigate(`/article/${articleId}`)
    }

    // Construct an article div
    return (<div className="article" key={id}
                 onClick={() => openArticle(id)}>

        <h4 className="tileTitle" id={title}>{title}</h4>
        {/*Only include the image if it exists*/}
        {imageUrl && <img src={imageUrl} alt={title}></img>}

        {/*Show where the article was scraped from*/}
        <p> {source} </p>
    </div>)
}

export default ArticleTile
