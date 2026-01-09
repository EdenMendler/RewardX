import React from "react";
import Sidebar from "./Sidebar";

const Layout = ({ children, title }) => {
  return (
    <div className="flex">
      <Sidebar />
      <div className="mr-64 flex-1 p-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-6">{title}</h1>
        {children}
      </div>
    </div>
  );
};

export default Layout;
