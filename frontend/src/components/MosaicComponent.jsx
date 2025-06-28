import "./MosaicComponent.css"

import {useEffect, useLayoutEffect, useState} from "react";

import {getRecentArticles} from "../services/ArticleService.js";
import {useNavigate} from "react-router-dom";

const MosaicComponent = () => {
    /** Article HTMLs to be placed into columns sorted by order to place onto the page */
    const [articleHTMLs, setArticleHTMLs] = useState([])
    /** An array of columns into which articles will be placed. */
    const [columns, setColumns] = useState([])

    /** Number of columns to display */
    const [numCols, setNumCols] = useState(1)

    /** Error message to be displayed if the articles cannot be fetched */
    const [error, setError] = useState("")

    /** Navigation hook to navigate to different pages */
    const navigate = useNavigate()

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
     * Opens an article by its ID.
     * @param articleId - The ID of the article to open.
     */
    function openArticle(articleId) {
        console.log("Opening article with ID: " + articleId)
        navigate(`/article/${articleId}`)
    }

    /**
     * Calculates, based on the width of the window, the number of columns the website should use to
     * make masonry grid layout.
     * @returns {number} The number of columns to be used.
     */
    function calcNumCols() {
        const width = window.innerWidth;

        if (width <= 600) {
            return 1;
        } else {
            return Math.floor(width / 300);
        }
    }

    // Get articles from the backend on render
    useEffect(() => {
        // Fetch articles on render
        getRecentArticles().then((response) => {
            // Convert article data to HTML
            let htmls = []
            response.data.forEach((article) => {
                htmls.push(
                    // Construct an article div
                    <div className="article" key={article.id}
                         onClick={() => openArticle(article.id)}>

                        <h4 className="tileTitle" id={article.title}>{article.title}</h4>
                        {/*Only include the image if it exists*/}
                        {article.imageUrl && <img src={article.imageUrl}></img>}

                        {/*Show where the article was scraped from*/}
                        <p> {article.source} </p>
                    </div>
                )
            })
            setArticleHTMLs(htmls)
        }).catch((error) => {  // Catch any errors fetching
            setError("Error fetching articles.")
            console.error(error)
        })
    }, [])

    // Updates the columns in which articles are displayed
    useEffect(() => {
        // Create a new array of columns
        let cols = []
        for (let i = 0; i < numCols; i++) {
            cols.push([])
        }

        // Place articles into columns
        let col = 0
        articleHTMLs.forEach((article) => {
            cols[col].push(article)
            col = (col + 1) % numCols
        })

        // Update the columns
        setColumns(cols)
    }, [articleHTMLs, numCols]);

    // Update the number of columns when the window is resized
    useLayoutEffect(() => {
        // Calculate the number of columns to display
        const handleResize = () => {
            setNumCols(calcNumCols())
        }

        // Run once, on the first load, before any resize occurs
        setNumCols(calcNumCols())

        // Add event listeners to update the screen width
        window.addEventListener("resize", handleResize)

        // Remove event listeners when the component is unmounted
        return () => window.removeEventListener("resize", handleResize)
    }, [numCols])

    return (
        <main className="mosaicBody">
            {/*Top Menu*/}
            <div id="menu">
                <h1>Koray&#39;s News Compiler</h1>
            </div>

            {getGeneralErrors()}

            <div className="articles">
                {
                    columns.map((col, index) => {
                        return (
                            <div className="column" key={index}>
                                {col}
                            </div>
                        )
                    })
                }
            </div>
        </main>
    )
}

export default MosaicComponent
