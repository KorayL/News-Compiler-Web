import {createContext, useContext, useEffect, useState} from "react";
import PropTypes from 'prop-types';
import {getRecentArticles} from "../../../services/ArticleService.js";

// Create context with default values
const MosaicDataContext = createContext({
    articles: [],
    loading: false,
    error: null,
    refreshArticles: () => {},  // Unused at the time of writing, may use later
});

// Custom hook to use the mosaic data context
export const useMosaicData = () => useContext(MosaicDataContext);

// Provider component that fetches and stores article data
export const MosaicDataProvider = ({children}) => {
    // State for storing the raw article data
    const [articles, setArticles] = useState([]);
    // State for storing the processed article HTML elements
    const [loading, setLoading] = useState(true);
    // Error state
    const [error, setError] = useState(null);

    // Function to fetch articles from the API
    const fetchArticles = async () => {
        setLoading(true);
        setError(null);
        
        try {
            const response = await getRecentArticles();
            setArticles(response.data);
            setLoading(false);
        } catch (err) {
            setError("Error fetching articles.");
            setLoading(false);
            console.error(err);
        }
    };

    // Fetch articles on the initial mount
    useEffect(() => {
        fetchArticles();
    }, []);

    // Value object to be provided by the context
    const value = {
        articles,
        loading,
        error,
        fetchArticles,
    };

    return (
        <MosaicDataContext.Provider value={value}>
            {children}
        </MosaicDataContext.Provider>
    );
};

// Add prop validation
MosaicDataProvider.propTypes = {
    children: PropTypes.node.isRequired
};
