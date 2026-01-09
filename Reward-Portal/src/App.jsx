import React from "react";
import { HashRouter, Routes, Route } from "react-router-dom";
import Dashboard from "./pages/Dashboard";
import Users from "./pages/Users";
import Achievements from "./pages/Achievements";
import Rules from "./pages/Rules";
import Events from "./pages/Events";

function App() {
  return (
    <HashRouter>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/users" element={<Users />} />
        <Route path="/achievements" element={<Achievements />} />
        <Route path="/rules" element={<Rules />} />
        <Route path="/events" element={<Events />} />
      </Routes>
    </HashRouter>
  );
}

export default App;
