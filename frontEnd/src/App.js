import Navibar from "./navbar";
import Home from "./home";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import NewPost from "./NewPost";
import Onepost from "./Onepost";
import * as React from "react";
import "bootstrap/dist/css/bootstrap.min.css";

function App() {
  return (
    <Router>
      <div className="App">
        <Navibar />
        <div className="content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/create" element={<NewPost />} />
            <Route path="/post/:id" element={<Onepost />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
