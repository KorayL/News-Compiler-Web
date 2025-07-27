import "./MosaicPage.css"

import {useEffect, useLayoutEffect, useState} from "react";
import {useMosaicData} from "./componets/MosaicData.jsx";
import {BeatLoader} from "react-spinners";
import ArticleTile from "./componets/ArticleTile.jsx";

const MosaicPage = () => {
    // Use the mosaic data context
    const {articles, loading, error} = useMosaicData();
    
    /** Article HTMLs to be placed into columns sorted by order to place onto the page */
    const [articleHTMLs, setArticleHTMLs] = useState([])
    /** An array of columns into which articles will be placed. */
    const [columns, setColumns] = useState([])

    /** Number of columns to display */
    const [numCols, setNumCols] = useState(1)

    /**
     * Creates an HTML element for the general error message or loading indicator.
     * @return {JSX.Element} The HTML element to be displayed.
     */
    function getStatusMessage() {
        if (loading) {
            // Create a fun loading indicator
            return (
                <center>
                    <br/>
                    <BeatLoader color="#ffffff">
                    </BeatLoader>
                </center>
            );
        } else if (error) {
            // Create an error message if there was an error fetching articles
            return (
                <div className="errorBox">
                    <h1>{error}</h1>
                </div>
            );
        }
        return null;
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

    // Process articles from context when they change
    useEffect(() => {
        if (articles && articles.length > 0) {
            // Create tiles from articles
            let tiles = []
            articles.forEach((article) => {
                tiles.push(
                    <ArticleTile
                        id={article.id}
                        title={article.title}
                        imageUrl={article.imageUrl}
                        source={article.source}
                    />
                )
            })
            setArticleHTMLs(tiles)
        }
    }, [articles])

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

            {getStatusMessage()}

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

export default MosaicPage
