import './App.css'
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom'

import MosaicComponent from './components/MosaicComponent.jsx'
import ArticleComponent from "./components/ArticleComponent.jsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/home" />} />
                <Route path="/home" element={<MosaicComponent/>} />
                <Route path="/article/:articleId" element={<ArticleComponent/>} />
                <Route path="*" element={<h1>Not Found</h1>} />
            </Routes>
        </BrowserRouter>
    )
}

export default App
