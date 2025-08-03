import './App.css'
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom'

import MosaicPage from './pages/mosaic/MosaicPage.jsx'
import ArticlePage from "./pages/article/ArticlePage.jsx";
import {MosaicDataProvider} from './pages/mosaic/components/MosaicData.jsx';

function App() {
    return (
        <BrowserRouter>
            <MosaicDataProvider>
                <Routes>
                    <Route path="/" element={<Navigate to="/home" />} />
                    <Route path="/home" element={<MosaicPage/>} />
                    <Route path="/article/:articleId" element={<ArticlePage/>} />
                    <Route path="*" element={<h1>Not Found</h1>} />
                </Routes>
            </MosaicDataProvider>
        </BrowserRouter>
    )
}

export default App
