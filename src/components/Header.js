import React from "react";
import "../styles/Header.css";

const Header = ({ name, className }) => {
  return (
    <div className="container" id="header">
      <header className={className}>
        <h1>{name}</h1>
      </header>
    </div>
  );
};

export default Header;
