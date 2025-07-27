import "./ArticlePage.css"

import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {getArticleById} from "../../services/ArticleService.js";

const ArticlePage = () => {
    /** The article to be displayed */
    const [article, setArticle] = useState({})
    /** The ID of the article to be displayed */
    const {articleId} = useParams()

    /** Error message to be displayed if the articles cannot be fetched */
    const [error, setError] = useState("")

    /**
     * Creates an HTML element for the general error message.
     * @return {JSX.Element} The HTML element to be displayed.
     */
    function getGeneralErrors() {
        if (error) {
            return (
                <div className="errorBox">
                    <h1>{error}</h1>
                </div>
            )
        }
    }

    /**
     * Creates an array of HTML elements for the body of the article.
     * @param body {String} The body of the article.
     * @returns {JSX.Element[]} An array of HTML elements to be displayed.
     */
    function createBodyTags(body) {
        if (body) {
            return body.split("\n\n").map((paragraph, index) => {
                return <p className="articleBody" key={index}>{paragraph}</p>
            })
        }
    }

    /**
     * Retrieves the article to be displayed and sets the state to the article.
     */
    function getArticle() {
        getArticleById(articleId).then((response) => {
            setArticle(response.data)
        }).catch(error => {
            console.error(error)
            setError("Could not retrieve article.")
        })
    }

    // Fetch article on load
    useEffect(() => {
        getArticle()
    }, [])

    return (
        <div id="content" className="container">
            {getGeneralErrors()}

            <h1 id="title"
                onClick={() => {
                window.open(article.articleUrl)}}>
                    {article.title}
            </h1>
            <img id="image" src={article.imageUrl} alt=""/>
            <p id="meta">
                {article.source} | {(new Date(article.timePublished)).toLocaleString()}
            </p>

            <br/>
            <hr/>  {/* Horizontal line */}
            <br/>

            {/* Body tags will go here */}
            {createBodyTags(article.body)}
        </div>
    );
}

export default ArticlePage;
